package com.example.customompanyshares.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import com.example.customompanyshares.data.Bar
import kotlin.math.roundToInt

const val MIN_COUNT_BARS = 10

@Composable
fun SharedPainter(bars: List<Bar>){

    var terminalState by rememberTerminalState(bars)

    val transformState = TransformableState { zoomChange, panChange, rotationChange ->
        val visibilityBarsCount =
            (terminalState.visibilityBarsCount / zoomChange).roundToInt()
                .coerceIn(MIN_COUNT_BARS, bars.size)

        val scrolledBy = (terminalState.scrolledBy + panChange.x)
            .coerceAtLeast(0f)
            .coerceAtMost(bars.size * terminalState.barWidth - terminalState.terminalWidth)


        terminalState = terminalState.copy(
            visibilityBarsCount = visibilityBarsCount,
            scrolledBy = scrolledBy
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformState)
    ) {
        terminalState = terminalState.copy(terminalWidth = size.width)

        val max = terminalState.visibleBars.maxOf { it.high }
        val min = terminalState.visibleBars.minOf { it.low }

        val barHeight = size.height / (max - min)

        translate(left = terminalState.scrolledBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - terminalState.barWidth * index

                drawLine(
                    color = bar.getColor(),
                    start = Offset(offsetX, size.height - ((bar.low - min) * barHeight)),
                    end = Offset(offsetX, size.height - ((bar.high - min) * barHeight)),
                    strokeWidth = terminalState.barWidth / 2
                )
            }
        }
    }
}

fun Bar.getColor(): Color {
    return if (this.open < this.close) {
        Color.Green
    } else {
        Color.Red
    }
}