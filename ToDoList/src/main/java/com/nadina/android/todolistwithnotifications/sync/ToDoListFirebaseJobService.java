package com.nadina.android.todolistwithnotifications.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Nadina on 28.04.2017.
 */

public class ToDoListFirebaseJobService extends JobService {

    private AsyncTask mAsyncTask;

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mAsyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context =ToDoListFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_SET_NOTIFICATION);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                jobFinished(jobParameters, false);
            }
        };
        mAsyncTask.execute();
        return true;
    }

}

