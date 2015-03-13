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

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class StreamIO {

    public static ArrayList<String> ReadFile(Context context, String fileName) {

        File file = new File(fileName);
        if (!file.exists())
            return null;

        boolean result = true;

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ArrayList<String> arrayList = null;

        try {
            fis = context.openFileInput(fileName);
        } catch (IOException e) {
            DebugLog.writeLog("StreamIO: open file input stream " + fileName + " failed with error " + e);
            result = false;
        }

        if (result) {
            try {
                ois = new ObjectInputStream(fis);
            } catch (IOException e) {
                DebugLog.writeLog("StreamIO: open object input stream " + fileName + " failed with error " + e);
                result = false;
            }
        }

        if (result) {
            try {
                arrayList = (ArrayList<String>) ois.readObject();
            } catch (ClassNotFoundException | IOException e) {
                DebugLog.writeLog("StreamIO: read file " + fileName + " failed with error: " + e);
            }
        }

        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                DebugLog.writeLog("StreamIO: close file input stream " + fileName + " failed with error: " + e);
            }
        }

        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                DebugLog.writeLog("StreamIO: close object input stream " + fileName + " failed with error: " + e);
            }
        }

        return arrayList;
    }

    public static void WriteFile(Context context, String fileName, ArrayList<String> arrayList) {

        boolean result = true;

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (IOException e) {
            DebugLog.writeLog("StreamIO: open file output stream " + fileName + " failed with error " + e);
            result = false;
        }

        if (result) {
            try {
                oos = new ObjectOutputStream(fos);
            } catch (IOException e) {
                DebugLog.writeLog("StreamIO: open object output stream " + fileName + " failed with error " + e);
                result = false;
            }
        }

        if (result) {
            try {
                oos.writeObject(arrayList);
            } catch (IOException e) {
                DebugLog.writeLog("StreamIO: write file " + fileName + " failed with error: " + e);
            }
        }

        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                DebugLog.writeLog("StreamIO: close file output stream " + fileName + " failed with error: " + e);
            }
        }

        if (oos != null) {
            try {
                oos.close();
            } catch (IOException e) {
                DebugLog.writeLog("StreamIO: close object output stream " + fileName + " failed with error: " + e);
            }
        }
    }
}
