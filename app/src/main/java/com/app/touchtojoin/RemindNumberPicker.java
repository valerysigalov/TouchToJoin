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
import android.util.AttributeSet;

class RemindNumberPicker extends NumberPickerPreference {

    public RemindNumberPicker(Context context, AttributeSet attrs) {

        super(context, attrs);
        attrName = "remind";
        timeUnit = getContext().getResources().getString(R.string.minutes);
        minValue = getContext().getResources().getInteger(R.integer.remind_min);
        maxValue = getContext().getResources().getInteger(R.integer.remind_max);
        defValue = getContext().getResources().getInteger(R.integer.remind_def);
        stepValue = getContext().getResources().getInteger(R.integer.remind_step);
        onSetInitialValue();
    }
}
