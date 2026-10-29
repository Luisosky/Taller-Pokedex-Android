package com.example.pokedex.ui.navigation

sealed class Screen(val route: String) {
    data object PokemonList : Screen("pokemon_list")
    data object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: Int): String = "pokemon_detail/$pokemonId"
    }
}
