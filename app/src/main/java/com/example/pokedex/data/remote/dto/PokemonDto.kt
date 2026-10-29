package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: Int, // in decimeters
    @SerializedName("weight")
    val weight: Int, // in hectograms
    @SerializedName("base_experience")
    val baseExperience: Int?,
    @SerializedName("sprites")
    val sprites: SpritesDto,
    @SerializedName("types")
    val types: List<PokemonTypeSlotDto>,
    @SerializedName("stats")
    val stats: List<PokemonStatSlotDto>,
    @SerializedName("abilities")
    val abilities: List<PokemonAbilitySlotDto>
)

data class SpritesDto(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("other")
    val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class PokemonTypeSlotDto(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: NamedApiResourceDto
)

data class PokemonStatSlotDto(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("stat")
    val stat: NamedApiResourceDto
)

data class PokemonAbilitySlotDto(
    @SerializedName("is_hidden")
    val isHidden: Boolean,
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("ability")
    val ability: NamedApiResourceDto
)
