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

class ActivityStatus {

    private static boolean isVisible;

    public static boolean isVisible() {
        if (isVisible == true) {
            DebugLog.writeLog("ActivityStatus: application is running on foreground while snoozing alarm.");
        } else {
            DebugLog.writeLog("ActivityStatus: application is running on background while snoozing alarm.");
        }
        return isVisible;
    }

    public static void Resumed() {
        DebugLog.writeLog("ActivityStatus: application is running on foreground.");
        isVisible = true;
    }

    public static void Paused() {
        DebugLog.writeLog("ActivityStatus: application is running on background.");
        isVisible = false;
    }
}
