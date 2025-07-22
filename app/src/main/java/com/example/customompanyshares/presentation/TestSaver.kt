package com.example.customompanyshares.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TestSaver() {
    var textData by rememberSaveable(saver = TextData.saver) {
        mutableStateOf(TextData(0, "text"))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { textData = textData.copy(number = textData.number + 1) }
        ,
        contentAlignment = Alignment.Center,
    ){
        Text(text = "Text: $textData")
    }
}

data class TextData(val number: Int, val text: String) {

    companion object {

        val saver: Saver<MutableState<TextData>, Any> = listSaver(
            save = {
                val number = it.value.number
                val text = it.value.text

                listOf(number, text)
            },
            restore = {
                mutableStateOf(
                    TextData(it[0] as Int, it[1] as String)
                )
            }
        )
    }
}