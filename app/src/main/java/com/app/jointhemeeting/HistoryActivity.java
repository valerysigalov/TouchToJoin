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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends Activity {

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        historyLog();

        Button dialButton = (Button) findViewById(R.id.buttonDial3);
        dialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = listView.getCheckedItemPosition();
                if (pos != AdapterView.INVALID_POSITION) {
                    String selected = listView.getAdapter().getItem(pos).toString();
                    String title = selected.substring(0, selected.indexOf("tel:"));
                    String number = selected.substring(selected.indexOf("tel:"), selected.length());
                    DebugLog.writeLog("HistoryActivity: title " + title);
                    DebugLog.writeLog("HistoryActivity: number " + number);
                    Intent intent = new Intent(HistoryActivity.this, JoinActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("title", title);
                    extras.putString("number", number);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {

        DebugLog.writeLog("HistoryActivity: resume history activity.");
        super.onResume();
        historyLog();
    }

    private void historyLog() {

        ArrayList<String> arrayList = new ArrayList<>();
        if (FileIO.Read(arrayList)) {
            listView = (ListView) findViewById(R.id.listView3);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getBaseContext(), android.R.layout.simple_list_item_single_choice, arrayList);
            listView.setAdapter(arrayAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
}
