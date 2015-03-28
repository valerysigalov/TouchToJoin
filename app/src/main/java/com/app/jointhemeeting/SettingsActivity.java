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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends Activity {

    private static Activity instance;
    private static Map<String, String> defaults;

    private EditText editDelay;
    private EditText editSnooze;
    private EditText editHistory;
    private CheckBox checkEvents;
    private Button   applyButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        DebugLog.writeLog("SettingsActivity: call onCreate.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getActionBar().setTitle(getString(R.string.settings));

        editDelay = (EditText) findViewById(R.id.editText);
        editSnooze = (EditText) findViewById(R.id.editText2);
        editHistory = (EditText) findViewById(R.id.editText3);
        checkEvents = (CheckBox) findViewById(R.id.checkBox);
        applyButton = (Button) findViewById(R.id.button);

        restoreSettings();

        applyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    public static void setDefaults(Activity activity) {

        DebugLog.writeLog("SettingsActivity: set default settings.");
        defaults = new HashMap<>();
        defaults.put("delay", "3");
        defaults.put("snooze", "5");
        defaults.put("history", "25");
        defaults.put("events", "conf");
        instance = activity;
    }

    public void saveSettings() {

        DebugLog.writeLog("SettingsActivity: save settings.");
        SharedPreferences.Editor edit = instance.getPreferences(MODE_PRIVATE).edit();
        edit.putString("delay", editDelay.getText().toString());
        DebugLog.writeLog("SettingsActivity: save settings - delay = " + editDelay.getText().toString());
        edit.putString("snooze", editSnooze.getText().toString());
        DebugLog.writeLog("SettingsActivity: save settings - snooze = " + editSnooze.getText().toString());
        edit.putString("history", editHistory.getText().toString());
        DebugLog.writeLog("SettingsActivity: save settings - history = " + editHistory.getText().toString());
        if (checkEvents.isChecked()) {
            edit.putString("events", "all");
            DebugLog.writeLog("SettingsActivity: save settings - events = all");
        }
        else {
            edit.putString("events", "conf");
            DebugLog.writeLog("SettingsActivity: save settings - events = conf");
        }
        edit.commit();
    }

    public void restoreSettings() {

        SharedPreferences pref = instance.getPreferences(MODE_PRIVATE);
        if (pref != null) {
            editDelay.setText(getValue("delay"));
            editSnooze.setText(getValue("snooze"));
            editHistory.setText(getValue("history"));
            if (getValue("events").equals("all")) {
                checkEvents.setChecked(true);
            }
            else {
                checkEvents.setChecked(false);
            }
        }
    }

    public static String getValue(String name) {

        String value = null;
        SharedPreferences pref = instance.getPreferences(MODE_PRIVATE);
        if (pref != null) {
            value = pref.getString(name, null);
        }
        if (value == null) {
            value = defaults.get(name);
        }
        DebugLog.writeLog("SettingsActivity: name " + name + ", value " + value);
        return value;
    }

    public static String setDelay() {

        String delay = getValue("delay");
        int comma = Integer.parseInt(delay);
        delay = "";
        for (int i = 0; i < comma; i++) {
            delay += ",";
        }

        return delay;
    }
}
