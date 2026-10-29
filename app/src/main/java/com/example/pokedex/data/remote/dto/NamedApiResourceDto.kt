package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NamedApiResourceDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) {
    /**
     * Extracts the numeric ID from the PokeAPI URL.
     * Example: "https://pokeapi.co/api/v2/pokemon/25/" -> 25
     */
    fun extractId(): Int {
        return url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
    }
}
