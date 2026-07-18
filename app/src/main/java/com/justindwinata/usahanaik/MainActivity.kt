package com.justindwinata.usahanaik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.justindwinata.usahanaik.ui.UsahaNaikApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UsahaNaikAppEntry()
        }
    }
}

@Composable
private fun UsahaNaikAppEntry() {
    UsahaNaikApp()
}

@Preview
@Composable
private fun UsahaNaikAppEntryPreview() {
    UsahaNaikAppEntry()
}
