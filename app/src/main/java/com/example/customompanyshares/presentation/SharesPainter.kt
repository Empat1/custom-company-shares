package com.example.customompanyshares.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import com.example.customompanyshares.data.Bar
import kotlin.math.roundToInt

const val MIN_COUNT_BARS = 10

@Composable
fun SharedPainter(bars: List<Bar>){

    var visibilityBarsCount by remember {
        mutableIntStateOf(25)
    }

    var scrollBy by remember {
        mutableFloatStateOf(0f)
    }

    var terminalWidth by remember {
        mutableFloatStateOf(0f)
    }

    val barWidth by remember {
        derivedStateOf {
            terminalWidth / visibilityBarsCount
        }
    }

    val visibilityBar by remember {
        derivedStateOf {
            val startIndex = (scrollBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibilityBarsCount).coerceAtMost(bars.size)

            bars.subList(startIndex, endIndex)
        }
    }

    val transformState = TransformableState{ zoomChange, panChange, rotationChange ->
        visibilityBarsCount = (visibilityBarsCount / zoomChange).roundToInt()
            .coerceIn(MIN_COUNT_BARS, bars.size)

        scrollBy = (scrollBy + panChange.x)
            .coerceAtLeast(0f)
            .coerceAtMost(bars.size * barWidth - terminalWidth)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformState)
    ){



        terminalWidth = size.width
        val max = visibilityBar.maxOf { it.high }
        val min = visibilityBar.minOf { it.low }

        val barHeight = size.height / (max - min)

        translate(left = scrollBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - barWidth * index

                drawLine(
                    color = bar.getColor(),
                    start = Offset(offsetX, size.height - ((bar.low - min) * barHeight)),
                    end = Offset(offsetX, size.height - ((bar.high - min) * barHeight)),
                    strokeWidth = barWidth / 2
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