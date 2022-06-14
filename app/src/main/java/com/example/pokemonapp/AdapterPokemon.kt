package com.example.pokemonapp


import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.ObtenerPokemonRequest.Companion.nextInt
import com.example.pokemonapp.databinding.ItemPokemonBinding
import com.google.android.material.snackbar.Snackbar
import com.pokemon.server.Usuario
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.*


class AdapterPokemon(val token: String) : RecyclerView.Adapter<AdapterPokemon.PokemonViewHolder>() {


    class PokemonViewHolder(val pokemonBinding: ItemPokemonBinding) : RecyclerView.ViewHolder(pokemonBinding.root)

    private var pokemons = ListaPokemon()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val pokemonBinding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(pokemonBinding)
    }


    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        colorFavorito(holder, position )
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

      holder.pokemonBinding.caja.setOnLongClickListener{
        selecionarFavorito(holder, position, token)
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
        //asignarFav(position, usuarios.listaUsuario, holder)
    }


    override fun getItemCount(): Int {
        return pokemons.listaPokemon.size
    }
    /*
    fun asignarFav(
        position: Int, listaUsuario: MutableList<Usuario>,
        holder: PokemonViewHolder,
    ){
        listaUsuario.forEach {
            pokemons.listaPokemon[position].favorito = it.pokemonFavoritoId == position
        }
        colorFavorito(holder, position, pokemons.listaPokemon)
    }

     */

    fun colorFavorito(holder: PokemonViewHolder, position: Int){

        pokemons.listaPokemon.forEach {
            if (it.favorito == true)
                holder.pokemonBinding.caja.setBackgroundColor(Color.LTGRAY)
            else
                holder.pokemonBinding.caja.setBackgroundColor(Color.BLACK)
        }
    }

    fun selecionarFavorito(holder: PokemonViewHolder, position: Int, token: String): Boolean{
        val id = llamadaFav(token, holder, position)

        pokemons.listaPokemon[id].favorito = pokemons.listaPokemon[id].favorito == false
        if (pokemons.listaPokemon[id].favorito == true)
            holder.pokemonBinding.caja.setBackgroundColor(Color.LTGRAY)
        else
            holder.pokemonBinding.caja.setBackgroundColor(Color.BLACK)
    

     //   colorFavorito(holder, position)

       /* pokemons.listaPokemon[position].favorito = pokemons.listaPokemon[position].favorito != true
        colorFavorito(holder, position)
        return true
        */

        return true
    }


    private fun llamadaFav(token: String, holder: PokemonViewHolder, position: Int) : Int {
            val client = OkHttpClient()
            val request = Request.Builder()
            request.url("http://10.0.2.2:8084/pokemonFavorito/$token/$position")

            val call = client.newCall(request.build())
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                       Snackbar.make(holder.pokemonBinding.caja, "ERROR DE TOKEN", Snackbar.LENGTH_INDEFINITE)
                           .setAction(
                               "ARREGLAR",
                           ) {
                               llamada()
                           }
                           .show()
                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    println(response.toString())
                }
            })

        return position
    }


    @SuppressLint("NotifyDataSetChanged")
    fun actualizarLista(listaPokemon: ListaPokemon) {
        pokemons = listaPokemon
        notifyDataSetChanged()
    }



}