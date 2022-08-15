package com.sejin.hyucafeteria

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sejin.hyucafeteria.data.*
import com.sejin.hyucafeteria.utilities.toLocalDate
import com.sejin.hyucafeteria.utilities.toUrlDate
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel() : ViewModel() {

    private val idRepository: CafeteriaIdNameRepository = CafeteriaIdNameRepository.getInstance()
    private val pageInfoRepository: PageInfoRepository = PageInfoRepository.getInstance()

    private val _cafeteriaIdNames = MutableLiveData<List<CafeteriaIdName>>()
    val cafeteriaIdNames = _cafeteriaIdNames

    private val _pageInfoList = MutableLiveData<List<PageInfo>>()
    val pageInfoList = _pageInfoList

    private val _currentCafeteriaIdName = MutableLiveData<CafeteriaIdName>()
    val currentCafeteriaIdName = _currentCafeteriaIdName

    private val _currentPageInfo = MutableLiveData<PageInfo>()
    val currentPageInfo = _currentPageInfo

    private val _currentDate = MutableLiveData<UrlDate>(LocalDate.now().toUrlDate())
    val currentDate = _currentDate

    init {
        viewModelScope.launch {
            updateCafeteriaIdNames()
        }
    }

    fun updateDateToBefore() {
        val temp = currentDate.value?.toLocalDate()!!.minusDays(1).toUrlDate()
        _currentDate.value = temp
        setCurrentPageInfo()
    }

    fun updateDateToNext() {
        val temp = currentDate.value?.toLocalDate()!!.plusDays(1).toUrlDate()
        _currentDate.value = temp
        setCurrentPageInfo()
    }

    private suspend fun updateCafeteriaIdNames() {
        val idNames = idRepository.getCafeteriaIdNames()
        _cafeteriaIdNames.value = idNames
    }

    fun setCurrentPageInfo() {
        viewModelScope.launch {
            _currentPageInfo.value =
                pageInfoRepository.getPageInfo(
                    _currentCafeteriaIdName.value!!.id,
                    _currentDate.value!!
                )
        }
    }
}