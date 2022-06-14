package com.example.pokemonapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonapp.databinding.ActivitySeleccionBinding
import com.pokemon.server.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private val viewModel: MainActivityViewModel by viewModels()
    var longitud = 5
    val user = getRandomString(longitud)
    val pass = getRandomString(longitud)
    val token = user + pass
    private lateinit var binding: ActivitySeleccionBinding
    private lateinit var listaPokemon: ListaPokemon
    private lateinit var usuario: Usuario

    private val tagListaPokemon = "TAG_LISTA_POKEMON"
    private val tagToken = "TOKEN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPokemon.layoutManager = LinearLayoutManager(this)
        binding.rvPokemon.adapter = AdapterPokemon(token)

        readFromPreferences()
       // usuario = llamada.get(user, pass, this, binding.root, listaPokemon)
        usuario = viewModel.get(user, pass, this, binding.root, listaPokemon)


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
            putString(tagToken, this@MainActivity.usuario.toJson())
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
        usuario = if (tokenTexto.isNullOrBlank()) {
            Usuario()
        } else {
            Usuario().fromJson(tokenTexto)

        }
    }

    fun getRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}


