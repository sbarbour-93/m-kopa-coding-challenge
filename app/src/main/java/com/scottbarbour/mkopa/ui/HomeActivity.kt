package com.scottbarbour.mkopa.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MKopaApp()
            }
        }
    }
}

@Composable
fun MKopaApp() {
    DeviceLockTimerScreen()
}

@Preview(showBackground = true)
@Composable
fun MKyAppPreview() {
    MKopaApp()
}