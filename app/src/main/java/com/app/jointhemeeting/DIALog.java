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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

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
                        int index = dialogMessage.indexOf("tel:");
                        if (index != -1) {
                            String title = dialogMessage.substring(0, index);
                            String number = dialogMessage.substring(index, dialogMessage.length());
                            DebugLog.writeLog("DIALog: title " + title);
                            DebugLog.writeLog("DIALog: number " + number);
                            Intent intent = new Intent(dialogActivity, JoinActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("title", title);
                            extras.putString("number", number);
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
}
