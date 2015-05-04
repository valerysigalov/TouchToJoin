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

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity
{

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String className = "Preferences";
        DebugLog.writeLog(className, "register broadcast receivers.");
        RegisterReceiver.registerReceiver(Preferences.this);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new InternalFragment()).commit();

        String appVersion = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            DebugLog.writeLog(className, "package not found.");
        }
        setTitle(getResources().getString(R.string.app_title) + "  " + appVersion);
    }

    public static class InternalFragment extends PreferenceFragment {

        private static final String className = "InternalFragment";
        private static Preference callBack = null;
        private static InternalFragment internalFragment = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
            callBack = findPreference("call");
            internalFragment = this;
        }

        @Override
        public void onResume() {

            super.onResume();

            if (callBack != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
                String confInfo = sharedPreferences.getString("call",
                        callBack.getContext().getResources().getString(R.string.no_call));
                DebugLog.writeLog(className, "refresh last call - " + confInfo);
                callBack.setSummary(confInfo);
            }
            else {
                DebugLog.writeLog(className, "Cannot refresh last call - context is null");
            }
        }

        public static Integer getInt(String key, Integer defVal) {

            if (callBack != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
                Integer value = sharedPreferences.getInt(key, defVal);
                DebugLog.writeLog(className, "return integer value " + key + " : " + value);
                return value;
            }
            DebugLog.writeLog(className, "return default integer value " + key + " : " + defVal + " - context is null");
            return defVal;
        }

        public static void putInt(String key, Integer value) {

            if (callBack != null) {
                DebugLog.writeLog(className, "save integer " + key + " : " + value);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putInt(key, value);
                edit.commit();
            } else {
                DebugLog.writeLog(className, "cannot save integer " + key + " : " + value + " - context is null");
            }
        }

        public static void saveLastCall(Bundle callInfo) {

            if (callBack != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
                SharedPreferences.Editor edit = sharedPreferences.edit();
                String date = callInfo.getString("date");
                DebugLog.writeLog(className, "save last call - date is " + date);
                edit.putString("date", date);
                String begin = callInfo.getString("begin");
                DebugLog.writeLog(className, "save last call - begin time is " + begin);
                edit.putString("begin", begin);
                String end = callInfo.getString("end");
                DebugLog.writeLog(className, "save last call - end time is " + end);
                edit.putString("end", end);
                String title = callInfo.getString("title");
                DebugLog.writeLog(className, "save last call - title is " + title);
                edit.putString("title", title);
                String number = callInfo.getString("number");
                DebugLog.writeLog(className, "save last call - number is " + number);
                edit.putString("number", number);
                String pin = callInfo.getString("pin");
                DebugLog.writeLog(className, "save last call - pin is " + pin);
                edit.putString("pin", pin);
                String confInfo = title + "\n" + begin.replaceAll("[^0-9:]", "").trim() + " \u2013 " + end + " " + number + "x" + pin + "#";
                DebugLog.writeLog(className, "save last call - " + confInfo);
                edit.putString("call", confInfo);
                edit.commit();
                callBack.setSummary(confInfo);
            }
            else {
                DebugLog.writeLog(className, "Cannot save last call - context is null");
            }
        }

        public static Bundle restoreLastCall() {

            if (callBack != null) {
                DebugLog.writeLog(className, "restore last call");
                Bundle callInfo = new Bundle();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
                String date = sharedPreferences.getString("date", null);
                if (date != null) {
                    DebugLog.writeLog(className, "conference date is " + date);
                    callInfo.putString("date", date);
                } else {
                    DebugLog.writeLog(className, "conference date is null");
                    return null;
                }
                String begin = sharedPreferences.getString("begin", null);
                if (begin != null) {
                    DebugLog.writeLog(className, "conference begin time is " + begin);
                    callInfo.putString("begin", begin);
                } else {
                    DebugLog.writeLog(className, "conference begin time is null");
                    return null;
                }
                String end = sharedPreferences.getString("end", null);
                if (end != null) {
                    DebugLog.writeLog(className, "conference end time is " + end);
                    callInfo.putString("end", end);
                } else {
                    DebugLog.writeLog(className, "conference end time is null");
                    return null;
                }
                String title = sharedPreferences.getString("title", null);
                if (title != null) {
                    DebugLog.writeLog(className, "conference title is " + title);
                    callInfo.putString("title", title);
                } else {
                    DebugLog.writeLog(className, "conference title is null");
                    return null;
                }
                String number = sharedPreferences.getString("number", null);
                if (number != null) {
                    DebugLog.writeLog(className, "conference number is " + number);
                    callInfo.putString("number", number);
                } else {
                    DebugLog.writeLog(className, "conference number is null");
                    return null;
                }
                String pin = sharedPreferences.getString("pin", null);
                if (pin != null) {
                    DebugLog.writeLog(className, "conference pin is " + pin);
                    callInfo.putString("pin", pin);
                } else {
                    DebugLog.writeLog(className, "conference pin is null");
                    return null;
                }
                return callInfo;
            }
            DebugLog.writeLog(className, "There is no call to restore");
            return null;
        }

        public static String setDelay() {

            String delay = ",,,";
            if (callBack != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
                Integer comma = sharedPreferences.getInt("pause",
                        callBack.getContext().getResources().getInteger(R.integer.pause_def)) / 2;
                delay = "";
                for (int i = 0; i < comma; i++) {
                    delay += ",";
                }
            }
            DebugLog.writeLog(className, "Set delay " + delay);
            return delay;
        }

        public static void setDefaults() {

            if (callBack != null) {
                DebugLog.writeLog(className, "Restore default settings.");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                edit.commit();
                edit.putString("call", callBack.getContext().getResources().getString(R.string.no_call));
                edit.putInt("pause", callBack.getContext().getResources().getInteger(R.integer.pause_def));
                edit.putInt("snooze", callBack.getContext().getResources().getInteger(R.integer.snooze_def));
                edit.putInt("remind", callBack.getContext().getResources().getInteger(R.integer.remind_def));
                edit.commit();
                internalFragment.setPreferenceScreen(null);
                internalFragment.addPreferencesFromResource(R.xml.preferences);
            }
            else {
                DebugLog.writeLog(className, "Cannot restore default settings - context is null");
            }
        }
    }
}
