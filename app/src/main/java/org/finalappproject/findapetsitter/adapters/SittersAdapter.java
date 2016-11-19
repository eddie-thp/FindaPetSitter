package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.UserProfileActivity;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.LinkedList;
import java.util.List;

import static org.finalappproject.findapetsitter.activities.UserProfileEditActivity.EXTRA_USER_OBJECT_ID;

public class SittersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static List<User> mAvailableSitters;
    private static Context mContext;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sitter, parent, false);
        viewHolder = new UserViewHolder(mContext, view, mAvailableSitters);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        UserViewHolder vh = (UserViewHolder) holder;
        final User sitter = mAvailableSitters.get(position);
        vh.tvItemFirstName.setText(sitter.getFullName());
        //vh.tvTagline.setText(sitter.getDescription());
        vh.tvTagline.setText("Description/Tagline" /*sitter.getTagLine()*/);

        ImageHelper.loadImage(mContext, sitter.getProfileImage(), R.drawable.cat, vh.ivItemProfilePic);

        vh.tvNumReviews.setText("45");
        vh.tvRatings.setText("4.8");

        vh.RlSitterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userProfileIntent = new Intent(getContext(), UserProfileActivity.class);
                userProfileIntent.putExtra(EXTRA_USER_OBJECT_ID, sitter.getObjectId());
                getContext().startActivity(userProfileIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mAvailableSitters.size();
    }

    public SittersAdapter(Context context, LinkedList<User> availablSitters) {
        this.mAvailableSitters = availablSitters;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }
}