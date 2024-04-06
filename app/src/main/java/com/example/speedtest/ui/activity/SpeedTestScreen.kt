package com.example.speedtest.ui.activity

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speedtest.state.UiState
import com.example.speedtest.ui.view.NavigationView
import com.example.speedtest.ui.theme.ComposeSpeedTestTheme
import com.example.speedtest.ui.theme.DarkGradient
import com.example.speedtest.ui.theme.LightColor
import com.example.speedtest.ui.view.CircularSpeedIndicator
import com.example.speedtest.ui.view.CustomHeader
import com.example.speedtest.ui.view.SpeedIndicator
import com.example.speedtest.ui.view.VerticalDivider
import com.example.speedtest.ui.view.startAnimation
import com.example.speedtest.ui.view.toUiState
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.max

//all ui components is customizable
//screens of speed
@Composable
fun SpeedTestScreen() {
    val coroutineScope = rememberCoroutineScope()

    val animation = remember { Animatable(0f) }
    val maxSpeed = remember { mutableStateOf(0f) }
    maxSpeed.value = max(maxSpeed.value, animation.value * 100f)

    SpeedTestScreen(state = animation.toUiState(maxSpeed.value)) {
        coroutineScope.launch {
            maxSpeed.value = 0f
            startAnimation(animation)
        }
    }
}


@Composable
private fun SpeedTestScreen(state: UiState, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CustomHeader()
        SpeedIndicator(state = state, onClick = onClick)
        AdditionalInfo(state.ping, state.maxSpeed)
        NavigationView()
    }
}

@Composable
fun StartButton(isEnabled: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 24.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.onSurface)

    ) {
        Text(text = "START", modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp))
    }
}

@Composable
fun AdditionalInfo(ping: String, maxSpeed: String) {
    @Composable
    fun RowScope.InfoColumn(title: String, value: String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = title)
            Text(
                text = value,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 8.dp)
            )

        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        InfoColumn(title = "PING", value = ping)
        VerticalDivider()
        InfoColumn(title = "MAX SPEED", value = maxSpeed)
    }
}

//preview of composable views
@Preview(showBackground = true)
@Composable
fun SpeedTestScreenPreview() {
    ComposeSpeedTestTheme {
        Surface {
            SpeedTestScreen(
                UiState(
                    speed = "120.5",
                    ping = "5 ms",
                    maxSpeed = "150.0 mbps",
                    arcValue = 0.25f
                )
            ) {

            }
        }
    }
}
