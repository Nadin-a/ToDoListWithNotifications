package com.nadina.android.todolistwithnotifications.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.nadina.android.todolistwithnotifications.R;
import com.nadina.android.todolistwithnotifications.sync.ToDoListFirebaseJobService;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nadina on 28.04.2017.
 * Getting time from user.
 */

public class ReminderUtilities {

    static SharedPreferences mSharedPreferences;

    private static int mReminderIntervalMinutes;
    private static int mReminderIntervalSeconds;
    private static int mSyncFlextimeSeconds;

    private static final String REMINDER_JOB_TAG = "todolist_reminder_tag";


    private static boolean sInitialized;

    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {
        if(sInitialized) return;

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mReminderIntervalMinutes = Integer.parseInt(mSharedPreferences.getString(context.getString(R.string.notifications_time_interval_key),
                context.getString(R.string.pref_interval_default)));
        mReminderIntervalSeconds =  (int) (TimeUnit.MINUTES.toSeconds(mReminderIntervalMinutes));
        mSyncFlextimeSeconds = mReminderIntervalSeconds;

        Log.d("===","time " + mReminderIntervalMinutes);


        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(ToDoListFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        mReminderIntervalSeconds,
                        mReminderIntervalSeconds + mSyncFlextimeSeconds))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);
        sInitialized = true;

    }



}
