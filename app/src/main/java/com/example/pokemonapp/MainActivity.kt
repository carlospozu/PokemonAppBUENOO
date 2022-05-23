package com.example.pokemonapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonapp.ObtenerPokemonRequest.Companion.nextInt
import com.example.pokemonapp.databinding.ActivitySeleccionBinding
import com.google.gson.Gson
import com.pokemon.server.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {


    var longitud = 5
    val user = getRandomString(longitud)
    val pass = getRandomString(longitud)
    val token = user + pass
    private lateinit var binding: ActivitySeleccionBinding
    private lateinit var listaPokemon: ListaPokemon

    private val tagListaPokemon = "TAG_LISTA_POKEMON"
    private val tagToken ="TOKEN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPokemon.layoutManager = LinearLayoutManager(this)
        binding.rvPokemon.adapter = AdapterPokemon()




        readFromPreferences()
        val tokenTexto = getPreferences(Context.MODE_PRIVATE).getString(token, "")
        if (tokenTexto.isNullOrBlank())
            llamada()
        llamadaFav()


        actualizarAdapter(listaPokemon)

        initBotonDescarga()

    }

    private fun initBotonDescarga() {
        binding.bDescarga.contentDescription = if (listaPokemon.listaPokemon.isNullOrEmpty()) {
            getString(R.string.descargar_pokemons)
        } else {
            getString(R.string.recargar_pokemons)
        }

        if (listaPokemon.listaPokemon.isNullOrEmpty()) {
            binding.bDescarga.setImageResource(R.mipmap.ic_descarga)
        } else {
            binding.bDescarga.setImageResource(R.mipmap.ic_recarga)
        }


        binding.bDescarga.setOnClickListener {
            loadingVisible(true)
            lifecycleScope.launch(Dispatchers.IO) {
                listaPokemon = ObtenerPokemonRequest.get()
                withContext(Dispatchers.Main) {
                    actualizarAdapter(listaPokemon)
                    initBotonDescarga()
                    loadingVisible(false)
                }
                writeInPreferences()
            }
        }
    }

    private fun loadingVisible(visible : Boolean) {
        binding.pbLoading.visibility = if (visible) View.VISIBLE else View.GONE
        binding.bDescarga.visibility = if (!visible) View.VISIBLE else View.GONE

    }

    private fun actualizarAdapter(listaPokemon : ListaPokemon){
        (binding.rvPokemon.adapter as AdapterPokemon).actualizarLista(listaPokemon)
    }

    private fun writeInPreferences() {
         getPreferences(Context.MODE_PRIVATE).edit().apply {
            putString(tagListaPokemon, this@MainActivity.listaPokemon.toJson())
            apply()
        }
    }
    private fun llamada(){

            val client = OkHttpClient()

            val request = Request.Builder()
            request.url("http://10.0.2.2:8084/crearUsuario/$user/$pass")


            val call = client.newCall(request.build())
            call.enqueue( object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@MainActivity, "Algo ha ido mal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    println(response.toString())
                    response.body?.let { responseBody ->
                        val body = responseBody.string()
                        println(body)
                        val gson = Gson()

                        val usuario = gson.fromJson(body, Usuario::class.java)
    }}} )}

    private fun llamadaFav(){
        val id = Random().nextInt(1..150)
        val client = OkHttpClient()

        val request = Request.Builder()
        request.url("http://10.0.2.2:8084/pokemonFavorito/$token/$id")


        val call = client.newCall(request.build())
        call.enqueue( object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@MainActivity, "Algo ha ido mal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.toString())
                response.body?.let { responseBody ->
                    val body = responseBody.string()
                    println(body)
                    val gson = Gson()

                    val usuario = gson.fromJson(body, Usuario::class.java)

                }}} )
     }

    private fun readFromPreferences() {
        val pokemonsText = getPreferences(Context.MODE_PRIVATE).getString(tagListaPokemon, "")
        listaPokemon = if (pokemonsText.isNullOrBlank()){
            ListaPokemon()
        } else {
            ListaPokemon.fromJson(pokemonsText)
        }
    }

    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }



}