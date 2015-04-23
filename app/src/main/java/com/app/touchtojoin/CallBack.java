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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class CallBack extends Activity {

    private static final String className = "CallBack";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(this.getResources().getString(R.string.call_id))) {
            String confInfo = sharedPreferences.getString(this.getResources().getString(R.string.call_id),
                    this.getResources().getString(R.string.no_call));

            String delim = ",\\stel:\\s?|,\\sext:\\s?";
            String [] confInfoArray = confInfo.split(delim);
            Bundle extras = new Bundle();
            extras.putString("date", "now");
            extras.putString("begin", "now");
            extras.putString("end", "now");
            extras.putString("title", confInfoArray[0]);
            DebugLog.writeLog(className, "conference title is " + confInfoArray[0]);
            extras.putString("number", confInfoArray[1]);
            DebugLog.writeLog(className, "conference number is " + confInfoArray[1]);
            extras.putString("pin", confInfoArray[2]);
            DebugLog.writeLog(className, "conference pin is " + confInfoArray[2]);

            Intent join = new Intent(this, JoinCall.class);
            join.putExtras(extras);
            join.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(join);
        }

        finish();
    }
}
