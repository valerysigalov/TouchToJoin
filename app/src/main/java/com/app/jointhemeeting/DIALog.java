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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DIALog extends DialogFragment {

    private Activity dialogActivity;
    private String dialogMessage;

    public void setParams(Activity activity, String string) {

        dialogActivity = activity;
        dialogMessage = string;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogMessage)
                .setPositiveButton(R.string.dial, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<String> data = new ArrayList<>();
                        int size = findPhone(data);
                        if (size > 2) {
                            Intent intent = new Intent(dialogActivity, JoinActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("date", data.get(0));
                            extras.putString("title", data.get(1));
                            extras.putString("number", data.get(2));
                            if (size == 4) {
                                extras.putString("pin", data.get(3));
                            }
                            else {
                                extras.putString("pin", "none");
                            }
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                        dismiss();
                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    private int findPhone(ArrayList<String> data) {

        final String subject = "(.*) Subject: (.*)";
        String num = ", Phone: (.*)";
        final String pin = ", PIN: (.*)";
        final String reg_num = subject + num;
        final String conf_num = subject + num + pin;

        Matcher match_subj = Pattern.compile(subject, Pattern.CASE_INSENSITIVE).matcher(dialogMessage);
        Matcher match_num = Pattern.compile(reg_num, Pattern.CASE_INSENSITIVE).matcher(dialogMessage);
        Matcher match_conf = Pattern.compile(conf_num, Pattern.CASE_INSENSITIVE).matcher(dialogMessage);
        if(match_conf.matches()) {
            return fillArray(match_conf, data);
        }
        else if(match_num.matches()) {
            return fillArray(match_num, data);
        }
        else if(match_subj.matches()) {
            return fillArray(match_subj, data);
        }
        return 0;
    }

    private int fillArray(Matcher matcher, ArrayList<String> data) {

        for (int index = 1; index <= matcher.groupCount(); index++) {
            data.add(matcher.group(index));
        }
        return matcher.groupCount();
    }
}
