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
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity
{
    private static final String className = "PR";
    private static InternalFragment internalFragment = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTheme(android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
        } else {
            setTheme(android.R.style.Theme_DeviceDefault);
        }
        super.onCreate(savedInstanceState);

        RegisterReceiver.registerReceiver(Preferences.this);

        internalFragment = new InternalFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, internalFragment).commit();

        setTitle(getResources().getString(R.string.app_title));
    }

    @Override
    public void onResume() {

        super.onResume();
        refreshLastCall(this);

        ListPreference enableLog = (ListPreference)internalFragment.findPreference("log");
        enableLog.setSummary(getString(this, "log", getResources().getString(R.string.disable)));
        enableLog.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Preferences.putString(Preferences.this, "log", (String) newValue);
                preference.setSummary((String) newValue);
                return true;
            }
        });

        Preference call = internalFragment.findPreference("call");
        call.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(Preferences.this, CallBack.class);
                startActivity(intent);
                return true;
            }
        });

        Preference mail = internalFragment.findPreference("mail");
        mail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(Preferences.this, Feedback.class);
                startActivity(intent);
                return true;
            }
        });

        Preference share = internalFragment.findPreference("share");
        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(Preferences.this, Share.class);
                startActivity(intent);
                return true;
            }
        });

        Preference about = internalFragment.findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(Preferences.this, About.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        internalFragment = null;
    }

    private void refreshLastCall(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String confInfo = sharedPreferences.getString("call",
                context.getResources().getString(R.string.no_call));
        DebugLog.writeLog(className, "last call - " + confInfo);
        if (internalFragment != null && internalFragment.findPreference("call") != null)
            internalFragment.findPreference("call").setSummary(confInfo);
    }

    public static String getString(Context context, String key, String defVal) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, defVal);
    }

    private static void putString(Context context, String key, String value) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static Integer getInt(Context context, String key, Integer defVal) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, defVal);
    }

    public static void putInt(Context context, String key, Integer value) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static Boolean isLogEnabled(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("log",
                context.getResources().getString(R.string.disable)).equals(context.getResources().getString(R.string.enable));
    }

    public static void saveLastCall(Context context, Bundle callInfo) {

        DebugLog.writeLog(className, "save call - " + callInfo.toString());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String date = callInfo.getString("date");
        edit.putString("date", date);
        String begin = callInfo.getString("begin");
        edit.putString("begin", begin);
        String end = callInfo.getString("end");
        edit.putString("end", end);
        String title = callInfo.getString("title");
        edit.putString("title", title);
        String number = callInfo.getString("number");
        edit.putString("number", number);
        String pin = callInfo.getString("pin");
        edit.putString("pin", pin);
        String confInfo = title + "\n" + begin.replaceAll("[^0-9:]", "").trim() +
                " \u2013 " + end + " " + number + "x" + pin + "#";
        edit.putString("call", confInfo);
        edit.commit();
        if (internalFragment != null && internalFragment.findPreference("call") != null)
            internalFragment.findPreference("call").setSummary(confInfo);
    }

    public static Bundle restoreLastCall(Context context) {

        Bundle callInfo = new Bundle();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String date = sharedPreferences.getString("date", null);
        if (date != null) {
            callInfo.putString("date", date);
        } else {
            DebugLog.writeLog(className, "date=null");
            return null;
        }
        String begin = sharedPreferences.getString("begin", null);
        if (begin != null) {
            callInfo.putString("begin", begin);
        } else {
            DebugLog.writeLog(className, "begin=null");
            return null;
        }
        String end = sharedPreferences.getString("end", null);
        if (end != null) {
            callInfo.putString("end", end);
        } else {
            DebugLog.writeLog(className, "end=null");
            return null;
        }
        String title = sharedPreferences.getString("title", null);
        if (title != null) {
            callInfo.putString("title", title);
        } else {
            DebugLog.writeLog(className, "title=null");
            return null;
        }
        String number = sharedPreferences.getString("number", null);
        if (number != null) {
            callInfo.putString("number", number);
        } else {
            DebugLog.writeLog(className, "number=null");
            return null;
        }
        String pin = sharedPreferences.getString("pin", null);
        if (pin != null) {
            callInfo.putString("pin", pin);
        } else {
            DebugLog.writeLog(className, "pin=null");
            return null;
        }
        DebugLog.writeLog(className, "restore call - " + callInfo.toString());
        return callInfo;
    }

    public static String setDelay(Context context) {

        String delay = "";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Integer comma = sharedPreferences.getInt("pause",
                context.getResources().getInteger(R.integer.pause_def)) / 2;
        for (int i = 0; i < comma; i++) {
            delay += ",";
        }
        DebugLog.writeLog(className, "delay=" + delay);
        return delay;
    }

    public static void setDefaults(Context context) {

        DebugLog.writeLog(className, "restore defaults");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("call", context.getResources().getString(R.string.no_call));
        edit.putString("log", context.getResources().getString(R.string.disable));
        edit.putInt("pause", context.getResources().getInteger(R.integer.pause_def));
        edit.putInt("snooze", context.getResources().getInteger(R.integer.snooze_def));
        edit.putInt("remind", context.getResources().getInteger(R.integer.remind_def));
        edit.commit();
        DebugLog.clear();
        if (internalFragment != null) {
            internalFragment.setPreferenceScreen(null);
            internalFragment.addPreferencesFromResource(R.xml.preferences);
        }
    }

    public static class InternalFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
