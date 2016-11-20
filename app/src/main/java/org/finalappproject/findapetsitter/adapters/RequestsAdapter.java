package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aoi on 11/19/2016.
 */

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private List<Request> mRequests;
    private Context mContext;

    private Context getContext() {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivRequestProfile)
        ImageView ivRequestProfile;
        @BindView(R.id.tvRequestReceived)
        TextView tvRequestReceived;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public RequestsAdapter(Context context, ArrayList<Request> requests) {
        mRequests = requests;
        mContext = context;
    }

    public RequestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_request, parent, false);
        RequestsAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RequestsAdapter.ViewHolder viewHolder, int position) {
        final Request request = mRequests.get(position);

        ImageHelper.loadImage(mContext, request.getSender().getProfileImage(), R.drawable.cat, viewHolder.ivRequestProfile);
        viewHolder.tvRequestReceived.setText(String.format("You got a request from %s", request.getSender().getFullName()));
    }

    @Override
    public int getItemCount() {
        return this.mRequests.size();
    }
}