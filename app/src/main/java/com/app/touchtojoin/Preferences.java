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
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity
{
    private final String className = "Preferences";

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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
        private static Preference callBack;

        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
            callBack = findPreference(getResources().getString(R.string.call_id));
        }

        @Override
        public void onResume() {

            super.onResume();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
            if (sharedPreferences.contains(callBack.getContext().getResources().getString(R.string.call_id))) {
                String confInfo = sharedPreferences.getString(callBack.getContext().getResources().getString(R.string.call_id),
                        callBack.getContext().getResources().getString(R.string.no_call));
                DebugLog.writeLog(className, "refresh active conference - " + confInfo);
                callBack.setSummary(confInfo);
            }
        }

        static void saveActiveConference(String confInfo) {

            DebugLog.writeLog(className, "save active conference - " + confInfo);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(callBack.getContext());
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(callBack.getContext().getResources().getString(R.string.call_id), confInfo);
            edit.commit();

            callBack.setSummary(confInfo);
        }
    }
}
