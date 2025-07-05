package com.example.customompanyshares.presentation

import com.example.customompanyshares.data.Bar

sealed class SharesState {

    data object Initial : SharesState()

    data class Content(val barList: List<Bar>) : SharesState()

    data class Error(val exception: Exception) : SharesState()
}