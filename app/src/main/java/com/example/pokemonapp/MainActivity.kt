package com.example.pokemonapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonapp.databinding.ActivitySeleccionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    var longitud = 5
    val user = getRandomString(longitud)
    val pass = getRandomString(longitud)
    val token = user + pass
    private lateinit var binding: ActivitySeleccionBinding
    private lateinit var listaPokemon: ListaPokemon
    private lateinit var listaUsuario: ListaUsuario

    private val tagListaPokemon = "TAG_LISTA_POKEMON"
    private val tagToken = "TOKEN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPokemon.layoutManager = LinearLayoutManager(this)
        binding.rvPokemon.adapter = AdapterPokemon(token)

        readFromPreferences()
        listaUsuario = llamada.get(user, pass, this, binding.root, listaPokemon)


        actualizarAdapter(listaPokemon)

        initBotonDescarga()

    }

    private fun initBotonDescarga() {
        binding.bDescarga.contentDescription = if (listaPokemon.listaPokemon.isEmpty()) {
            getString(R.string.descargar_pokemons)
        } else {
            getString(R.string.recargar_pokemons)
        }

        if (listaPokemon.listaPokemon.isEmpty()) {
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

    private fun loadingVisible(visible: Boolean) {
        binding.pbLoading.visibility = if (visible) View.VISIBLE else View.GONE
        binding.bDescarga.visibility = if (!visible) View.VISIBLE else View.GONE

    }

    private fun actualizarAdapter(listaPokemon: ListaPokemon) {
        (binding.rvPokemon.adapter as AdapterPokemon).actualizarLista(listaPokemon)
    }

    private fun writeInPreferences() {
        getPreferences(Context.MODE_PRIVATE).edit().apply {
            putString(tagListaPokemon, this@MainActivity.listaPokemon.toJson())
            putString(tagToken, this@MainActivity.listaUsuario.toJson())
            apply()
        }
    }


    private fun readFromPreferences() {
        val pokemonsText = getPreferences(Context.MODE_PRIVATE).getString(tagListaPokemon, "")
        listaPokemon = if (pokemonsText.isNullOrBlank()) {
            ListaPokemon()
        } else {
            ListaPokemon.fromJson(pokemonsText)
        }

        val tokenTexto = getPreferences(Context.MODE_PRIVATE).getString(tagToken, "")
        listaUsuario = if (tokenTexto.isNullOrBlank()) {
            ListaUsuario()
        } else {
            ListaUsuario.fromJson(tokenTexto)
        }
    }

    fun getRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}
/*
    private fun llamadaFav() : Int {
        val listaUser1 = ListaUsuario()
        val id = Random().nextInt(1..20)

            Thread.sleep(10000)
            val client = OkHttpClient()
            val request = Request.Builder()
            request.url("http://10.0.2.2:8084/pokemonFavorito/$token/$id")

            val call = client.newCall(request.build())
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        Snackbar.make(binding.rvPokemon, "ERROR DE TOKEN", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ARREGLAR", View.OnClickListener {
                                llamada()
                            })
                            .show()
                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    println(response.toString())
                    listaUser1.cambiarFav(id, token, binding.root)
                }
            })
        return id
    }

 */



   /* private fun llamada(): ListaUsuario {
        val listaUser = ListaUsuario()
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
                     val id = llamadaFav()

                    val usuario = gson.fromJson(body, Usuario::class.java)
                    usuario.pokemonFavoritoId=id
                    listaUser.agregar(usuario)

                }}} )

    return listaUser}

    */


