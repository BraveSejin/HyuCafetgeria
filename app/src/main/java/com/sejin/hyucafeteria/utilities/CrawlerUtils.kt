package com.sejin.hyucafeteria.utilities

import com.sejin.hyucafeteria.data.Cafeteria
import com.sejin.hyucafeteria.data.Meal
import com.sejin.hyucafeteria.data.Menu
import com.sejin.hyucafeteria.data.UrlDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDate

const val baseUrl = "https://www.hanyang.ac.kr/web/www/"
const val BASE_CAFETERIA_ID = "re1"

private fun generateUrl(cafeteriaId: String, urlDate: UrlDate): String {
    return baseUrl +
            "$cafeteriaId?p_p_id=foodView_WAR_foodportlet" +
            "&p_p_lifecycle=0&p_p_state=normal" +
            "&p_p_mode=view&p_p_col_id=column-1" +
            "&p_p_col_pos=1&p_p_col_count=2" +
            "&_foodView_WAR_foodportlet_sFoodDateDay=${urlDate.day}" +
            "&_foodView_WAR_foodportlet_sFoodDateYear=${urlDate.year}" +
            "&_foodView_WAR_foodportlet_action=view&" +
            "_foodView_WAR_foodportlet_sFoodDateMonth=${urlDate.monthMinusOne}"
}

// 현재 페이지의 식당 id를 가져옵니다.


suspend fun getDocument(
    cafeteriaId: String,
    urlDate: UrlDate = LocalDate.now().toUrlDate()
): Document {
    val url = generateUrl(cafeteriaId, urlDate)
    val doc = CoroutineScope(Dispatchers.IO).async {
        val res = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .ignoreHttpErrors(true)
            .referrer("http://www.google.com")
            .execute()
        Jsoup.parse(res.body())
    }
    return doc.await()
}



private fun createMealList(mealElements: List<Element>): List<Meal> {
    val result = mutableListOf<Meal>()
    for (mealElement in mealElements) {
        val mealTitle = mealElement.select("h4.d-title2").text()
        val menuElements = mealElement.select("ul.thumbnails").select("li.span3")
        val menuList = createMenuList(menuElements)
        val meal = Meal(mealTitle, menuList)
        result.add(meal)
    }
    return result
}

private fun createMenuList(menuElements: List<Element>): List<Menu> {
    val result = mutableListOf<Menu>()
    for (menuElement in menuElements) {
        val menuName = menuElement.select("h3").text()
        val image = menuElement.select("img").attr("src")
        val price = menuElement.select("p.price").text()
        result.add(Menu(menuName, image, price))
    }
    return result
}