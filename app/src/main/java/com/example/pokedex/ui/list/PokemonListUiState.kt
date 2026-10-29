package com.example.pokedex.ui.list

import com.example.pokedex.domain.model.PokemonListItem

data class PokemonListUiState(
    val pokemonList: List<PokemonListItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedType: String? = null,
    val availableTypes: List<String> = emptyList(),
    val isEndReached: Boolean = false,
    val isOnline: Boolean = true
)
