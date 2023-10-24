package com.openclassrooms.realestatemanager.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.use_cases.GetPropertyByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    @Assisted private val propertyId: Long
):ViewModel() {
    var state by mutableStateOf(DetailSate(null))
        private set

    private fun getPropertyById() = viewModelScope.launch {
        getPropertyByIdUseCase(propertyId).collectLatest {property ->
            state = state.copy(property = property)
        }

    }

}

data class DetailSate(
    val property: PropertyModel? = null
)