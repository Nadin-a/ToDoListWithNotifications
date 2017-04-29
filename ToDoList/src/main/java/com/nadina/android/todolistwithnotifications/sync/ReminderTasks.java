package com.nadina.android.todolistwithnotifications.sync;

import android.content.Context;

/**
 * Created by Nadina on 28.04.2017.
 */

public class ReminderTasks {

    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_SET_NOTIFICATION = "set-notification";


    public static void executeTask(Context context, String action) {
        if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        }
        else if(ACTION_SET_NOTIFICATION.equals(action))
        {
            NotificationUtils.remindUser(context);
        }
    }


}

