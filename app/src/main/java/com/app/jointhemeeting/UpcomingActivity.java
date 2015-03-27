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
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UpcomingActivity extends Activity {

    private boolean wasRegistered = false;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_activity);

        getActionBar().setTitle(getString(R.string.upcoming));

        listView = (ListView) findViewById(R.id.listView2);

        DebugLog.writeLog("UpcomingActivity: set default settings.");
        SettingsActivity.setDefaults(this);

        upcomingEvents();

        if (!wasRegistered) {
            DebugLog.writeLog("UpcomingActivity: register calendar events receiver.");
            IntentFilter eventFilter = new IntentFilter(CalendarContract.ACTION_EVENT_REMINDER);
            eventFilter.addDataScheme("content");
            registerReceiver(new EventReceiver(), eventFilter);

            DebugLog.writeLog("UpcomingActivity: register alarm receiver.");
            IntentFilter alarmFilter = new IntentFilter("com.app.JoinTheMeeting");
            registerReceiver(new SendAlarm(), alarmFilter);

            wasRegistered = true;
        }
    }

    @Override
    public void onResume() {

        DebugLog.writeLog("UpcomingActivity: resume upcoming activity.");
        super.onResume();
        upcomingEvents();
    }

    @Override
    public void onBackPressed() {

        DebugLog.writeLog("UpcomingActivity: minimize upcoming activity.");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DebugLog.writeLog("UpcomingActivity: create menu.");
        getMenuInflater().inflate(R.menu.upcoming_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method method = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.history:
                DebugLog.writeLog("UpcomingActivity: selected history option.");
                Intent history = new Intent(UpcomingActivity.this,
                        HistoryActivity.class);
                startActivity(history);
                break;
            case R.id.search:
                DebugLog.writeLog("UpcomingActivity: selected search option.");
                Intent search = new Intent(UpcomingActivity.this,
                        RangeActivity.class);
                startActivity(search);
                break;
            case R.id.settings:
                DebugLog.writeLog("UpcomingActivity: selected settings option.");
                Intent settings = new Intent(UpcomingActivity.this,
                        SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.feedback:
                DebugLog.writeLog("UpcomingActivity: selected feedback option.");
                break;
            case R.id.help:
                DebugLog.writeLog("UpcomingActivity: selected help option.");
                break;
            case R.id.about:
                DebugLog.writeLog("UpcomingActivity: selected about option.");
                break;
            default:
                break;
        }

        return true;
    }

    private void upcomingEvents() {

        ArrayList<String> arrayList = new ArrayList<>();
        long currentTimeMillis = System.currentTimeMillis();
        long startTimeMillis = currentTimeMillis - TimeUnit.HOURS.toMillis(3);
        long endTimeMillis = currentTimeMillis + TimeUnit.DAYS.toMillis(7);
        DebugLog.writeLog("UpcomingActivity: read calendar from " + startTimeMillis + " to " + endTimeMillis);
        ReadCalendar.getEventsByDateRange(getBaseContext(), arrayList,
                startTimeMillis, endTimeMillis);
        ButtonAdapter arrayAdapter = new ButtonAdapter(arrayList, this);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
}
