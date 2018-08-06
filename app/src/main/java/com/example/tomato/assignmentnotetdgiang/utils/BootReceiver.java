package com.example.tomato.assignmentnotetdgiang.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tomato.assignmentnotetdgiang.database.DBManager;
import com.example.tomato.assignmentnotetdgiang.model.Note;

import java.util.Calendar;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    private String mTitle;
    private String mTime;
    private String mDate;

    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mYear, mMonth, mHour, mMinute, mDay, mNoteID;

    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            DBManager db = new DBManager(context);
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();

            List<Note> notes = db.getAllNotes();

            for (Note n : notes) {
                mNoteID = n.getID();
                mDate = n.getDate();
                mTime = n.getTime();

                mDateSplit = mDate.split("-");
                mTimeSplit = mTime.split(":");

                mDay = Integer.parseInt(mDateSplit[0]);
                mMonth = Integer.parseInt(mDateSplit[1]);
                mYear = Integer.parseInt(mDateSplit[2]);
                mHour = Integer.parseInt(mTimeSplit[0]);
                mMinute = Integer.parseInt(mTimeSplit[1]);

                //  mCalendar.set(Calendar.MONTH, --mMonth);
                mCalendar.set(Calendar.MONTH, mMonth);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                mCalendar.set(Calendar.MINUTE, mMinute);
                mCalendar.set(Calendar.SECOND, 0);

                // Create a new notification
                mAlarmReceiver.setAlarm(context, mCalendar, mNoteID);
            }
        }
    }
}
