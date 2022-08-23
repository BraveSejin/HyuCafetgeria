package com.sejin.hyucafeteria.utilities

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.sejin.hyucafeteria.data.*
import java.time.LocalDate
import java.time.Month

fun String.trimEmptyLines(): String {
    val list = this.split("\n").filter { it.trim().isNotEmpty() }
    return list.joinToString("\n")
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

// 생과대 신소재 제1생 제2생 학생식당 행원파크
fun String.compressCafeteriaName(): String {
    if (this.startsWith("생") || this == "교직원식당") return "생과대"
    else if (this.startsWith("신") || this == "신교직원식당") return "신소재"
    else if (this.startsWith("행")) return "행원파크"
    else if (this.trim().contains("제1")) return "제1생"
    else if (this.trim().contains("제2")) return "제2생"
    else if (this.trim().contains("생식")) return "학생식당"

    return this
}

fun String.commaToNewLine(): String {
    return this.replace(',', '\n')
}

fun Cafeteria.refine(): Cafeteria {
    return Cafeteria(id, name.compressCafeteriaName(), location, notice)
}

fun Cafeteria.getInfo(): String {
    var res = "'"
    res += "식당 id: $id\n"
    res += "식당 이름: $name\n"
    res += "식당 위치: $location\n"
    res += "식당 운영시간: ${notice}\n"
    return res.trimEmptyLines()
}

//fun PageInfo.refine(): PageInfo {
////    return this.apply { cafeteria.name = cafeteria.name.compressCafeteriaName() }
//}

fun Menu.isSquareBracketMenu(): Boolean {
    val trimmed = name.trimStart()
    if (trimmed.first() != '[') return false
    val endBracketIndex = trimmed.indexOf(']')
    return endBracketIndex != -1
}

fun Menu.getSquareBracketMenuName(): Pair<String, String>{

    val bracketEndIndex = name.indexOf(']')
    val bracketPart = name.take(bracketEndIndex.plus(1))
    val remainPart = name.drop(bracketEndIndex.plus(1)).trim()
    return Pair(bracketPart,remainPart)
//    val parenthesisEndIndex = name.indexOf(')')
//    return name.take(parenthesisEndIndex + 1)
}

fun Menu.getMenuNameBeforeParenthesisStart(): String{
    val parenthesisStartIndex = name.indexOf('(')
    return name.take(parenthesisStartIndex )
}

fun Menu.getMenuNameUntilParenthesisEnd(): String{
    val parenthesisEndIndex = name.indexOf(')')
    return name.take(parenthesisEndIndex + 1)
}

fun Menu.getMenuNameWithoutEngWords(): String {
    return name.split(" ")
        .filter { str -> !str.first().isAlphabet() }
        .joinToString(" ")
}

fun Meal.getInfo(): String {
    var res = "식사분류 : $title\n"
    for (menu in menus) {
        res += "${menu.name} : ${menu.price}원 \n"
    }
    return res
}

// 괄호 뒤에 영어가 나오는 경우 식별
fun String.reduceEngMenu(): String {
    if (!isEngMenuName()) return this
    val closeIdx = indexOf(')')
    return take(closeIdx + 1)
}

fun String.isEngMenuName(): Boolean {

    if (!contains('(') or !contains(')')) return false

    val replaced = this.replace("[\n\r' ']".toRegex(), "")
    val parenthesesEndIdx = replaced.indexOf(')')
    if (parenthesesEndIdx + 1 < replaced.length - 1 && replaced[parenthesesEndIdx + 1].isAlphabet())
        return true
    return false
}

fun Char.isAlphabet(): Boolean = (this in 'a'..'z' || this in 'A'..'Z')

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


