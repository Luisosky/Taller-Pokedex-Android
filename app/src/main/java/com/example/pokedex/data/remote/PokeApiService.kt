package com.example.pokedex.data.remote

import com.example.pokedex.data.remote.dto.PokemonDto
import com.example.pokedex.data.remote.dto.PokemonListResponseDto
import com.example.pokedex.data.remote.dto.PokemonSpeciesDto
import com.example.pokedex.data.remote.dto.TypeDetailDto
import com.example.pokedex.data.remote.dto.TypeResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    /**
     * Endpoint 1: Obtiene la lista paginada de Pokémon.
     * Ejemplo: GET https://pokeapi.co/api/v2/pokemon?limit=20&offset=0
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponseDto

    /**
     * Endpoint 2: Obtiene los detalles completos de un Pokémon por nombre o ID
     * (stats, tipos, sprites, habilidades, peso, altura).
     * Ejemplo: GET https://pokeapi.co/api/v2/pokemon/charizard
     */
    @GET("pokemon/{nameOrId}")
    suspend fun getPokemonDetail(
        @Path("nameOrId") nameOrId: String
    ): PokemonDto

    /**
     * Endpoint 3a: Obtiene la lista de todos los tipos elementales de Pokémon.
     * Ejemplo: GET https://pokeapi.co/api/v2/type
     */
    @GET("type")
    suspend fun getPokemonTypes(): TypeResponseDto

    /**
     * Endpoint 3b: Obtiene todos los Pokémon correspondientes a un tipo específico.
     * Ejemplo: GET https://pokeapi.co/api/v2/type/fire
     */
    @GET("type/{nameOrId}")
    suspend fun getPokemonByType(
        @Path("nameOrId") nameOrId: String
    ): TypeDetailDto

    /**
     * Endpoint 4: Obtiene información de la especie del Pokémon
     * (lore / descripción en español, género taxonómico, hábitat, generación, rareza).
     * Ejemplo: GET https://pokeapi.co/api/v2/pokemon-species/6
     */
    @GET("pokemon-species/{nameOrId}")
    suspend fun getPokemonSpecies(
        @Path("nameOrId") nameOrId: String
    ): PokemonSpeciesDto
}
