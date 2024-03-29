package com.example.applibre.ui.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applibre.data.model.SuperHero
import com.example.applibre.data.model.db.SuperHeroState
import com.example.applibre.network.SuperHeroApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception

/**
 * lógica de las cartas y de los SuperHeroes
 */
class HeroDeckViewModel:ViewModel(){
    /**
     * @param auth Firebase Authentication
     * @param firestore Inicializa firestore con una instancia del cliente de Firestore.
     * @param _superHero lista donde guarda el jugador sus superheroes
     * @param superHeroDeck es la variable visible que referencia a _superHero
     * @param superHeroDeckPlayer son las cartas que tiene el jugador en la base de datos
     * @param superHeroesDC contiene superHeroes de la lista de DC -> pero en este caso contiene todos
     * @param superHeroresMarvel contiene superHeroes de la lista de Marvel
     * @param lista guarda los superheroes en una lista para después pasarlo a la final
     * @param actualSuperHero superHeroe seleccionado actual
     * @param listaId contiene id de todos los superheroes que podemos tener entre las cartas
     * @param listaIdDc contiene id de superheroes de DC
     * @param listaMarvel contiene id de superheroes de Marvel
     * @param query contiene el texto de busqueda
     * @param active si está activo el barra de busqueda
     * contiene los id de los superheroes y estos son los que contiene:
     *     ID -> nombre superHeroe
     *     70->batman, 655->superman, 485->naruto, 215->Deathlok, 201->daredevil, 435->master chief,
     *     423->magneto, 620->spiderman, 489->nick fury, 10->agent bob, 263->flash, 280->Ghost Rider,
     *     43->Ares, 52->Atom Girl, 298->Green Arrow, 309->Harley Queen, 311->Havok, 322->HellBoy,
     *     345->Fire Man, 213->Death-pool, 670->Toad, 538->Ra's Al Ghu, 550->Red Skull
     *     720 -> Wonder Woman, 491 -> NigthWing, 165 -> Catwoman, 194 -> Cyborg, 38 -> Aquaman,
     *     432 -> Martian manhunter, 132 -> Booster Gold, 367 -> John Constantine
     *     505 -> Oracle, 268 -> Forge, 732 -> Iron Man, 717 -> wolverine, 332 -> Hulk
     *     598 -> Silver Surfer, 456 -> Mister Fantastic, 216 -> DeathStroke
     *     724 -> X-Man, 149 -> Captain America, 156 -> Captain Marvel,
     *     405 -> Lex Luthor, 162 -> Carnage, 416 -> Luke Cage
     *     657 -> The Comedian, 370 -> Joker,
     *     195 -> Cyborg Superman, 213 -> DeadPool
     *     459 -> Mister Mxyzptlk, 705 -> Warlock,
     *     637 -> StephenWolf, 151 -> Captain Britain,
     *     687 -> Venom
     * */

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val firestore = Firebase.firestore

    private val _superHeroesDC  = MutableStateFlow<List<SuperHero>>(emptyList())
    val superHeroDeckDC: StateFlow<List<SuperHero>> = _superHeroesDC

    private val _superHeroDeckPlayer = MutableStateFlow<List<SuperHeroState>>(emptyList())
    val superHeroDeckPlayer: StateFlow<List<SuperHeroState>> =  _superHeroDeckPlayer

    private val lista: MutableList<SuperHero> = mutableListOf()
    private var actualSuperHero by mutableStateOf(SuperHero())
        private set;

    /*private val listaIdDc = listOf(70, 655, 52, 298, 538, 720, 491, 165, 194, 38, 432, 132, 367, 505,
        216, 156, 405)
    private val listaIdMarvel = listOf(215, 201, 423, 620, 489, 10, 263, 280, 43, 309, 311, 322, 345,
        213, 670, 268, 732, 724, 149, 162)*/


    private val listaId = listOf(215, 201, 423, 620, 489, 10, 263, 280, 43, 309, 311, 322, 345,
        213, 670, 70, 655, 52, 298, 538, 720, 491, 165, 194, 38, 432, 132, 367, 505, 268, 732,
        332, 598, 216, 724, 149, 156, 405, 162, 416, 657, 705, 637, 687)

    val query = MutableStateFlow("")

    val active = MutableStateFlow(false)


    init {
        getSuperHeroes()
    }


    /**
     * obtiene todas las cartas guardadas en la lista
     */
    private fun getSuperHeroes(){
        //iniciamos una corrutina
        lista.clear()
        for (i in 0..listaId.size-1) {
            viewModelScope.launch {
                try {
                    val superHeroId = SuperHeroApi.retrofitService.getSuperHeroById(listaId[i].toString())

                    val gson = Gson()
                    val superheroResponse = gson.fromJson(superHeroId, SuperHero::class.java)
                    if(superheroResponse.response=="success"){
                        lista.add(superheroResponse)
                        _superHeroesDC.value = lista
                    }
                }catch (e:IOException){
                    Log.d("getSuperHeroes: ", e.message.toString())
                }
            }
        }
    }


    /**
     * obtiene superHeroes de DC
     * son métodos antiguos para intentar separar las listas por lazy row
     * pero no me funcionaba
     */
    /*fun getSuperHeroesDC(){
        //iniciamos una corrutina
        lista.clear()
        for (i in listaIdDc.indices) {
            viewModelScope.launch {
                try {
                    val superHeroId = SuperHeroApi.retrofitService.getSuperHeroById(listaIdDc[i].toString())

                    val gson = Gson()
                    val superheroResponse = gson.fromJson(superHeroId, SuperHero::class.java)
                    if(superheroResponse.response=="success"){
                        lista.add(superheroResponse)
                        _superHeroesDC.value = lista
                    }
                }catch (e:IOException){
                    Log.d("getSuperHeroes: ", e.message.toString())
                }
            }
        }
    }


    fun getSuperHeroesMarvel(){
        //iniciamos una corrutina
        lista.clear()
        for (i in listaIdDc.indices) {
            viewModelScope.launch {
                try {
                    val superHeroId = SuperHeroApi.retrofitService.getSuperHeroById(listaIdMarvel[i].toString())

                    val gson = Gson()
                    val superheroResponse = gson.fromJson(superHeroId, SuperHero::class.java)
                    if(superheroResponse.response=="success"){
                        lista.add(superheroResponse)
                        _superHeroesDC.value = lista
                    }
                }catch (e:IOException){
                    Log.d("getSuperHeroes: ", e.message.toString())
                }
            }
        }
    }*/


    /**
     * devuelve el superHeroe actual
     * @param id le pasamos el string del id del superHeroe que hemos pulsado
     * esto nos permitirá obtener el superheroe por id
     */
    fun findById(id:String):SuperHero{
        for(l in lista){
            if(l.id == id){
                 actualSuperHero = l
            }
        }
        return actualSuperHero
    }

    /**
     * función sin implementar que va a permitir poner a pelear a dos superheores
     * @param superHero uno de los superHeroes que combate
     * @param superHero2 el enemigo contra el que combate el superHero
     */
    fun combatir(superHero: SuperHero, superHero2:SuperHero){
        val powerStats1 = superHero.powerStats
        val powerStats2 = superHero2.powerStats

        powerStats1.durability = (powerStats2.strength.toInt() - powerStats1.durability.toInt()).toString()
        powerStats2.durability = (powerStats1.strength.toInt() - powerStats2.durability.toInt()).toString()

    }

//COMIENZA LA PARTE ONLINE

    /**
     * @param onSuccess en el caso de lograr guardar en superHeroe
     * Guarda el superHeroe en la base de datos
     */
    fun saveSuperHero(onSuccess:() -> Unit){
        val email = auth.currentUser?.email
        viewModelScope.launch (Dispatchers.IO){
            try {
                val newSuperHero = hashMapOf(
                    "id" to actualSuperHero.id,
                    "name" to actualSuperHero.name,
                    "powerstats" to actualSuperHero.powerStats,
                    "image" to actualSuperHero.image.url,
                    "emailUser" to email.toString()
                )
                firestore.collection("SuperHeroes")
                    .add(newSuperHero)
                    .addOnSuccessListener {
                        onSuccess()
                        Log.d("Error save","Se guardó el superHeroe")
                    }.addOnFailureListener{
                        Log.d("Save error","Error al guardar")
                    }
            }catch (e:Exception){
                Log.d("Error al guardar superHeroe","Error al guardar SuperHeroe")
            }
        }
    }

    /**
     * guarda los superHeroes que tenga el usuario en la base de datos
     */

    fun fetchSuperHeroes(){
        val email = auth.currentUser?.email

        firestore.collection("SuperHeroes")
            .whereEqualTo("emailUser",email.toString())
            .addSnapshotListener{querySnapshot, error->
                if(error != null){
                    return@addSnapshotListener
                }
                val superHeroes = mutableListOf<SuperHeroState>()
                if(querySnapshot != null){
                    for(superHero in querySnapshot){
                        val cardSuperHero = superHero.toObject(SuperHeroState::class.java).copy()
                        superHeroes.add(cardSuperHero)
                    }
                }
                _superHeroDeckPlayer.value = superHeroes
            }
    }


    /**
     * elimina el superHeroe
     * @param idSuperHero id del superHeroe que queremos eliminar
     * @param onSuccess que hacemos al eliminar el superHeroe
     */
    fun deleteSuperHero(idSuperHero: String, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                firestore.collection("SuperHeroes")
                    .document(idSuperHero)
                    .delete()
                    .addOnSuccessListener {
                        onSuccess()
                        Log.d("ELIMINAR OK", "Se eliminó el superHeroe correctamente en Firestore")
                    }
                    .addOnFailureListener {
                        Log.d("ERROR AL ELIMINAR", "ERROR al eliminar un superHeroe en Firestore")
                    }
            } catch (e:Exception) {
                Log.d("ERROR BORRAR","Error al eliminar ${e.localizedMessage} ")
            }
        }
    }

    /**
     * Actualiza la consulta de búsqueda actual.
     *
     * @param newQuery La nueva cadena de texto de consulta para la búsqueda.
     */
    fun setQuery(newQuery: String) {
        query.value = newQuery
    }

    /**
     * Establece si la búsqueda está activa o no.
     *
     * @param newActive El nuevo estado booleano que indica si la búsqueda está activa.
     */
    fun setActive(newActive: Boolean) {
        active.value = newActive
    }




}



