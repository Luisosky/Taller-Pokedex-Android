package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntryDto>,
    @SerializedName("genera")
    val genera: List<GenusDto>,
    @SerializedName("color")
    val color: NamedApiResourceDto?,
    @SerializedName("habitat")
    val habitat: NamedApiResourceDto?,
    @SerializedName("generation")
    val generation: NamedApiResourceDto?,
    @SerializedName("capture_rate")
    val captureRate: Int?,
    @SerializedName("base_happiness")
    val baseHappiness: Int?,
    @SerializedName("is_legendary")
    val isLegendary: Boolean?,
    @SerializedName("is_mythical")
    val isMythical: Boolean?
)

data class FlavorTextEntryDto(
    @SerializedName("flavor_text")
    val flavorText: String,
    @SerializedName("language")
    val language: NamedApiResourceDto,
    @SerializedName("version")
    val version: NamedApiResourceDto?
)

data class GenusDto(
    @SerializedName("genus")
    val genus: String,
    @SerializedName("language")
    val language: NamedApiResourceDto
)
