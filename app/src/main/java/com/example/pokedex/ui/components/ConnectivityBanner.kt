package com.example.pokedex.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.ui.theme.OfflineRed
import com.example.pokedex.ui.theme.OnlineGreen
import kotlinx.coroutines.delay

@Composable
fun ConnectivityBanner(
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    var showRestoredBanner by remember { mutableStateOf(false) }
    var wasOffline by remember { mutableStateOf(false) }

    LaunchedEffect(isOnline) {
        if (!isOnline) {
            wasOffline = true
            showRestoredBanner = false
        } else if (wasOffline) {
            showRestoredBanner = true
            delay(3000)
            showRestoredBanner = false
            wasOffline = false
        }
    }

    AnimatedVisibility(
        visible = !isOnline || showRestoredBanner,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
    ) {
        val backgroundColor = if (isOnline) OnlineGreen else OfflineRed
        val icon = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff
        val text = if (isOnline) "Conexión a internet restablecida" else "Sin conexión a internet (Modo offline)"

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
