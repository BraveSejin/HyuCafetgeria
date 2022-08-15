package com.sejin.hyucafeteria.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class PageInfoRepository {

    private val datasource = PageInfoDatasource()

    suspend fun getPageInfo(id: String, urlDate: UrlDate): PageInfo{
        return datasource.getPageInfo(id, urlDate)
    }

    companion object {
        @Volatile
        private var instance: PageInfoRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: PageInfoRepository().also { instance = it }
            }
    }
}