package com.example.insighttest;

import android.annotation.SuppressLint;

import androidx.lifecycle.ViewModel;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;

public class InsightPhotoViewModel extends ViewModel {

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mInsightDateFormat = new SimpleDateFormat("yyy-MM-dd");
    private final DateTime mInsightSolZero = new DateTime(DateTime.parse("2018-11-26T00:00:00.000Z"));
    private DateTime mCurrentDate = new DateTime(DateTimeZone.UTC);


}
