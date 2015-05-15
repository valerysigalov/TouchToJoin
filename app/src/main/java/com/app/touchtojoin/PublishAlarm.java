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
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

public class PublishAlarm extends BroadcastReceiver {

    public static final String notificationID = "notificationID";
    public static final String notificationData = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(notificationData);
        Integer id = intent.getIntExtra(notificationID, 0);

        final String className = "PublishAlarm";
        DebugLog.writeLog(className, "id=" + id);
        notificationManager.notify(id, notification);
    }
}
