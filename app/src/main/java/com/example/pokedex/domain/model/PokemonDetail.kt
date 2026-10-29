package com.example.pokedex.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val heightMeters: Float,
    val weightKg: Float,
    val baseExperience: Int?,
    val types: List<String>,
    val stats: List<PokemonStat>,
    val abilities: List<PokemonAbility>,
    val description: String,
    val category: String,
    val color: String,
    val habitat: String?,
    val generation: String?,
    val captureRate: Int?,
    val baseHappiness: Int?,
    val isLegendary: Boolean,
    val isMythical: Boolean
) {
    val formattedId: String
        get() = "#%03d".format(id)

    val capitalizedName: String
        get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    val primaryType: String
        get() = types.firstOrNull() ?: "normal"

    val totalStats: Int
        get() = stats.sumOf { it.value }
}

data class PokemonAbility(
    val name: String,
    val isHidden: Boolean
) {
    val formattedName: String
        get() = name.replace("-", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}
