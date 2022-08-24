package com.sejin.hyucafeteria

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sejin.hyucafeteria.base.BaseViewModel
import com.sejin.hyucafeteria.data.*
import com.sejin.hyucafeteria.utilities.SingleLiveEvent
import com.sejin.hyucafeteria.utilities.toLocalDate
import com.sejin.hyucafeteria.utilities.toUrlDate
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel() : BaseViewModel() {

    private val initialRepository: InitailRepository = InitailRepository.getInstance()
    private val pageInfoRepository: PageInfoRepository = PageInfoRepository.getInstance()

    private val _idNameList = MutableLiveData<List<CafeteriaIdName>>()
    val idNameList = _idNameList

    private val _pageInfoList = MutableLiveData<List<PageInfo>>()
    val pageInfoList = _pageInfoList

    private val _currentCafeteriaIdName = MutableLiveData<CafeteriaIdName>()
    val currentCafeteriaIdName = _currentCafeteriaIdName

    var currentCafeteriaIndex = -1

    private val _currentPageInfo = MutableLiveData<PageInfo>()
    val currentPageInfo = _currentPageInfo

    private val _currentDate = MutableLiveData<UrlDate>(LocalDate.now().toUrlDate())
    val currentDate = _currentDate

    private val _repeated404ErrorEvent = SingleLiveEvent<String>()
    val repeated404ErrorEvent = _repeated404ErrorEvent

    init {
        viewModelScope.launch(coroutineNetworkExceptionHandler) {
            getInitialInfo()
        }
    }

    fun updateDateToBefore() {
        val temp = currentDate.value?.toLocalDate()!!.minusDays(1).toUrlDate()
        _currentDate.value = temp
        updateCurrentPageInfo()
    }

    fun updateDateToNext() {
        val temp = currentDate.value?.toLocalDate()!!.plusDays(1).toUrlDate()
        _currentDate.value = temp
        updateCurrentPageInfo()
    }


    fun updateCurrentPageInfo() {
        if (_currentCafeteriaIdName.value == null) return

        viewModelScope.launch(coroutineNetworkExceptionHandler) {
            val pageInfo = pageInfoRepository.getPageInfo(
                _currentCafeteriaIdName.value!!.id,
                _currentDate.value!!
            )
            if (pageInfo == defaultPageInfo) {
                _repeated404ErrorEvent.value = "정보를 받아오지 못했어요. 안전을 위해 앱을 종료합니다. 곧 업데이트 할게요!"
            }
            _currentPageInfo.value = pageInfo
        }
    }

    private suspend fun getInitialInfo() {
        val initialInfo = initialRepository.getInitialInfo()
        if (initialInfo == defaultInitialInfo) {
            _repeated404ErrorEvent.value = "정보를 받아오지 못했어요. 안전을 위해 앱을 종료합니다. 곧 업데이트 할게요!!"
        }
        _idNameList.value = initialInfo.idNameList
        _currentPageInfo.value = initialInfo.pageInfo
    }
}