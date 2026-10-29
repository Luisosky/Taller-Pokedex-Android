package com.example.pokedex.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.repository.PokemonRepository
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    private val pokemonId: String = savedStateHandle.get<Int>("pokemonId")?.toString()
        ?: savedStateHandle.get<String>("pokemonId")
        ?: ""

    init {
        if (pokemonId.isNotEmpty()) {
            loadPokemonDetail(pokemonId)
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Identificador de Pokémon no válido"
                )
            }
        }
    }

    fun loadPokemonDetail(idOrName: String = pokemonId) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = repository.getPokemonDetail(idOrName)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pokemonDetail = result.data,
                            errorMessage = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "No se pudo cargar la información del Pokémon"
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun retry() {
        loadPokemonDetail(pokemonId)
    }
}
