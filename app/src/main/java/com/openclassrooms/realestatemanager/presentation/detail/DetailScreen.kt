package com.openclassrooms.realestatemanager.presentation.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    propertyId: Long,
    assistedFactory: DetailAssistedFactory,
    onBackPressed:() -> Unit
) {
    println("in Detail Screen and the property id is $propertyId")
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        factory = DetailedViewModelFactory(
            propertyId = propertyId,
            assistedFactory = assistedFactory
        )
    )
    viewModel.updatePropertyById(propertyId)
    val state = viewModel.state

    println("property id is ${state.property?.id}")

    DetailScreenView(modifier = modifier, state = state)

    BackHandler {

        onBackPressed.invoke()
    }

}

@Composable
private fun DetailScreenView(
    modifier: Modifier,
    state: DetailSate,
){
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "Surface Area: ${state.property?.area}")
        Text(text = "Number of Rooms: ${state.property?.rooms}")
        Text(text = "Number of bathrooms ${state.property?.bathrooms}")
        Text(text = "Number of bedrooms: ${state.property?.bedrooms}")
    }

}