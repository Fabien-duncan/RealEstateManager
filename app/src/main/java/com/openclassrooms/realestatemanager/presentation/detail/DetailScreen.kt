package com.openclassrooms.realestatemanager.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    propertyId: Long,
    assistedFactory: DetailAssistedFactory,
) {
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        factory = DetailedViewModelFactory(
            propertyId = propertyId,
            assistedFactory = assistedFactory
        )
    )
    
    val state = viewModel.state

   DetailScreenView(modifier = modifier, state = state)
    
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