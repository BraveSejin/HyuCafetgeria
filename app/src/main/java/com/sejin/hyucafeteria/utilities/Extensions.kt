package com.sejin.hyucafeteria.utilities

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.sejin.hyucafeteria.data.Cafeteria
import com.sejin.hyucafeteria.data.Meal
import com.sejin.hyucafeteria.data.UrlDate
import java.time.LocalDate
import java.time.Month

fun String.trimEmptyLines(): String {
    val list = this.split("\n").filter { it.trim().isNotEmpty() }
    return list.joinToString("\n")
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Cafeteria.getInfo(): String {
    var res = "'"
    res += "식당 id: $id\n"
    res += "식당 이름: $name\n"
    res += "식당 위치: $location\n"
    res += "식당 운영시간: ${notice}\n"
    return res.trimEmptyLines()
}

fun Meal.getInfo(): String {
    var res = "식사분류 : $title\n"
    for (menu in menus) {
        res += "${menu.name} : ${menu.price}원 \n"
    }
    return res
}

fun LocalDate.toUrlDate(): UrlDate =
    UrlDate(
        day = this.dayOfMonth.toString(),
        year = this.year.toString(),
        monthMinusOne = this.minusMonths(1).monthValue.toString()
    )

fun UrlDate.toLocalDate(): LocalDate {
    return LocalDate.of(year.toInt(), Month.of(monthMinusOne.toInt().plus(1)), day.toInt())
}

fun UrlDate.getMonthDotDayString(): String {
    val monthString = if (this.getMonth().length == 2) getMonth() else "0${getMonth()}"
    val dayString = if (this.day.length == 2) day else "0${day}"
    return "${monthString}.${dayString}"
}

fun UrlDate.getMonth(): String {
    return monthMinusOne.toInt().plus(1).toString()
}

fun UrlDate.getKoreanDayOfWeek(): String {
    val localDate = this.toLocalDate()
    val dayOfWeek = localDate.dayOfWeek
    val korean = when (dayOfWeek.value) {
        1 -> "월요일"
        2 -> "화요일"
        3 -> "수요일"
        4 -> "목요일"
        5 -> "금요일"
        6 -> "토요일"
        7 -> "일요일"
        else -> throw IllegalStateException()
    }

    return "$korean"
}

// 생과대 신소재 제1생 제2생 학생식당 행원파크
fun String.compressCafeteriaName(): String {
    if (this.startsWith("생")) return "생과대"
    else if (this.startsWith("신")) return "신소재"
    else if (this.startsWith("행")) return "행원파크"
    else if (this.trim().contains("제1")) return "제1생"
    else if (this.trim().contains("제2")) return "제2생"
    else if (this.trim().contains("생식")) return "학생식당"

    return this
}

fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom,
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}


