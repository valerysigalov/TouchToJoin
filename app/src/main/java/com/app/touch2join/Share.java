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
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Share extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        shareApp();
        finish();
    }

    private void shareApp() {

        final String className = "SR";
        String mailId = "";
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", mailId, null));
        Resources res = getResources();
        InputStream inputStream = res.openRawResource(R.raw.short_description);
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
        String subject = "Check out " + res.getString(R.string.app_title) + "!";
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(
                Intent.EXTRA_TEXT,
                Html.fromHtml(new StringBuilder()
                        .append("<p>" + descr.toString() + "</p>")
                        .append("<a href=" + res.getString(R.string.link) + ">" +
                                res.getString(R.string.link) + "</a>")
                        .toString())
        );
        try {
            startActivity(Intent.createChooser(intent, res.getString(R.string.send)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Share.this, res.getString(R.string.noclients),
                    Toast.LENGTH_LONG).show();
        }
    }
}
