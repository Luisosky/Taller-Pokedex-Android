package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TypeResponseDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("results")
    val results: List<NamedApiResourceDto>
)

data class TypeDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon")
    val pokemon: List<TypePokemonEntryDto>
)

data class TypePokemonEntryDto(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("pokemon")
    val pokemon: NamedApiResourceDto
)
