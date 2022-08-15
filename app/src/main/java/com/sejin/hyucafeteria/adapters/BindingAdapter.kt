package com.sejin.hyucafeteria.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.sejin.hyucafeteria.data.UrlDate
import com.sejin.hyucafeteria.utilities.getKoreanDayOfWeek
import com.sejin.hyucafeteria.utilities.getMonthDotDayString
import com.sejin.hyucafeteria.utilities.toLocalDate

@BindingAdapter("currentDate")
fun bindCurrentDate(textview: TextView, urlDate: UrlDate) {
    textview.text = urlDate.getMonthDotDayString()
}

@BindingAdapter("koreanDayOfWeek")
fun bindKoreanDayOfWeek(textview: TextView, urlDate: UrlDate) {
    textview.text = urlDate.getKoreanDayOfWeek()
}