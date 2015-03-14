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

package com.app.jointhemeeting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends Activity {

    private static Activity instance;
    private static Map<String, String> defaults;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        DebugLog.writeLog("SettingsActivity: call onCreate.");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.upcoming_activity);
    }

    public static void setDefaults(Activity activity) {

        DebugLog.writeLog("SettingsActivity: set default settings.");
        defaults = new HashMap<>();
        defaults.put("delay", "3");
        defaults.put("snooze", "5");
        defaults.put("history", "25");
        instance = activity;
    }

    public void saveSettings() {

        DebugLog.writeLog("SettingsActivity: save settings.");
        SharedPreferences.Editor edit = getPreferences(MODE_PRIVATE).edit();
        edit.putString("", "");
        edit.commit();
    }

    public void restoreSettings() {

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        if (pref != null) {
            DebugLog.writeLog("SettingsActivity: restore saved settings.");
            String value;
            value = pref.getString("history", null);
        }
    }

    public static String getValue(String name) {

        String value = null;
        SharedPreferences pref = instance.getPreferences(MODE_PRIVATE);
        if (pref != null) {
            value = pref.getString(name, null);
            if (value == null) {
                value = defaults.get(name);
            }
        }
        DebugLog.writeLog("SettingsActivity: name " + name + ", value " + value);
        return value;
    }
}
