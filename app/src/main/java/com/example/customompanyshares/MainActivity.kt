package com.example.customompanyshares

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.customompanyshares.presentation.SharedPainter
import com.example.customompanyshares.presentation.SharesState
import com.example.customompanyshares.presentation.SharesViewModel
import com.example.customompanyshares.presentation.TestSaver

class MainActivity : ComponentActivity() {

    val sharesViewModel : SharesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ObserveState()
        }


    }


    @Composable
    private fun ObserveState(){
        val screenState = sharesViewModel.state.collectAsState()

        when(val currentState = screenState.value){
            is SharesState.Initial -> {

            }
            is SharesState.Content -> {
                SharedPainter(currentState.barList)
            }
            is SharesState.Error -> {
                Log.e("MainActivity", "error = ${currentState.exception.message}", currentState.exception)
            }
        }
    }
}