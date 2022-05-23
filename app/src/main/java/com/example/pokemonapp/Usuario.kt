package com.pokemon.server

import com.example.pokemonapp.ListaPokemon
import com.google.gson.Gson


data class Usuario(var nombre: String, var pass: String) {
    val token = nombre + pass
    var pokemonFavoritoId : Int? = null

    companion object {
        fun fromJson(json: String): ListaPokemon {
            val gson = Gson()
            return gson.fromJson(json, ListaPokemon::class.java)
        }
    }

    fun toJson(): String{
        val gson = Gson()
        return gson.toJson(this)
    }
}