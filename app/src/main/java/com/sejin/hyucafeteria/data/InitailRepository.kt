package com.sejin.hyucafeteria.data

class InitailRepository {
    private val datasource: InitialDatasource = InitialDatasource.getInstance()

    suspend fun getInitialInfo(): InitialInfo {
        return datasource.getInitialInfo()
    }

    companion object {
        @Volatile
        private var instance: InitailRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: InitailRepository().also { instance = it }
            }
    }
}