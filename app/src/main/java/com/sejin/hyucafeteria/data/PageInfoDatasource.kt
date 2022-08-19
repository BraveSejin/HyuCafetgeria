package com.sejin.hyucafeteria.data

import android.util.Log
import com.sejin.hyucafeteria.utilities.getDocument
import com.sejin.hyucafeteria.utilities.logger
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PageInfoDatasource {

    suspend fun getPageInfo(cafeteriaId: String, urlDate: UrlDate): PageInfo {
        var doc: Document? = null
        for (i in 1 until 5) {
            doc = getDocument(cafeteriaId, urlDate)
            logger("try $i")
            if (doc != null) {
                break
            }
        }
        if (doc == null) return defaultPageInfo

        val cafeteria = parseCafeteria(doc)
        val mealList = parseMealList(doc)
        return PageInfo(urlDate, cafeteria, mealList)
    }

    private fun parseCafeteria(doc: Document): Cafeteria {
        val id = getActiveCafeteriaId(doc)
        return parseCafeteriaWithId(id, doc)
    }

    private fun parseCafeteriaWithId(id: String, doc: Document): Cafeteria {
        val elements = doc.select("div.tab-pane.active")
        val cafeteriaElement = elements[0]
        val cafeteriaTableBody = cafeteriaElement.select("td")
        val cafeteriaName = cafeteriaTableBody[0].text()
        val location = cafeteriaTableBody[1].text()
        val timeData = cafeteriaTableBody[2].text()
        return Cafeteria(id, cafeteriaName, location, timeData)
    }

    private fun getActiveCafeteriaId(doc: Document): String {
        val text = doc.select("ul.nav.nav-tabs")
            .select("li.active").select("a").attr("href")
        val idIndex = text.lastIndexOf("/") + 1
        return text.slice(idIndex..text.lastIndex)
    }

    private fun parseMealList(doc: Document): List<Meal> {
        val mealElements = doc.select("div.tab-pane.active")[1].select("div.in-box")
        return createMealList(mealElements)
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

    companion object {
        private var instance: PageInfoDatasource? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: PageInfoDatasource().also { instance = it }
            }
    }
}