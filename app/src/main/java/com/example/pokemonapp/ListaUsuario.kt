package com.example.pokemonapp

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.pokemon.server.Usuario
import okhttp3.Callback
import java.util.*


class ListaUsuario(var listaUsuario : MutableList<Usuario> = mutableListOf()) {


    companion object {
        fun fromJson(json: String):ListaUsuario {
            val gson = Gson()
            return gson.fromJson(json, ListaUsuario::class.java)
        }
    }

    fun agregar(usuario: Usuario) {
        listaUsuario.add(usuario)
    }

    fun cambiarFav(id: Int, token: String, view: View){

        listaUsuario.forEach{
            if (it.token == token)
                it.pokemonFavoritoId = id
            else
                Snackbar.make(
                    view,
                    "ERROR DE TOKEN",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
        }
        }




    fun imprimirUsuarios(){
        if (listaUsuario.isEmpty()) {
            println("No se ha encontrado a ese Pokémon")
        } else {
            listaUsuario.forEach {
                println(it)
            }
        }
    }


    fun toJson(): String{
        val gson = Gson()
        return gson.toJson(this)
    }


}


