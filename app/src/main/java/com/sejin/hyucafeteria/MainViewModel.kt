package com.sejin.hyucafeteria

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sejin.hyucafeteria.data.*
import com.sejin.hyucafeteria.utilities.SingleLiveEvent
import com.sejin.hyucafeteria.utilities.toLocalDate
import com.sejin.hyucafeteria.utilities.toUrlDate
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

    private val _errorEvent = SingleLiveEvent<String>()
    val errorEvent = _errorEvent

    init {
        viewModelScope.launch {
            initCafeteriaIdNames()
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

    fun setCurrentCafeteriaIdNameAndCurrentDate(
        cafeteriaIdName: CafeteriaIdName,
        urlDate: UrlDate
    ) {
        _currentCafeteriaIdName.value = cafeteriaIdName
        _currentDate.value = urlDate
    }

    fun setCurrentPageInfo() {
        if (_currentCafeteriaIdName.value == null) return

        viewModelScope.launch {
            val pageInfo = pageInfoRepository.getPageInfo(
                _currentCafeteriaIdName.value!!.id,
                _currentDate.value!!
            )
            if (pageInfo == defaultPageInfo) {
                _errorEvent.value = "정보를 받아오지 못했어요. 안전을 위해 앱을 종료합니다. 곧 업데이트 할게요!"
            }
            _currentPageInfo.value = pageInfo
        }
    }

    private fun callEvent(msg: String) {
    }

    private suspend fun initCafeteriaIdNames() {
        val idNames = idRepository.getCafeteriaIdNames()
        if (idNames.isEmpty()) {
            _errorEvent.value = "정보를 받아오지 못했어요. 안전을 위해 앱을 종료합니다. 곧 업데이트 할게요!!"
        }
        _cafeteriaIdNames.value = idNames
    }
}