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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends Activity {

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        getActionBar().setTitle(getString(R.string.history));

        historyLog();
    }

    @Override
    public void onResume() {

        DebugLog.writeLog("HistoryActivity: resume history activity.");
        super.onResume();
        historyLog();
    }

    private void historyLog() {

        ArrayList<String> arrayList = new ArrayList<>();
        if (FileIO.Read(this.getFilesDir() + "/history.log", arrayList)) {
            listView = (ListView) findViewById(R.id.listView3);
            ButtonAdapter arrayAdapter = new ButtonAdapter(arrayList, this);
            listView.setAdapter(arrayAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
}
