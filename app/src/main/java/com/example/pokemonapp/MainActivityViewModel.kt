package com.example.pokemonapp

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.pokemonapp.ObtenerPokemonRequest.Companion.nextInt
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.pokemon.server.Usuario
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import java.util.*

class MainActivityViewModel : ViewModel() {


        fun get(
            user: String,
            pass: String,
            context: Context,
            view: View,
            listaPokemon: ListaPokemon
        ): Usuario {
            val listaUser = Usuario()
            viewModelScope.launch {
                withContext(Dispatchers.IO) {

                    val token = user + pass

                    val client = OkHttpClient()

                    val request = Request.Builder()
                    request.url("http://10.0.2.2:8084/crearUsuario/$user/$pass")


                    val call = client.newCall(request.build())
                    call.enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            println(e.toString())
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(context, "Algo ha ido mal", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {

                            println(response.toString())
                            response.body?.let { responseBody ->
                                val body = responseBody.string()
                                println(body)
                                val gson = Gson()
                                val id = llamadafavorito(view, token, listaUser)
                                val usuario = gson.fromJson(body, Usuario::class.java)
                                CoroutineScope(Dispatchers.Main).launch {
                                usuario.pokemonFavoritoId = id
                                var cont = 0
                                listaPokemon.listaPokemon.forEach {
                                    cont++
                                    if (id == cont)
                                        it.favorito
                                }
                            }
                            }
                        }
                    })


                }}
            return listaUser

        }


    fun llamadafavorito(view: View, token: String, listaUser: Usuario): Int {
        val id = Random().nextInt(1..20)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {



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
                        CoroutineScope(Dispatchers.Main).launch {
                            if (listaUser.token == token)
                                listaUser.pokemonFavoritoId = id
                        }
                    }
                })

            }

        }
        return id
    }

    }


