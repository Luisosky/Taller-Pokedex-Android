package com.example.pokedex.data.repository

import com.example.pokedex.data.remote.PokeApiService
import com.example.pokedex.data.remote.dto.PokemonDto
import com.example.pokedex.data.remote.dto.PokemonSpeciesDto
import com.example.pokedex.domain.model.PokemonAbility
import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.model.PokemonListItem
import com.example.pokedex.domain.model.PokemonStat
import com.example.pokedex.domain.repository.PokemonRepository
import com.example.pokedex.util.Constants
import com.example.pokedex.util.PokemonTypeUtils
import com.example.pokedex.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Resource<List<PokemonListItem>> {
        return try {
            val response = apiService.getPokemonList(limit = limit, offset = offset)
            val items = response.results.map { resource ->
                val id = resource.extractId()
                PokemonListItem(
                    id = id,
                    name = resource.name,
                    imageUrl = "${Constants.OFFICIAL_ARTWORK_BASE_URL}$id.png"
                )
            }
            Resource.Success(items)
        } catch (e: IOException) {
            Resource.Error("Error de conexión: Verifica tu conexión a internet.")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.localizedMessage ?: "Ocurrió un error al cargar la lista"}")
        }
    }

    override suspend fun getPokemonDetail(nameOrId: String): Resource<PokemonDetail> {
        return try {
            val cleanQuery = nameOrId.lowercase().trim()

            // Consultar endpoint 2 (/pokemon/{id}) y endpoint 4 (/pokemon-species/{id})
            coroutineScope {
                val pokemonDeferred = async { apiService.getPokemonDetail(cleanQuery) }
                val speciesDeferred = async {
                    try {
                        apiService.getPokemonSpecies(cleanQuery)
                    } catch (e: Exception) {
                        null
                    }
                }

                val pokemonDto = pokemonDeferred.await()
                val speciesDto = speciesDeferred.await()

                val detail = mapToPokemonDetail(pokemonDto, speciesDto)
                Resource.Success(detail)
            }
        } catch (e: IOException) {
            Resource.Error("Error de conexión al cargar los detalles del Pokémon.")
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) {
                Resource.Error("No se encontró ningún Pokémon con ese nombre o número.")
            } else {
                Resource.Error("Error del servidor (${e.code()}). Intente más tarde.")
            }
        } catch (e: Exception) {
            Resource.Error("Error al cargar detalle: ${e.localizedMessage ?: "Desconocido"}")
        }
    }

    override suspend fun getPokemonTypes(): Resource<List<String>> {
        return try {
            val response = apiService.getPokemonTypes()
            // Filtramos tipos que no son estándar en juegos principales
            val excluded = setOf("unknown", "shadow", "stellar")
            val types = response.results
                .map { it.name }
                .filter { it !in excluded }
            Resource.Success(types)
        } catch (e: IOException) {
            Resource.Error("Error de conexión al cargar tipos.")
        } catch (e: Exception) {
            Resource.Error("Error al cargar tipos: ${e.localizedMessage}")
        }
    }

    override suspend fun getPokemonByType(type: String): Resource<List<PokemonListItem>> {
        return try {
            val response = apiService.getPokemonByType(type.lowercase().trim())
            val items = response.pokemon.map { entry ->
                val id = entry.pokemon.extractId()
                PokemonListItem(
                    id = id,
                    name = entry.pokemon.name,
                    imageUrl = "${Constants.OFFICIAL_ARTWORK_BASE_URL}$id.png",
                    primaryType = type
                )
            }.filter { it.id > 0 } // Ignorar posibles variantes especiales sin ID válido
            Resource.Success(items)
        } catch (e: IOException) {
            Resource.Error("Error de red al filtrar por tipo $type.")
        } catch (e: Exception) {
            Resource.Error("Error al filtrar por tipo: ${e.localizedMessage}")
        }
    }

    override suspend fun searchPokemon(query: String): Resource<PokemonListItem> {
        return try {
            val cleanQuery = query.lowercase().trim()
            val dto = apiService.getPokemonDetail(cleanQuery)
            val item = PokemonListItem(
                id = dto.id,
                name = dto.name,
                imageUrl = dto.sprites.other?.officialArtwork?.frontDefault
                    ?: "${Constants.OFFICIAL_ARTWORK_BASE_URL}${dto.id}.png",
                primaryType = dto.types.firstOrNull()?.type?.name
            )
            Resource.Success(item)
        } catch (e: IOException) {
            Resource.Error("Error de conexión al buscar '$query'.")
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) {
                Resource.Error("No se encontró ningún Pokémon llamado '$query'.")
            } else {
                Resource.Error("Error HTTP ${e.code()} al realizar la búsqueda.")
            }
        } catch (e: Exception) {
            Resource.Error("Error en la búsqueda: ${e.localizedMessage}")
        }
    }

    private fun mapToPokemonDetail(
        pokemon: PokemonDto,
        species: PokemonSpeciesDto?
    ): PokemonDetail {
        val imageUrl = pokemon.sprites.other?.officialArtwork?.frontDefault
            ?: pokemon.sprites.frontDefault
            ?: "${Constants.OFFICIAL_ARTWORK_BASE_URL}${pokemon.id}.png"

        // Altura: decímetros -> metros (ej. 7 -> 0.7 m)
        val heightMeters = pokemon.height / 10f
        // Peso: hectogramos -> kilogramos (ej. 69 -> 6.9 kg)
        val weightKg = pokemon.weight / 10f

        val types = pokemon.types.sortedBy { it.slot }.map { it.type.name }

        val stats = pokemon.stats.map { statSlot ->
            PokemonStat(
                name = statSlot.stat.name,
                spanishName = PokemonTypeUtils.getStatNameInSpanish(statSlot.stat.name),
                value = statSlot.baseStat
            )
        }

        val abilities = pokemon.abilities.sortedBy { it.slot }.map {
            PokemonAbility(
                name = it.ability.name,
                isHidden = it.isHidden
            )
        }

        // Obtener texto descriptivo en español con fallback en inglés
        val rawDescription = species?.flavorTextEntries?.find { it.language.name == "es" }?.flavorText
            ?: species?.flavorTextEntries?.find { it.language.name == "en" }?.flavorText
            ?: "No hay descripción disponible para este Pokémon."
        val cleanDescription = rawDescription.replace("\n", " ").replace("\u000c", " ").replace("\r", " ")

        val category = species?.genera?.find { it.language.name == "es" }?.genus
            ?: species?.genera?.find { it.language.name == "en" }?.genus
            ?: "Pokémon"

        return PokemonDetail(
            id = pokemon.id,
            name = pokemon.name,
            imageUrl = imageUrl,
            heightMeters = heightMeters,
            weightKg = weightKg,
            baseExperience = pokemon.baseExperience,
            types = types,
            stats = stats,
            abilities = abilities,
            description = cleanDescription,
            category = category,
            color = species?.color?.name ?: "red",
            habitat = species?.habitat?.name,
            generation = species?.generation?.name?.replace("generation-", "Gen ")?.uppercase(),
            captureRate = species?.captureRate,
            baseHappiness = species?.baseHappiness,
            isLegendary = species?.isLegendary ?: false,
            isMythical = species?.isMythical ?: false
        )
    }
}
