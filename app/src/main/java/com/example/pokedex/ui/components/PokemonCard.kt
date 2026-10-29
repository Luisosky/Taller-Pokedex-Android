package com.example.pokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pokedex.domain.model.PokemonListItem
import com.example.pokedex.util.PokemonTypeUtils

@Composable
fun PokemonCard(
    pokemon: PokemonListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val baseColor = pokemon.primaryType?.let {
        PokemonTypeUtils.getTypeColor(it)
    } ?: MaterialTheme.colorScheme.primary

    val cardBrush = Brush.verticalGradient(
        colors = listOf(
            baseColor.copy(alpha = 0.18f),
            baseColor.copy(alpha = 0.06f),
            MaterialTheme.colorScheme.surface
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBrush)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header: Number and optional Type badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pokemon.formattedId,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                    pokemon.primaryType?.let { type ->
                        TypeBadge(
                            type = type,
                            fontSize = 9.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Official Artwork Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemon.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemon.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(24.dp),
                                    color = baseColor
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Pokemon Name
                Text(
                    text = pokemon.capitalizedName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
