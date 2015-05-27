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

/*
 * Classes' names map:
 * AB - About
 * BR - BootReceiver
 * CB - CallBack
 * CL - CallListener
 * CT - Contacts
 * ER - EventReceiver
 * FB - Feedback
 * JC - JoinCall
 * PR - Preferences
 * PA - PublishAlarm
 * RC - ReadCalendar
 * RD - REDialog
 * RR - RegisterReceiver
 * SD - SendAlarm
 * SZ - SnoozeAlarm
 */

package com.app.touchtojoin;

import android.util.Log;
import java.sql.Timestamp;
import java.util.Arrays;

class DebugLog {

    public static void writeLog(String className, String msg) {
        Log.d("TouchToJoin", className + ": " + msg);
        DebugLogQueue.enqueue(new Timestamp(System.currentTimeMillis()).toString().substring(5) + " " + className + ": " + msg);
    }

    public static String getLog() {
        return DebugLogQueue.queueString();
    }

    public static void clear() { DebugLogQueue.clear(); }

    private static class DebugLogQueue {

        private static final int qSize = 1000;
        private static int fPtr = 0;
        private static int rPtr = 0;
        private static Object [] qObject = new Object[qSize];

        public static void enqueue(Object obj) {
            if (isFull()) {
                dequeue();
            }
            qObject[rPtr] = obj;
            rPtr = (rPtr + 1) % qSize;
        }

        public static String queueString() {
            String log = "";
            int fp = fPtr;
            while (fp != rPtr) {
                log += (qObject[fp] + "\n");
                fp = (fp + 1) % qSize;
            }
            return log;
        }

        public static void clear() {
            fPtr = 0;
            rPtr = 0;
            Arrays.fill(qObject, null);
        }

        private static boolean isEmpty() {
            return (rPtr == fPtr);
        }

        private static boolean isFull() {
            int diff = rPtr - fPtr;
            return (diff == -1 || diff == (qSize - 1));
        }

        private static Object dequeue() {
            Object item;
            if(isEmpty()) {
                return null;
            } else {
                item = qObject[fPtr];
                qObject[fPtr] = null;
                fPtr = (fPtr + 1) % qSize;
            }
            return item;
        }
    }
}
