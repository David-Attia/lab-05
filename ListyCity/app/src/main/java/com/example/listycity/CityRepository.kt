package com.example.listycity

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {

    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")

    private val _cities = mutableStateListOf<City>()

    init {
        citiesRef.addSnapshotListener { querySnapshot, error ->

            if (error != null) {
                Log.e("CityRepository", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (querySnapshot != null) {

                _cities.clear()

                for (doc in querySnapshot) {
                    val city = doc.toObject(City::class.java)
                    _cities.add(city)
                }
            }
        }
    }

    val cities: List<City>
        get() = _cities


    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
    }


    fun updateCity(oldCity: City, updatedCity: City) {

        if (oldCity.name != updatedCity.name) {
            citiesRef.document(oldCity.name).delete()
        }

        citiesRef.document(updatedCity.name).set(updatedCity)
    }


    fun deleteCity(city: City) {
        citiesRef.document(city.name).delete()
    }
}