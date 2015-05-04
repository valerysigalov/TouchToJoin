/*
 * Copyright (c) 2015
 * Michael Franz (misha.franz@gmail.com)
 * Valery Sigalov (valery.sigalov@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.touchtojoin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;

import java.text.DateFormat;
import java.util.Date;

class ReadCalendar {

    private static final String className = "ReadCalendar";
    private static String savedTime = null;

    public static void getEventByTime(Context context, Intent intent) {

        String alarmTime = intent.getData().getLastPathSegment();

        if (savedTime != null && alarmTime.equals(savedTime)) {
            DebugLog.writeLog(className, "the event was already processed.");
            return;
        }
        savedTime = alarmTime;

        String[] projection = new String[] { CalendarContract.CalendarAlerts.EVENT_LOCATION,
                CalendarContract.CalendarAlerts.DESCRIPTION,
                CalendarContract.CalendarAlerts.TITLE,
                CalendarContract.CalendarAlerts.DTSTART,
                CalendarContract.CalendarAlerts.DTEND};

        String selection = CalendarContract.CalendarAlerts.ALARM_TIME + "=?";

        Cursor cursor = context.getContentResolver().query(
                CalendarContract.CalendarAlerts.CONTENT_URI_BY_INSTANCE,
                projection, selection, new String[]{alarmTime}, null);

        String phoneNumber = null;
        String pinCode = null;
        String title = "call";
        String date = "now";
        String begin = "now";
        String end = "now";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    DateFormat fmt_date = DateFormat.getDateInstance(DateFormat.SHORT);
                    DateFormat fmt_time = DateFormat.getTimeInstance(DateFormat.SHORT);
                    long time = cursor.getLong(cursor.getColumnIndex(
                            CalendarContract.CalendarAlerts.DTSTART));
                    date = fmt_date.format(new Date(time));
                    DebugLog.writeLog(className, "date " + date);
                    begin = fmt_time.format(new Date(time));
                    DebugLog.writeLog(className, "begin " + begin);
                    time = cursor.getLong(cursor.getColumnIndex(
                            CalendarContract.CalendarAlerts.DTEND));
                    end = fmt_time.format(new Date(time));
                    DebugLog.writeLog(className, "end " + end);
                    title = cursor.getString(cursor.getColumnIndex(
                            CalendarContract.CalendarAlerts.TITLE));
                    if (title.equals("")) {
                        title = "(No title)";
                    }
                    DebugLog.writeLog(className, "title " + title);
                    phoneNumber = PhoneNumber.findNumber(title);
                    pinCode = PhoneNumber.findPinCode(title, phoneNumber);
                    if (phoneNumber == null || pinCode == null) {
                        String location = cursor.getString(cursor.getColumnIndex(
                                CalendarContract.CalendarAlerts.EVENT_LOCATION));
                        DebugLog.writeLog(className, "location " + location);
                        phoneNumber = PhoneNumber.findNumber(location);
                        pinCode = PhoneNumber.findPinCode(location, phoneNumber);
                    }
                    if (phoneNumber == null || pinCode == null) {
                        String description = cursor.getString(cursor.getColumnIndex(
                                CalendarContract.CalendarAlerts.DESCRIPTION));
                        DebugLog.writeLog(className, "description " + description);
                        phoneNumber = PhoneNumber.findNumber(description);
                        pinCode = PhoneNumber.findPinCode(description, phoneNumber);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (phoneNumber == null || pinCode == null) {
            DebugLog.writeLog(className, "failed to find valid phone number and PIN code in calendar event.");
        }
        else {
            DebugLog.writeLog(className, "the conference phone number is " + phoneNumber);
            DebugLog.writeLog(className, "the conference PIN code is " + pinCode);
            DebugLog.writeLog(className, "start notification activity.");
            Intent notification = new Intent(context, SendAlarm.class);
            Bundle extras = new Bundle();
            extras.putString("date", date.trim());
            extras.putString("begin", begin.trim());
            extras.putString("end", end.trim());
            extras.putString("title", title.trim());
            extras.putString("number", phoneNumber.trim());
            extras.putString("pin", pinCode.trim());
            notification.putExtras(extras);
            context.sendBroadcast(notification);
        }
    }
}
