package com.example.pokedex.util

import androidx.compose.ui.graphics.Color

object PokemonTypeUtils {

    val STANDARD_TYPES = listOf(
        "normal",
        "fire",
        "water",
        "grass",
        "electric",
        "ice",
        "fighting",
        "poison",
        "ground",
        "flying",
        "psychic",
        "bug",
        "rock",
        "ghost",
        "dragon",
        "steel",
        "fairy",
        "dark"
    )

    fun getTypeColor(type: String): Color {
        return when (type.lowercase()) {
            "normal" -> Color(0xFFA8A878)
            "fire", "fuego" -> Color(0xFFF08030)
            "water", "agua" -> Color(0xFF6890F0)
            "grass", "planta" -> Color(0xFF78C850)
            "electric", "electrico", "eléctrico" -> Color(0xFFF8D030)
            "ice", "hielo" -> Color(0xFF98D8D8)
            "fighting", "lucha" -> Color(0xFFC03028)
            "poison", "veneno" -> Color(0xFFA040A0)
            "ground", "tierra" -> Color(0xFFE0C068)
            "flying", "volador" -> Color(0xFFA890F0)
            "psychic", "psiquico", "psíquico" -> Color(0xFFF85888)
            "bug", "bicho" -> Color(0xFFA8B820)
            "rock", "roca" -> Color(0xFFB8A038)
            "ghost", "fantasma" -> Color(0xFF705898)
            "dragon" -> Color(0xFF7038F8)
            "steel", "acero" -> Color(0xFFB8B8D0)
            "fairy", "hada" -> Color(0xFFEE99AC)
            "dark", "siniestro" -> Color(0xFF705848)
            else -> Color(0xFF68A090)
        }
    }

    fun getTypeNameInSpanish(type: String): String {
        return when (type.lowercase()) {
            "normal" -> "Normal"
            "fire" -> "Fuego"
            "water" -> "Agua"
            "grass" -> "Planta"
            "electric" -> "Eléctrico"
            "ice" -> "Hielo"
            "fighting" -> "Lucha"
            "poison" -> "Veneno"
            "ground" -> "Tierra"
            "flying" -> "Volador"
            "psychic" -> "Psíquico"
            "bug" -> "Bicho"
            "rock" -> "Roca"
            "ghost" -> "Fantasma"
            "dragon" -> "Dragón"
            "steel" -> "Acero"
            "fairy" -> "Hada"
            "dark" -> "Siniestro"
            else -> type.replaceFirstChar { it.uppercase() }
        }
    }

    fun getStatNameInSpanish(stat: String): String {
        return when (stat.lowercase()) {
            "hp" -> "PS"
            "attack" -> "Ataque"
            "defense" -> "Defensa"
            "special-attack" -> "At. Esp."
            "special-defense" -> "Def. Esp."
            "speed" -> "Velocidad"
            else -> stat.replaceFirstChar { it.uppercase() }
        }
    }
}
