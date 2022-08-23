package com.sejin.hyucafeteria.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sejin.hyucafeteria.utilities.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel : ViewModel() {
    private val _isNetworkError = SingleLiveEvent<String>()
    val isNetWorkError: LiveData<String> = _isNetworkError
    protected val coroutineNetworkExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            _isNetworkError.value = "네트워크 오류가 발생했습니다."
        }
}