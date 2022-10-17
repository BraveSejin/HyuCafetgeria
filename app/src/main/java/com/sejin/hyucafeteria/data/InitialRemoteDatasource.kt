package com.sejin.hyucafeteria.data

import com.sejin.hyucafeteria.utilities.*
import org.jsoup.nodes.Document
import java.time.LocalDate

class InitialRemoteDatasource: InitialInfoDataSource {
    // 먼저 아이디를 가져와야한다.

    companion object {
        private var instance: InitialRemoteDatasource? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: InitialRemoteDatasource().also { instance = it }
            }
    }

    override suspend fun getInitialInfo(): InitialInfo {
        val urlDate = LocalDate.now().toUrlDate()

        var doc: Document? = null
        for (i in 1 .. 2) {
            doc = getDocument(BASE_CAFETERIA_ID, urlDate)
            logger("try $i")
            if (doc != null) {
                break
            }
        }
        if (doc == null) return defaultInitialInfo


        val elements = doc.select("ul.nav.nav-tabs")
            .select("li").select("a")
        val idNamesList = mutableListOf<CafeteriaIdName>()

        for (linkElement in elements) {
            val linkText = linkElement.attr("href")
            val idIndex = linkText.lastIndexOf("/") + 1
            val id = linkText.slice(idIndex..linkText.lastIndex)
            val name = linkElement.text()
            idNamesList.add(CafeteriaIdName(id, name))
        }

        val cafeteria = parseCafeteria(doc)
        val mealList = parseMealList(doc)

        return InitialInfo(idNamesList, PageInfo(urlDate, cafeteria, mealList))
    }

    fun getActiveCafeteriaId(doc: Document): String {
        val text = doc.select("ul.nav.nav-tabs")
            .select("li.active").select("a").attr("href")
        val idIndex = text.lastIndexOf("/") + 1
        return text.slice(idIndex..text.lastIndex)
    }
}