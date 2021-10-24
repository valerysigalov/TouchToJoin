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

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class FileIO {

    public static boolean Read(String fileName, ArrayList<String> arrayList) {

        BufferedReader bufferedReader;
        String line;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((line = bufferedReader.readLine()) != null) {
                arrayList.add(line);
                DebugLog.writeLog("FileIO: read line \"" + line + "\" from file " + fileName);
            }
            bufferedReader.close();
            return true;
        } catch (IOException e) {
            DebugLog.writeLog("FileIO: read file " + fileName + " failed with error " + e);
            return false;
        }
    }

    public static boolean Write(String fileName, String line) {

        ArrayList<String> arrayList = new ArrayList<>();
        Read(fileName, arrayList);
        HashSet<String> hashSet = new HashSet<>();
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
                DebugLog.writeLog("FileIO: create new file " + fileName);
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String history = SettingsActivity.getValue("history");
            int size = Integer.parseInt(history);
            bufferedWriter.write(line + "\n");
            hashSet.add(line);
            size = Math.min(arrayList.size(), size - 1);
            for (int i = 0; i < size; i++) {
                line = arrayList.get(i);
                if (hashSet.add(line)) {
                    bufferedWriter.write(line + "\n");
                    DebugLog.writeLog("FileIO: write line \"" + line + "\" to file " + fileName);
                }
            }
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            DebugLog.writeLog("FileIO: write file " + fileName + " failed with error " + e);
            return false;
        }
    }
}
