package com.example.customompanyshares.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.customompanyshares.data.Bar
import kotlin.math.roundToInt

data class TerminalState(
    val bars: List<Bar>,
    var visibilityBarsCount: Int = 100,
    var terminalWidth: Float = 0f,
    var scrolledBy: Float = 0f
){

    val barWidth: Float
        get() = terminalWidth / visibilityBarsCount

    val visibleBars : List<Bar>
        get() {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibilityBarsCount).coerceAtMost(bars.size)

            return bars.subList(startIndex, endIndex)
        }

    companion object{
        val saver: Saver<MutableState<TerminalState>, Any> = listSaver(
            save = {
                val bars = it.value.bars
                val visibilityBarsCount = it.value.visibilityBarsCount
                val terminalWidth = it.value.terminalWidth
                val scrolledBy = it.value.scrolledBy

                listOf(bars, visibilityBarsCount, terminalWidth, scrolledBy)
            },
            restore = {
                mutableStateOf(
                    TerminalState(
                        it[0] as List<Bar>,
                        it[1] as Int,
                        it[2] as Float,
                        it[3] as Float
                    )
                )
            }
        )
    }
}

@Composable
fun rememberTerminalState(bars: List<Bar>): MutableState<TerminalState> {
    return rememberSaveable(saver = TerminalState.saver) {
        mutableStateOf(TerminalState(bars))
    }
}
