package com.example.pokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokedex.ui.detail.PokemonDetailScreen
import com.example.pokedex.ui.list.PokemonListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        modifier = modifier
    ) {
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                }
            )
        }

        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument("pokemonId") {
                    type = NavType.IntType
                }
            )
        ) {
            PokemonDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
