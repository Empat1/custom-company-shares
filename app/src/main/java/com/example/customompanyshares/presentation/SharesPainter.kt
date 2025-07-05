package com.example.customompanyshares.presentation

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
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

    var scrollState by remember {
        mutableIntStateOf(100)
    }

    val transformState = TransformableState{ zoomChange, panChange, rotationChange ->
        visibilityBarsCount = (visibilityBarsCount / zoomChange).roundToInt()
            .coerceIn(MIN_COUNT_BARS, bars.size)

        scrollState = panChange.x.roundToInt()
    }


    val takeBars = bars.take(visibilityBarsCount)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformState)
    ){

        val max = takeBars.maxOf { it.high }
        val min = takeBars.minOf { it.low }

        val barWidth = size.width / takeBars.size
        val barHeight = size.height / (max - min)

        takeBars.forEachIndexed { index, bar ->
            val offsetX = barWidth * index

            drawLine(
                color = bar.getColor(),
                start = Offset(offsetX,size.height - ((bar.low - min) * barHeight)),
                end = Offset(offsetX, size.height - ((bar.high - min) * barHeight)),
                strokeWidth = barWidth / 2
            )
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