package com.example.pokedex.domain.repository

import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.model.PokemonListItem
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    /**
     * Paginación de Pokémon (Endpoint 1: /pokemon)
     */
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<List<PokemonListItem>>

    /**
     * Detalle completo del Pokémon combinando Endpoint 2 (/pokemon/{id}) y Endpoint 4 (/pokemon-species/{id})
     */
    suspend fun getPokemonDetail(nameOrId: String): Resource<PokemonDetail>

    /**
     * Lista de tipos elementales (Endpoint 3a: /type)
     */
    suspend fun getPokemonTypes(): Resource<List<String>>

    /**
     * Filtro por tipo (Endpoint 3b: /type/{name})
     */
    suspend fun getPokemonByType(type: String): Resource<List<PokemonListItem>>

    /**
     * Búsqueda por nombre o número (Endpoint 2: /pokemon/{query})
     */
    suspend fun searchPokemon(query: String): Resource<PokemonListItem>
}
