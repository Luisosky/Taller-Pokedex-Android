package com.example.pokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.util.PokemonTypeUtils

@Composable
fun TypeBadge(
    type: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 11.sp
) {
    val typeColor = PokemonTypeUtils.getTypeColor(type)
    val spanishName = PokemonTypeUtils.getTypeNameInSpanish(type)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(typeColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = spanishName,
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
