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

package com.app.touch2join;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class Feedback extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sendFeedback();
        finish();
    }

    private void sendFeedback() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Resources res = getResources();
        String subject;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            subject = res.getString(R.string.app_title) + " " + pInfo.versionName +
                    " " + res.getString(R.string.subject);
        } catch (PackageManager.NameNotFoundException e) {
            final String className = "FB";
            DebugLog.writeLog(className, "package not found");
            subject = res.getString(R.string.app_title) + " " +
                      res.getString(R.string.subject);
        }

        String device = res.getString(R.string.device) +  " " + android.os.Build.MODEL;
        String os = res.getString(R.string.os) +  " " +  Build.VERSION.RELEASE;
        String settings = res.getString(R.string.settings) + " " +
                res.getString(R.string.pause) + " " + Preferences.getInt(this, "pause",
                res.getInteger(R.integer.pause_def)) + " " + res.getString(R.string.seconds) + ", " +
                res.getString(R.string.snooze) + " " + Preferences.getInt(this, "snooze",
                res.getInteger(R.integer.snooze_def)) + " " + res.getString(R.string.minutes) + ", " +
                res.getString(R.string.remind) + " " + Preferences.getInt(this, "remind",
                res.getInteger(R.integer.remind_def)) + " " + res.getString(R.string.minutes);
        String log = "";
        if (Preferences.isLogEnabled(this)) {
            log = res.getString(R.string.logcat) + "\n\n" + DebugLog.getLog() + "\n\n";
        }
        String body = device + ", " + os + ".\n" + settings + ".\n\n" + log +
                res.getString(R.string.bug) + "\n\n" + res.getString(R.string.suggestion) + "\n\n" +
                res.getString(R.string.other) + "\n\n";

        String uriText = "mailto:" + res.getString(R.string.mailto) +
                         "?subject=" + Uri.encode(subject) +
                         "&body=" + Uri.encode(body);
        Uri uri = Uri.parse(uriText);
        intent.setData(uri);

        try {
            startActivity(Intent.createChooser(intent, res.getString(R.string.send)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Feedback.this, res.getString(R.string.noclients),
                    Toast.LENGTH_LONG).show();
        }
    }
}
