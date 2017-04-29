package com.nadina.android.todolistwithnotifications.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;

import com.nadina.android.todolistwithnotifications.R;
import com.nadina.android.todolistwithnotifications.sync.NotificationUtils;
import com.nadina.android.todolistwithnotifications.utilities.ReminderUtilities;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Nadina on 28.04.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @BindString(R.string.show_notifications_key) String show_notifications_key;
    @BindBool(R.bool.pref_show_notifications) boolean pref_show_notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(show_notifications_key,
                pref_show_notifications))  {
            NotificationUtils.sendActionToNotification(this);
            ReminderUtilities.scheduleChargingReminder(this);
        }
    }
}