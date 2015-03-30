package com.app.touchtojoin;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.CalendarContract;

class RegisterReceiver extends Activity {

    private static boolean wasRegistered = false;

    public static void registerReceiver(Context context) {

        if (wasRegistered == false) {
            DebugLog.writeLog("RegisterReceiver: register calendar events receiver.");
            IntentFilter eventFilter = new IntentFilter(CalendarContract.ACTION_EVENT_REMINDER);
            eventFilter.addDataScheme("content");
            context.getApplicationContext().registerReceiver(new EventReceiver(), eventFilter);

            DebugLog.writeLog("RegisterReceiver: register alarm receiver.");
            IntentFilter alarmFilter = new IntentFilter("com.app.touchtojoin");
            context.getApplicationContext().registerReceiver(new SendAlarm(), alarmFilter);

            wasRegistered = true;
        }
    }
}
