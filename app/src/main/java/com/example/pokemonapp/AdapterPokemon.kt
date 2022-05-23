package com.example.pokemonapp


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.databinding.ItemPokemonBinding
import com.pokemon.server.Usuario
import com.squareup.picasso.Picasso


class AdapterPokemon : RecyclerView.Adapter<AdapterPokemon.PokemonViewHolder>() {

    class PokemonViewHolder(val pokemonBinding: ItemPokemonBinding) : RecyclerView.ViewHolder(pokemonBinding.root)

    private var pokemons = ListaPokemon()
    private var user = Usuario


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

        colorFavorito(holder, position)

      holder.pokemonBinding.caja.setOnLongClickListener{
          selecionarFavorito(holder ,position)


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

    fun colorFavorito(holder: PokemonViewHolder, position: Int){
        if ( pokemons.listaPokemon[position].favorito == true)
            holder.pokemonBinding.caja.setBackgroundColor(Color.LTGRAY)
        if ( pokemons.listaPokemon[position].favorito == false)
            holder.pokemonBinding.caja.setBackgroundColor(Color.BLACK)
    }

    fun selecionarFavorito(holder: PokemonViewHolder ,position: Int ): Boolean {

        colorFavorito(holder, position)
        return true
    }


    fun actualizarLista(listaPokemon: ListaPokemon) {
        pokemons = listaPokemon
        notifyDataSetChanged()
    }



}