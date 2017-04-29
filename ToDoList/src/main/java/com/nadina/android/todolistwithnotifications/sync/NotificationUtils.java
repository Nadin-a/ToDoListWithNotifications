package com.nadina.android.todolistwithnotifications.sync;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.nadina.android.todolistwithnotifications.MainActivity;
import com.nadina.android.todolistwithnotifications.R;
import com.nadina.android.todolistwithnotifications.data.TaskContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nadina on 28.04.2017.
 */

public class NotificationUtils {

    private static final int TODOLIST_REMINDER_NOTIFICATION_ID = 1138;
    private static final int TODO_REMINDER_PENDING_INTENT_ID = 3417;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;


    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static String sendActionToNotification(Context context) {
        String notification_text = context.getString(R.string.not);
        int priority = 1;
        String stringPr = Integer.toString(priority);
        Uri uri = TaskContract.TaskEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringPr).build();

        Cursor mCursor = context.getContentResolver().query(
                uri,
                new String[]{
                        TaskContract.TaskEntry.COLUMN_DESCRIPTION},
                TaskContract.TaskEntry.COLUMN_PRIORITY + " = ?",
                new String[]{Integer.toString(priority)}, null);

        if (mCursor == null) {
            Log.d("====", "cursor is null");
        } else if (mCursor.getCount() < 1) {
            Log.d("=====", "cursor < -1");
        } else {
            int descriptionIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);

            List<String> important_notifications = new ArrayList<>();

            while (mCursor.moveToNext()) {
                String description = mCursor.getString(descriptionIndex);
                important_notifications.add(description);
            }

            if (!important_notifications.isEmpty()) {
                notification_text = context.getString(R.string.your);
                for (String s : important_notifications) {
                    notification_text += "\n" + s;
                }
            }
        }
        return notification_text;
    }


    public static void remindUser(Context context) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(context.getString(R.string.reminder))
                .setContentText(context.getString(R.string.doSomething))
                .setSmallIcon(R.drawable.ic_todo)
                .setLargeIcon(largeIcon(context))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(sendActionToNotification(context)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }


        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getBoolean(context.getString(R.string.show_notifications_key),
                context.getResources().getBoolean(R.bool.pref_show_notifications))) {
            notificationManager.notify(TODOLIST_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
        }
    }


    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, ToDoListIntentService.class);
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.ic_cancel_pic,
                context.getString(R.string.close),
                ignoreReminderPendingIntent);
        return ignoreReminderAction;
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,
                TODO_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_todo);
        return largeIcon;
    }

}
