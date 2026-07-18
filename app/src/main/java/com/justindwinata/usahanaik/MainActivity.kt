package com.justindwinata.usahanaik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.justindwinata.usahanaik.ui.theme.UsahaNaikTheme

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
    UsahaNaikTheme {
        Surface {
            Text(text = "UsahaNaik")
        }
    }
}

@Preview
@Composable
private fun UsahaNaikAppEntryPreview() {
    UsahaNaikAppEntry()
}
