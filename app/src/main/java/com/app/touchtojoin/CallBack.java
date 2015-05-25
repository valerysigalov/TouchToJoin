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
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class CallBack extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = Preferences.restoreLastCall(this);
        if (extras != null) {
            final String className = "CB";
            DebugLog.writeLog(className, extras.toString());
            String number = extras.getString("number").trim();
            if (CallListener.isCallActive(number)) {
                DebugLog.writeLog(className, "call " + number + " in progress");
                Toast.makeText(CallBack.this, getResources().getString(R.string.inprogress),
                        Toast.LENGTH_LONG).show();
            } else {
                String pin = extras.getString("pin").trim();
                Intent join = new Intent(Intent.ACTION_CALL);
                CallListener.setActiveCall(number);
                number = "tel:" + number + Preferences.setDelay(this) + pin + "#";
                join.setData(Uri.parse(number));
                join.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(join);
            }
        }
        finish();
    }
}
