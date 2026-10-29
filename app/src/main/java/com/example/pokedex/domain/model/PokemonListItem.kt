package com.example.pokedex.domain.model

import com.example.pokedex.util.Constants

data class PokemonListItem(
    val id: Int,
    val name: String,
    val imageUrl: String = "${Constants.OFFICIAL_ARTWORK_BASE_URL}$id.png",
    val primaryType: String? = null
) {
    val formattedId: String
        get() = "#%03d".format(id)

    val capitalizedName: String
        get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
