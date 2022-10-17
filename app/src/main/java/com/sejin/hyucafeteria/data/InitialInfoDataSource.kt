package com.sejin.hyucafeteria.data

interface InitialInfoDataSource {
    suspend fun getInitialInfo(): InitialInfo
}