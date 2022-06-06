package com.example.pokemonapp

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.pokemon.server.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.*

class llamada {

    companion object {


        fun get(
            user: String,
            pass: String,
            context: Context,
            view: View,
            listaPokemon: ListaPokemon
        ): Usuario {
            val token = user + pass
            val listaUser = Usuario()
            val client = OkHttpClient()

            val request = Request.Builder()
            request.url("http://10.0.2.2:8084/crearUsuario/$user/$pass")


            val call = client.newCall(request.build())
            call.enqueue( object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Algo ha ido mal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    println(response.toString())
                    response.body?.let { responseBody ->
                        val body = responseBody.string()
                        println(body)
                        val gson = Gson()
                        val id = llamadaFav.get(view, token)

                        val usuario = gson.fromJson(body, Usuario::class.java)
                        usuario.pokemonFavoritoId=id
                       var cont = 0
                        listaPokemon.listaPokemon.forEach {
                            cont ++
                            if (id == cont)
                                it.favorito
                        }
                    }}} )

            return listaUser
        }


    }


}