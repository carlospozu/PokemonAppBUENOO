package com.example.pokemonapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import com.example.pokemonapp.databinding.ActivityPokemonBinding
import com.squareup.picasso.Picasso

class PokemonActivity : AppCompatActivity() {



    companion object {
        const val POKEMON_TAG = "Pokemon"
        fun start(pokemon: Pokemon, context: Context) {
            val intent = Intent(context, PokemonActivity::class.java)
            intent.putExtra(POKEMON_TAG, pokemon.toJson())
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityPokemonBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.boton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val pokemonJson = intent.getStringExtra(POKEMON_TAG)
        if (pokemonJson != null) {
            val pokemon = Pokemon.fromJson(pokemonJson)
            binding.tvPokemonNombre.text = pokemon.nameCapitalized()
            Picasso.get().load(pokemon.sprites.frontDefault).into(binding.fotoPokemon)
            Picasso.get().load(pokemon.sprites.backDefault).into(binding.fotoDetrasPokemon)
            val altura =  pokemon.height.toString()
            val peso = pokemon.weight.toString()
            binding.tvPokemonAlt.text = "ALTURA : " + altura + " Centimetros"
            binding.tvPokemonPeso.text = "PESO : " + peso + " Gramos"
            binding.tvPokemonTipo.text = "TIPO :"


            val image1 = pokemon.obtenerImagenTipo1()
            if (image1 != null)
               binding.ivPokemonTipo1.setImageResource(image1)
            else
                binding.ivPokemonTipo1.setImageDrawable(null)
            val image2 = pokemon.obtenerImagenTipo2()
            if (image2 != null)
                binding.ivPokemonTipo2.setImageResource(image2)

        } else {
            Toast.makeText(this, "No se ha recibido ningún Pokémon", Toast.LENGTH_LONG).show()
            finish()
        }
    }



    }
