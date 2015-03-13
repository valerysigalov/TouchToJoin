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
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UpcomingActivity extends Activity {

    private boolean wasRegistered = false;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_activity);

        listView = (ListView) findViewById(R.id.listView2);

        List<String> arrayList = new ArrayList<>();
        long currentTimeMillis = System.currentTimeMillis();
        long startTimeMillis = currentTimeMillis - TimeUnit.HOURS.toMillis(2);
        long endTimeMillis = currentTimeMillis + TimeUnit.HOURS.toMillis(22);
        ReadCalendar.getEventsByDateRange(getBaseContext(), arrayList,
                startTimeMillis, endTimeMillis);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getBaseContext(), android.R.layout.simple_list_item_single_choice, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button dialButton = (Button) findViewById(R.id.buttonDial2);
        dialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = listView.getCheckedItemPosition();
                if (pos != AdapterView.INVALID_POSITION) {
                    String selected = listView.getAdapter().getItem(pos).toString();
                    String number = selected.substring(selected.indexOf("tel:"), selected.length());
                    DebugLog.writeLog(number);
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(number));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });

        if (!wasRegistered) {
            DebugLog.writeLog("Register calendar events receiver.");
            IntentFilter eventFilter = new IntentFilter(CalendarContract.ACTION_EVENT_REMINDER);
            eventFilter.addDataScheme("content");
            registerReceiver(new EventReceiver(), eventFilter);

            DebugLog.writeLog("Register alarm receiver.");
            IntentFilter alarmFilter = new IntentFilter("com.app.JoinTheMeeting");
            registerReceiver(new SendAlarm(), alarmFilter);

            wasRegistered = true;
        }
    }

    @Override
    public void onBackPressed() {
        DebugLog.writeLog("Minimize upcoming activity.");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DebugLog.writeLog("Create menu.");
        getMenuInflater().inflate(R.menu.upcoming_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
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
                DebugLog.writeLog("Selected history option.");
                Intent history = new Intent(UpcomingActivity.this,
                        HistoryActivity.class);
                startActivity(history);
                break;
            case R.id.search:
                DebugLog.writeLog("Selected search option.");
                Intent search = new Intent(UpcomingActivity.this,
                        RangeActivity.class);
                startActivity(search);
                break;
            case R.id.settings:
                DebugLog.writeLog("Selected settings option.");
                break;
            case R.id.feedback:
                DebugLog.writeLog("Selected feedback option.");
                break;
            case R.id.help:
                DebugLog.writeLog("Selected help option.");
                break;
            case R.id.about:
                DebugLog.writeLog("Selected about option.");
                break;
            default:
                break;
        }

        return true;
    }
}
