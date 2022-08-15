package com.sejin.hyucafeteria.data

import com.sejin.hyucafeteria.utilities.trimEmptyLines
import java.time.LocalDate

class PageInfo(
    val urlDate: UrlDate,
    val cafeteria: Cafeteria,
    val mealList: List<Meal>
) {

}

data class UrlDate(val day: String, val year: String, val monthMinusOne: String)

data class Cafeteria(
    val id: String,
    val name: String,
    val location: String,
    val notice: String,
)

data class Meal(val title: String, val menus: List<Menu>)

data class Menu(val name: String, val url: String, val price: String)
