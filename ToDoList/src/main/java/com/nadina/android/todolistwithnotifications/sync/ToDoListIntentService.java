package com.nadina.android.todolistwithnotifications.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Nadina on 28.04.2017.
 */

public class ToDoListIntentService extends IntentService {

    public ToDoListIntentService() {
        super("ToDoListIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this,action);
    }
}