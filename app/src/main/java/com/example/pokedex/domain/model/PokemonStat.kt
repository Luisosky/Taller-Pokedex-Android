package com.example.pokedex.domain.model

data class PokemonStat(
    val name: String,
    val spanishName: String,
    val value: Int,
    val maxValue: Int = 255
) {
    val progress: Float
        get() = (value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f)
}
