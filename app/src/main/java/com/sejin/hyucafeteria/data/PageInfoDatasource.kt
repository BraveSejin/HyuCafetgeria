package com.sejin.hyucafeteria.data

import com.sejin.hyucafeteria.utilities.getDocument
import com.sejin.hyucafeteria.utilities.logger
import com.sejin.hyucafeteria.utilities.*
import org.jsoup.nodes.Document

class PageInfoDatasource {

    suspend fun getPageInfo(cafeteriaId: String, urlDate: UrlDate): PageInfo {
        var doc: Document? = null
        for (i in 1 .. 2) {
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



    companion object {
        private var instance: PageInfoDatasource? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: PageInfoDatasource().also { instance = it }
            }
    }
}