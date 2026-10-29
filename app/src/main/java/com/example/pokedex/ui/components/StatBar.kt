package com.example.pokedex.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.domain.model.PokemonStat

@Composable
fun StatBar(
    stat: PokemonStat,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val progressAnimation by animateFloatAsState(
        targetValue = if (animationPlayed) stat.progress else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "StatProgressAnimation"
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stat Title (e.g., "Ataque", "PS")
        Text(
            text = stat.spanishName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
            modifier = Modifier.width(85.dp)
        )

        // Stat Number (e.g., 078)
        Text(
            text = "%03d".format(stat.value),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.width(40.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Progress Bar
        val statColor = when {
            stat.value >= 100 -> Color(0xFF4CAF50)
            stat.value >= 65 -> accentColor
            else -> Color(0xFFFF7043)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progressAnimation)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(statColor)
            )
        }
    }
}
