package com.example.listycity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme


@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    onDeleteCity: (City) -> Unit,
    modifier: Modifier = Modifier
) {

    var newCityName by rememberSaveable {
        mutableStateOf("")
    }

    var newProvinceName by rememberSaveable {
        mutableStateOf("")
    }

    var showAddCityFields by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedCity by remember {
        mutableStateOf<City?>(null)
    }

    var editedCityName by rememberSaveable {
        mutableStateOf("")
    }

    var editedProvinceName by rememberSaveable {
        mutableStateOf("")
    }


    Column(
        modifier = modifier.fillMaxSize()
    ) {

        // + BUTTON
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            FloatingActionButton(
                modifier = Modifier.padding(16.dp),

                onClick = {

                    showAddCityFields = !showAddCityFields

                    if (showAddCityFields) {
                        selectedCity = null
                        editedCityName = ""
                        editedProvinceName = ""
                    }
                }
            ) {
                Text("+")
            }
        }


        // ADD CITY SECTION
        if (showAddCityFields) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = newCityName,

                        onValueChange = {
                            newCityName = it
                        },

                        label = {
                            Text("City")
                        },

                        modifier = Modifier.weight(1f)
                    )


                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )


                    OutlinedTextField(
                        value = newProvinceName,

                        onValueChange = {
                            newProvinceName = it
                        },

                        label = {
                            Text("Province")
                        },

                        modifier = Modifier.weight(1f)
                    )
                }


                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),

                    onClick = {

                        if (
                            newCityName.isNotBlank() &&
                            newProvinceName.isNotBlank()
                        ) {

                            onAddCity(
                                City(
                                    name = newCityName,
                                    province = newProvinceName
                                )
                            )

                            newCityName = ""
                            newProvinceName = ""
                            showAddCityFields = false
                        }
                    }
                ) {
                    Text("ADD CITY")
                }
            }
        }


        // SELECTED CITY:
        // UPDATE + DELETE
        if (selectedCity != null) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                // EDIT TEXT FIELDS
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = editedCityName,

                        onValueChange = {
                            editedCityName = it
                        },

                        label = {
                            Text("Updated City")
                        },

                        modifier = Modifier.weight(1f)
                    )


                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )


                    OutlinedTextField(
                        value = editedProvinceName,

                        onValueChange = {
                            editedProvinceName = it
                        },

                        label = {
                            Text("Updated Province")
                        },

                        modifier = Modifier.weight(1f)
                    )
                }


                // UPDATE + DELETE BUTTONS
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {

                    Button(
                        onClick = {

                            val cityToUpdate = selectedCity

                            if (
                                cityToUpdate != null &&
                                editedCityName.isNotBlank() &&
                                editedProvinceName.isNotBlank()
                            ) {

                                onUpdateCity(
                                    cityToUpdate,

                                    City(
                                        name = editedCityName,
                                        province = editedProvinceName
                                    )
                                )

                                selectedCity = null
                                editedCityName = ""
                                editedProvinceName = ""
                            }
                        },

                        modifier = Modifier.weight(1f)
                    ) {
                        Text("UPDATE CITY")
                    }


                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )


                    Button(
                        onClick = {

                            val cityToDelete = selectedCity

                            if (cityToDelete != null) {

                                onDeleteCity(cityToDelete)

                                selectedCity = null
                                editedCityName = ""
                                editedProvinceName = ""
                            }
                        },

                        modifier = Modifier.weight(1f)
                    ) {
                        Text("DELETE CITY")
                    }
                }
            }
        }


        // CITY LIST
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            itemsIndexed(cities) { index, city ->

                CityRow(
                    city = city,

                    onClick = {

                        // Close Add section
                        showAddCityFields = false

                        newCityName = ""
                        newProvinceName = ""

                        // Select clicked city
                        selectedCity = city

                        // Put current information
                        // into the edit boxes
                        editedCityName = city.name
                        editedProvinceName = city.province
                    }
                )


                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}


@Composable
fun CityRow(
    city: City,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
    ) {

        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )


        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {

    ListyCityTheme {

        CityListScreen(

            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),

            onAddCity = {},

            onUpdateCity = { _, _ -> },

            onDeleteCity = {}
        )
    }
}