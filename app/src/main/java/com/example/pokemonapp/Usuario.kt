package com.pokemon.server

import com.example.pokemonapp.ListaPokemon
import com.google.gson.Gson


 class Usuario() {
    val nombre: String = ""
    val pass: String = ""
    val token = nombre + pass
    var pokemonFavoritoId : Int? = null

        fun fromJson(json: String): Usuario {
            val gson = Gson()
            return gson.fromJson(json, Usuario::class.java)
        }


    fun toJson(): String{
        val gson = Gson()
        return gson.toJson(this)
    }
}