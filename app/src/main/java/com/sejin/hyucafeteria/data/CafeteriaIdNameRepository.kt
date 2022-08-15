package com.sejin.hyucafeteria.data

class CafeteriaIdNameRepository {
    private val datasource: CafeteriaIdNameDatasource = CafeteriaIdNameDatasource.getInstance()

    suspend fun getCafeteriaIdNames(): List<CafeteriaIdName> {
        return datasource.getCafeteriaIdNames()
    }

    companion object {
        @Volatile
        private var instance: CafeteriaIdNameRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CafeteriaIdNameRepository().also { instance = it }
            }
    }
}