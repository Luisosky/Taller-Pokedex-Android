package com.example.pokedex.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.PokemonListItem
import com.example.pokedex.domain.repository.PokemonRepository
import com.example.pokedex.util.Constants
import com.example.pokedex.util.NetworkMonitor
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = Constants.PAGE_SIZE

    // Caché de la lista paginada principal para restaurar al limpiar búsqueda o filtro
    private val cachedMainList = mutableListOf<PokemonListItem>()

    private var searchJob: Job? = null

    init {
        observeNetwork()
        loadAvailableTypes()
        loadInitialPokemonList()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                _uiState.update { it.copy(isOnline = online) }
            }
        }
    }

    private fun loadAvailableTypes() {
        viewModelScope.launch {
            when (val result = repository.getPokemonTypes()) {
                is Resource.Success -> {
                    _uiState.update { it.copy(availableTypes = result.data ?: emptyList()) }
                }
                is Resource.Error -> {
                    // Si falla cargar tipos, no bloquea el flujo principal
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun loadInitialPokemonList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    selectedType = null,
                    isEndReached = false
                )
            }
            currentOffset = 0
            cachedMainList.clear()

            when (val result = repository.getPokemonList(limit = pageSize, offset = currentOffset)) {
                is Resource.Success -> {
                    val list = result.data ?: emptyList()
                    cachedMainList.addAll(list)
                    currentOffset += list.size
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pokemonList = list,
                            isEndReached = list.size < pageSize
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Error al cargar la lista de Pokémon"
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    /**
     * Paginación mediante scroll infinito (Endpoint 1: /pokemon?limit=&offset=)
     */
    fun loadNextPage() {
        val state = _uiState.value
        // Solo paginar en la lista principal sin filtros activos ni búsqueda
        if (state.isLoading || state.isLoadingMore || state.isEndReached ||
            state.searchQuery.isNotEmpty() || state.selectedType != null
        ) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            when (val result = repository.getPokemonList(limit = pageSize, offset = currentOffset)) {
                is Resource.Success -> {
                    val newItems = result.data ?: emptyList()
                    if (newItems.isNotEmpty()) {
                        cachedMainList.addAll(newItems)
                        currentOffset += newItems.size
                        _uiState.update {
                            it.copy(
                                isLoadingMore = false,
                                pokemonList = cachedMainList.toList(),
                                isEndReached = newItems.size < pageSize
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoadingMore = false,
                                isEndReached = true
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            errorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    /**
     * Búsqueda por nombre o número con debounce (Endpoint 2: /pokemon/{query})
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, errorMessage = null) }
        searchJob?.cancel()

        if (query.isBlank()) {
            // Restaurar estado anterior (filtro por tipo o lista principal)
            val selectedType = _uiState.value.selectedType
            if (selectedType != null) {
                onTypeSelected(selectedType)
            } else {
                _uiState.update {
                    it.copy(
                        pokemonList = cachedMainList.toList(),
                        isLoading = false,
                        errorMessage = null,
                        isEndReached = false
                    )
                }
            }
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // Debounce para evitar llamadas excesivas
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = repository.searchPokemon(query)) {
                is Resource.Success -> {
                    val found = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pokemonList = if (found != null) listOf(found) else emptyList(),
                            isEndReached = true
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pokemonList = emptyList(),
                            errorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    /**
     * Filtrado por tipo elemental (Endpoint 3: /type/{type})
     */
    fun onTypeSelected(type: String?) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                selectedType = type,
                searchQuery = "",
                errorMessage = null
            )
        }

        if (type == null) {
            // Restaurar lista principal
            _uiState.update {
                it.copy(
                    pokemonList = cachedMainList.toList(),
                    isLoading = false,
                    isEndReached = false
                )
            }
            if (cachedMainList.isEmpty()) {
                loadInitialPokemonList()
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getPokemonByType(type)) {
                is Resource.Success -> {
                    val items = result.data ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pokemonList = items,
                            isEndReached = true
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pokemonList = emptyList(),
                            errorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun retry() {
        val state = _uiState.value
        when {
            state.searchQuery.isNotBlank() -> onSearchQueryChange(state.searchQuery)
            state.selectedType != null -> onTypeSelected(state.selectedType)
            else -> loadInitialPokemonList()
        }
    }
}
