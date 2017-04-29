package com.nadina.android.todolistwithnotifications;

/**
 * Created by Nadina on 28.04.2017.
 */


import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.nadina.android.todolistwithnotifications.data.TaskContract;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddTaskActivity extends AppCompatActivity {

    private int mPriority;
    @BindView(R.id.radButton1)
    RadioButton rButton1;
    @BindView(R.id.radButton2) RadioButton rButton2;
    @BindView(R.id.radButton3) RadioButton rButton3;
    @BindView(R.id.editTextTaskDescription)
    EditText etTaskDescription;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        rButton1.setChecked(true);
        mPriority = 1;
    }


    public void onClickAddTask(View view) {
        String input = etTaskDescription.getText().toString();
        if (input.length() == 0) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, input);
        contentValues.put(TaskContract.TaskEntry.COLUMN_PRIORITY, mPriority);
        getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);

        finish();

    }

    public void onPrioritySelected(View view) {
        if (rButton1.isChecked()) {
            mPriority = 1;
        } else if (rButton2.isChecked()) {
            mPriority = 2;
        } else if (rButton3.isChecked()) {
            mPriority = 3;
        }
    }
}