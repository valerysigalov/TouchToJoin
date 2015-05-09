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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class CallListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager telephonyManager =
                (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        CallStateListener callStateListener = new CallStateListener(context);
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class CallStateListener extends PhoneStateListener {

        private final String className = "CallStateListener";
        private final Context context;

        CallStateListener(Context context) {

            this.context = context;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            Bundle extras;
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    extras = Preferences.restoreLastCall(context);
                    if (extras != null) {
                        if (incomingNumber.equals(extras.getString("number").replace(" ", ""))) {
                            DebugLog.writeLog(className, "call number " + incomingNumber + " ended.");
                            try {
                                long currentTimeInMillis = System.currentTimeMillis();
                                String parseEndTime = extras.getString("date").trim() + " " +
                                        extras.getString("end").trim();
                                DebugLog.writeLog(className, "parse end time " + parseEndTime);
                                DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                                Date endTime = formatter.parse(parseEndTime);
                                long endTimeInMillis = endTime.getTime();
                                if (endTimeInMillis > currentTimeInMillis) {
                                    Intent rejoin = new Intent(context, REDIALog.class);
                                    rejoin.putExtras(extras);
                                    rejoin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    DebugLog.writeLog(className, "Start rejoin dialog for call " + incomingNumber);
                                    context.startActivity(rejoin);
                                }
                            } catch (ParseException e) {
                                DebugLog.writeLog(className, "failed to parse time " + e.toString());
                            }
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    extras = Preferences.restoreLastCall(context);
                    if (extras != null && incomingNumber.equals(extras.getString("number").trim())) {
                        DebugLog.writeLog(className, "call number " + incomingNumber + " started.");
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    DebugLog.writeLog(className, "call number " + incomingNumber + " ringing.");
                    break;
            }
        }
    }
}
