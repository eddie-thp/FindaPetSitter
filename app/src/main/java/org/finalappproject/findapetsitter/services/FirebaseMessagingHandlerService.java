package org.finalappproject.findapetsitter.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.parse.GetCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.RequestDetailActivity;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.pushmessage.PushMessageHelper;

import java.util.Map;

/**
 * Service responsible for processing push notifications from the Parse backend (through FCM)
 * for more info see: https://developers.google.com/cloud-messaging/android/android-migrate-fcm
 */
public class FirebaseMessagingHandlerService extends FirebaseMessagingService {
    private static final String TAG = "FCMHandlerService";
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        // FCM SenderId
        String from = remoteMessage.getFrom();

        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                Log.d(TAG, "Received data [" + entry.getKey() + "]: " + entry.getValue());
            }

            // TODO validate that to user matches current user

            if (data.containsKey(PushMessageHelper.KEY_REQUEST_OBJECT_ID)) {
                // Create notification for received pet sitting request
                String requestId = data.get(PushMessageHelper.KEY_REQUEST_OBJECT_ID);
                Request.queryRequest(requestId, new GetCallback<Request>() {
                    @Override
                    public void done(Request request, ParseException e) {
                        notifyNewRequest(request);
                    }
                });
            } else {
                // TODO create and if for received chat message
            }
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            createNotification(notification);
        }
    }

    private void notifyNewRequest(Request request) {
        Context context = getBaseContext();
        try {
            // TODO improve error handling here

            User sender = (User) request.getSender().fetchIfNeeded();
            User receiver = (User) request.getSender().fetchIfNeeded();

            User currentUser = (User) User.getCurrentUser().fetchIfNeeded();

            Intent detailsIntent = new Intent(getApplicationContext(), RequestDetailActivity.class);
            detailsIntent.putExtra("request_id", request.getObjectId());
            // Assume pet sitting request received
            detailsIntent.putExtra("request_type", true);
            String title = context.getString(R.string.notification_pet_sitting_request_title, sender.getNickName());
            // Pet sitting response received
            if (currentUser.getObjectId().equals(sender.getObjectId())) {
                detailsIntent.putExtra("request_type", false);
                title = context.getString(R.string.notification_pet_sitting_response_title, receiver.getNickName());
            }

            createNotification(title, request.getNote(), detailsIntent);
        } catch(ParseException e) {
            Log.d(TAG, "Failed to retrieve user information", e);
        }
    }

    // Creates notification based on title and body received
    private void createNotification(RemoteMessage.Notification notification) {
        createNotification(notification.getTitle(), notification.getBody(), null);
    }

    private void createNotification(String title, String body, Intent intent) {
        Context context = getBaseContext();

        // TODO set custom icon, intent, etc

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.push_notification_logo).setContentTitle(title)
                .setContentText(body);

        if (intent != null) {
            int requestID = (int) System.currentTimeMillis();
            PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }
}