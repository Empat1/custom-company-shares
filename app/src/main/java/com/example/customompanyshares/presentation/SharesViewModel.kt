package com.example.customompanyshares.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customompanyshares.data.ApiFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharesViewModel : ViewModel() {

    private val apiService = ApiFactory.apiService

    private val _state = MutableStateFlow<SharesState>(SharesState.Initial)
    val state: StateFlow<SharesState>
        get() = _state

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch() {
            try {
                val result = apiService.loadBars()
                _state.value = SharesState.Content(result.barList)
            }catch (e: Exception){
                _state.value = SharesState.Error(e)
            }
        }
    }
}