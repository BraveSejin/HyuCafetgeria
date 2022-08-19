package com.sejin.hyucafeteria.data

import android.util.Log
import com.sejin.hyucafeteria.utilities.BASE_CAFETERIA_ID
import com.sejin.hyucafeteria.utilities.getDocument
import com.sejin.hyucafeteria.utilities.logger
import com.sejin.hyucafeteria.utilities.toUrlDate
import org.jsoup.nodes.Document
import java.time.LocalDate

class CafeteriaIdNameDatasource {
    // 먼저 아이디를 가져와야한다.

    companion object {
        private var instance: CafeteriaIdNameDatasource? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CafeteriaIdNameDatasource().also { instance = it }
            }
    }

    suspend fun getCafeteriaIdNames(): List<CafeteriaIdName> {
        val urlDate = LocalDate.now().toUrlDate()

        var doc: Document? = null
        for (i in 1 until 5) {
            doc = getDocument(BASE_CAFETERIA_ID, urlDate)
            logger("try $i")
            if (doc != null) {
                break
            }
        }
        if (doc == null) return listOf()


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
        return idNamesList
    }

    fun getActiveCafeteriaId(doc: Document): String {
        val text = doc.select("ul.nav.nav-tabs")
            .select("li.active").select("a").attr("href")
        val idIndex = text.lastIndexOf("/") + 1
        return text.slice(idIndex..text.lastIndex)
    }
}