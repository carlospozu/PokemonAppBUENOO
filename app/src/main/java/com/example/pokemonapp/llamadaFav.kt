package com.example.pokemonapp

import android.content.Context
import android.view.View
import android.widget.Toast
import com.example.pokemonapp.ObtenerPokemonRequest.Companion.nextInt
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.pokemon.server.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.*

class llamadaFav {

    companion object {

        private var gson = Gson()

        fun get(view: View, token: String, listaUser: Usuario): Int {

            val id = Random().nextInt(1..20)

            Thread.sleep(10000)
            val client = OkHttpClient()
            val request = Request.Builder()
            request.url("http://10.0.2.2:8084/pokemonFavorito/$token/$id")

            val call = client.newCall(request.build())
            // val response = call.execute()
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        Snackbar.make(view, "ERROR DE TOKEN", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ARREGLAR", View.OnClickListener {
                                llamada()
                            })
                            .show()

                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    println(response.toString())
                   if (listaUser.token == token)
                       listaUser.pokemonFavoritoId = id
                }
            })
            return id
        }


    }


}