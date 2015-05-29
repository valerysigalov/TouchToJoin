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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PhoneNumber {

    private static final String country_code = ".*?(((\\+?\\s*[0-9]{0,3}\\s*\\-?\\s*";
    private static final String us_country_code = ".*?(((\\+\\s*1\\s*\\-?\\s*";
    private static final String area_code = "\\(?\\s*[0-9]{3}\\s*\\)?\\s*\\-?\\s*";
    private static final String us_toll_free_codes = "\\(?\\s*(800|888|877|866|855|844)\\s*\\)?\\s*\\-?\\s*";
    private static final String phone_number = "\\(?\\s*[0-9]{3}\\s*\\)?\\s*\\-?\\s*\\(?\\s*[0-9]{4}\\s*\\)?))).*";
    private static final String pin_code = ".*?((\\D|\\s)([0-9]{5,8})(\\D|\\s|$)).*";
    private static final String pin_code_ex = ".*(Access|Pin|Code|Id|Conference)([\\w\\s:]+?)([0-9\\s-]+)(\\s|#|,?).*";
    private static final String us_pattern = "(\\bUSA\\b|\\bUNITED STATES\\b|\\bUnited States\\b)";

    public static String findNumber(String text) {

        String subtext = text;
        Pattern pattern = Pattern.compile(us_pattern);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find())
            subtext = text.substring(matcher.start());

        String phoneNumber;
        String toll_free_number = us_country_code + us_toll_free_codes + phone_number;
        phoneNumber = matchNumber(toll_free_number, subtext);
        if (phoneNumber == null) {
            String us_number = country_code + area_code + phone_number;
            phoneNumber = matchNumber(us_number, subtext);
        }

        return phoneNumber;
    }

    public static String findPinCode(String text, String phoneNumber) {

        String pinCode = matchNumber(pin_code_ex, text);
        if (pinCode == null) {
            if (phoneNumber != null) {
                int index = text.indexOf(phoneNumber);
                if (index != -1) {
                    text = text.substring(index);
                }
            }
            pinCode = matchNumber(pin_code, text);
        }

        return pinCode;
    }

    private static String matchNumber(String number, String text) {

        String phoneNumber = null;
        if (text == null) {
            return null;
        }
        Pattern pattern = Pattern.compile(number, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find())
            phoneNumber = matcher.group(3);

        return phoneNumber;
    }
}
