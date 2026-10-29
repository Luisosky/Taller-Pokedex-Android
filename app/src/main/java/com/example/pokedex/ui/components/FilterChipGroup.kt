package com.example.pokedex.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.util.PokemonTypeUtils

@Composable
fun FilterChipGroup(
    types: List<String>,
    selectedType: String?,
    onTypeSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "Todos" Chip
        val isAllSelected = selectedType == null
        FilterChip(
            selected = isAllSelected,
            onClick = { onTypeSelected(null) },
            label = {
                Text(
                    text = "Todos",
                    fontSize = 12.sp,
                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White,
                containerColor = MaterialTheme.colorScheme.surface,
                labelColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // Type specific chips
        types.forEach { type ->
            val isSelected = selectedType.equals(type, ignoreCase = true)
            val typeColor = PokemonTypeUtils.getTypeColor(type)
            val spanishName = PokemonTypeUtils.getTypeNameInSpanish(type)

            FilterChip(
                selected = isSelected,
                onClick = { onTypeSelected(if (isSelected) null else type) },
                label = {
                    Text(
                        text = spanishName,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = typeColor,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
