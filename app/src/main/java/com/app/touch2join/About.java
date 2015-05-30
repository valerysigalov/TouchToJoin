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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class About extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        final String className = "AB";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        setFinishOnTouchOutside(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width  = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        Resources res = getResources();
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            DebugLog.writeLog(className, "package not found");
        }
        setTitle(res.getString(R.string.about) + " " +
                res.getString(R.string.app_title) + " " + version);
        TextView textView = (TextView) findViewById(R.id.about);
        InputStream inputStream = res.openRawResource(R.raw.long_description);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder descr = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                descr.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            DebugLog.writeLog(className, e.toString());
        }
        textView.setText(descr.toString());
    }
}
