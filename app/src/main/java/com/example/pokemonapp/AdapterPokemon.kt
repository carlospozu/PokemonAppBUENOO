package com.example.pokemonapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.databinding.ItemPokemonBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class AdapterPokemon : RecyclerView.Adapter<AdapterPokemon.PokemonViewHolder>() {

    class PokemonViewHolder(val pokemonBinding: ItemPokemonBinding) : RecyclerView.ViewHolder(pokemonBinding.root)

    private var pokemons = ListaPokemon()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val pokemonBinding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(pokemonBinding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons.listaPokemon[position]
        holder.pokemonBinding.progressVida1.max = pokemon.hpMax
        holder.pokemonBinding.progressVida1.progress = pokemon.hpRest
        holder.pokemonBinding.tvPokemon.text = pokemon.nameCapitalized()
        holder.pokemonBinding.progressVida1.progressDrawable.colorFilter
/*
        if (holder.pokemonBinding.progressVida1.progress >= holder.pokemonBinding.progressVida1.max*0.7 ){
            holder.pokemonBinding.progressVida1.progress.toColor() = Color.GREEN
        }else{
            if (holder.pokemonBinding.progressVida1.progress > holder.pokemonBinding.progressVida1.max*0.4){
                holder.pokemonBinding.progressVida1.progressDrawable.colorFilter = BlendModeColorFilter(Color.YELLOW, BlendMode.SRC_IN)
            }else{
                if (holder.pokemonBinding.progressVida1.progress < holder.pokemonBinding.progressVida1.max*0.4){
                    holder.pokemonBinding.progressVida1.progressDrawable.colorFilter = BlendModeColorFilter(Color.RED, BlendMode.SRC_IN)
            }
            }
        }*/

        holder.pokemonBinding.progressVida1.apply {
            max = pokemon.hpMax
            progress = pokemon.hpRest
            progressTintList = ColorStateList.valueOf(
                when{
                    pokemon.hpRest < pokemon.hpMax* 0.15 -> Color.RED
                    pokemon.hpRest < pokemon.hpMax* 0.5 -> Color.YELLOW
                    else -> Color.GREEN
                }
            )
        }



        Picasso.get().load(pokemon.sprites.frontDefault).into(holder.pokemonBinding.ivPokemon)
        val image1 = pokemon.obtenerImagenTipo1()
        if (image1 != null)
            holder.pokemonBinding.ivTipo1.setImageResource(image1)
        else
            holder.pokemonBinding.ivTipo1.setImageDrawable(null)
        val image2 = pokemon.obtenerImagenTipo2()
        if (image2 != null)
            holder.pokemonBinding.ivTipo2.setImageResource(image2)
        else
            holder.pokemonBinding.ivTipo2.setImageDrawable(null)

        holder.pokemonBinding.root.setOnClickListener {
            PokemonActivity.start(pokemon, holder.pokemonBinding.root.context)
        }
    }

    override fun getItemCount(): Int {
        return pokemons.listaPokemon.size
    }

    fun actualizarLista(listaPokemon: ListaPokemon) {
        pokemons = listaPokemon
        notifyDataSetChanged()
    }
    private fun verde(visible : Boolean, holder: PokemonViewHolder) {
        holder.pokemonBinding.progressVida1.visibility = if (visible) View.GONE else View.VISIBLE
    }


}