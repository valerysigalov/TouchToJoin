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
import android.widget.Toast;

public class Share extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        shareApp();
        finish();
    }

    private void shareApp() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Resources res = getResources();
        String subject = "Check out " + res.getString(R.string.app_title) + "!";
        String body = res.getString(R.string.short_description) + "\n\n" +
                res.getString(R.string.link);

        String uriText = "mailto:" +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(body);
        Uri uri = Uri.parse(uriText);
        intent.setData(uri);

        try {
            startActivity(Intent.createChooser(intent, res.getString(R.string.send)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Share.this, res.getString(R.string.noclients),
                    Toast.LENGTH_LONG).show();
        }
    }}
