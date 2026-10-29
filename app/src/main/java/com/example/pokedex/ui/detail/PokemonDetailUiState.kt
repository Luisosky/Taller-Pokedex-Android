package com.example.pokedex.ui.detail

import com.example.pokedex.domain.model.PokemonDetail

data class PokemonDetailUiState(
    val isLoading: Boolean = true,
    val pokemonDetail: PokemonDetail? = null,
    val errorMessage: String? = null
)
