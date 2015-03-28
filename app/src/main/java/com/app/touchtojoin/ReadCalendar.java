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

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

class ReadCalendar {

    private static String savedTime = null;

    public static void getEventByTime(Context context, Intent intent) {

        String alarmTime = intent.getData().getLastPathSegment();

        if (savedTime != null && alarmTime.equals(savedTime)) {
            DebugLog.writeLog("ReadCalendar: the event was already processed.");
            return;
        }
        savedTime = alarmTime;

        String[] projection = new String[] { CalendarContract.CalendarAlerts.EVENT_LOCATION,
                CalendarContract.CalendarAlerts.DESCRIPTION,
                CalendarContract.CalendarAlerts.TITLE,
                CalendarContract.CalendarAlerts.DTSTART};

        String selection = CalendarContract.CalendarAlerts.ALARM_TIME + "=?";

        Cursor cursor = context.getContentResolver().query(
                CalendarContract.CalendarAlerts.CONTENT_URI_BY_INSTANCE,
                projection, selection, new String[]{alarmTime}, null);

        String phoneNumber = null;
        String pinCode = null;
        String title = "call";
        String date = "now";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    long start = cursor.getLong(cursor.getColumnIndex(
                            CalendarContract.CalendarAlerts.DTSTART));
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    date = formatter.format(new Date(start));
                    DebugLog.writeLog("ReadCalendar: date " + date);
                    title = cursor.getString(cursor.getColumnIndex(
                            CalendarContract.CalendarAlerts.TITLE));
                    DebugLog.writeLog("ReadCalendar: title " + title);
                    phoneNumber = PhoneNumber.findNumber(title);
                    pinCode = PhoneNumber.findPinCode(title);
                    if (phoneNumber == null || pinCode == null) {
                        String location = cursor.getString(cursor.getColumnIndex(
                                CalendarContract.CalendarAlerts.EVENT_LOCATION));
                        DebugLog.writeLog("ReadCalendar: location " + location);
                        phoneNumber = PhoneNumber.findNumber(location);
                        pinCode = PhoneNumber.findPinCode(location);
                    }
                    if (phoneNumber == null || pinCode == null) {
                        String description = cursor.getString(cursor.getColumnIndex(
                                CalendarContract.CalendarAlerts.DESCRIPTION));
                        DebugLog.writeLog("ReadCalendar: description " + description);
                        phoneNumber = PhoneNumber.findNumber(description);
                        pinCode = PhoneNumber.findPinCode(description);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (phoneNumber == null || pinCode == null) {
            DebugLog.writeLog("ReadCalendar: failed to find valid phone number and PIN code in calendar event.");
        }
        else {
            DebugLog.writeLog("ReadCalendar: the conference phone number is " + phoneNumber);
            DebugLog.writeLog("ReadCalendar: the conference PIN code is " + pinCode);
            DebugLog.writeLog("ReadCalendar: start notification activity.");
            Intent notification = new Intent(context, SendAlarm.class);
            Bundle extras = new Bundle();
            extras.putString("date", date.trim());
            extras.putString("title", title.trim());
            extras.putString("number", phoneNumber.trim());
            extras.putString("pin", pinCode.trim());
            notification.putExtras(extras);
            context.sendBroadcast(notification);
        }
    }

    public static void getEventsByDateRange(Context context, List<String> arrayList,
                long startTimeInMillis, long endTimeInMillis) {

        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(eventsUriBuilder, startTimeInMillis);
        ContentUris.appendId(eventsUriBuilder, endTimeInMillis);
        Uri eventsUri = eventsUriBuilder.build();

        String[] projection = new String[] { CalendarContract.Instances.EVENT_LOCATION,
                CalendarContract.Instances.DESCRIPTION,
                CalendarContract.Instances.TITLE,
                CalendarContract.Instances.BEGIN};

        Cursor cursor = context.getContentResolver().query( eventsUri, projection, null, null, CalendarContract.Instances.BEGIN + " ASC" );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String phoneNumber;
                String pinCode;
                String title;
                HashSet hashSet = new HashSet();
                do {
                    title = cursor.getString(cursor.getColumnIndex(
                            CalendarContract.Instances.TITLE)).trim();
                    phoneNumber = PhoneNumber.findNumber(title);
                    pinCode = PhoneNumber.findPinCode(title);
                    if (phoneNumber == null || pinCode == null) {
                        String location = cursor.getString(cursor.getColumnIndex(
                                CalendarContract.Instances.EVENT_LOCATION));
                        phoneNumber = PhoneNumber.findNumber(location);
                        pinCode = PhoneNumber.findPinCode(location);
                        if (phoneNumber == null || pinCode == null) {
                            String description = cursor.getString(cursor.getColumnIndex(
                                    CalendarContract.Instances.DESCRIPTION));
                            phoneNumber = PhoneNumber.findNumber(description);
                            pinCode = PhoneNumber.findPinCode(description);
                        }
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    long start = cursor.getLong(cursor.getColumnIndex(
                            CalendarContract.Instances.BEGIN));
                    String date = formatter.format(new Date(start)).trim();
                    if (phoneNumber != null && pinCode != null) {
                        phoneNumber = phoneNumber.trim();
                        pinCode = pinCode.trim();
                        if (hashSet.add(title + " " + phoneNumber + " " + pinCode) ||
                                SettingsActivity.getValue("events").equals("all")) {
                            arrayList.add(date + " Subject: " + title + ", Phone: " + phoneNumber + ", PIN: " + pinCode);
                            DebugLog.writeLog("ReadCalendar: " + date + " Subject: " + title + ", Phone: " + phoneNumber + ", PIN: " + pinCode);
                        }
                    }
                    else if (SettingsActivity.getValue("events").equals("all")) {
                        if (phoneNumber != null) {
                            phoneNumber = phoneNumber.trim();
                            arrayList.add(date + " Subject: " + title + ", Phone: " + phoneNumber);
                            DebugLog.writeLog("ReadCalendar: " + date + " Subject: " + title + ", Phone: " + phoneNumber);
                        }
                        else {
                            arrayList.add(date + " Subject: " + title);
                            DebugLog.writeLog("ReadCalendar: " + date + " Subject: " + title);
                        }
                    }
                } while (cursor.moveToNext());
            }
            Collections.sort(arrayList);
            cursor.close();
        }
    }
}
