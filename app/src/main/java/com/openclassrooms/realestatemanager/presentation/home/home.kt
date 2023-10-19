package com.openclassrooms.realestatemanager.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onPropertyClicked: (Long) -> Unit
){
    when(state.properties){
        is ScreenViewState.Loading -> {
            println("data loading")
            CircularProgressIndicator()
        }

        is ScreenViewState.Success -> {
            val properties = state.properties.data
            println("got data")
            HomePropertyList(properties = properties, modifier = modifier, onPropertyClicked = onPropertyClicked)

        }

        is ScreenViewState.Error -> {
            println("error: $state.properties.message")
            Text(
                text = state.properties.message ?: "Unknown Error",
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }
}

@Composable
private fun HomePropertyList(
    properties: List<PropertyWithAllDetails>,
    modifier: Modifier,
    onPropertyClicked:(Long) -> Unit
){
    LazyColumn(contentPadding = PaddingValues(top = 16.dp), modifier = modifier){

        itemsIndexed(properties){ index, property ->
            PropertyItem(property = property, onPropertyClicked = onPropertyClicked)
        }

    }
}

@Composable
private fun PropertyItem(
    property: PropertyWithAllDetails,
    onPropertyClicked:(Long) -> Unit
){
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onPropertyClicked.invoke(property.property.id)
            },
    ) {
        Column(
        ) {
            Text(
                text = property.property.type.toString(),
                modifier = Modifier
                    .padding(1.dp)
            )
            Text(
                text = property.address.street,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(1.dp)
            )
            Text(
                text = "\$${property.property.price}",
                color = Color(0xFFFE4080),
                modifier = Modifier
                    .padding(1.dp)
            )
        }
    }

}