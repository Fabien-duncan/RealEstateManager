package com.openclassrooms.realestatemanager.presentation.create_property

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.use_cases.AddPropertyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CreatePropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase
):ViewModel(){
    init{
        /*viewModelScope.launch {
            initialise()
        }*/
    }
    private suspend fun initialise(){
        /*val tesProperties = getAllProperties()
        withContext(Dispatchers.IO) {
            tesProperties.forEach{ property ->
                addPropertyUseCase(property)
            }
        }*/

    }
    /*private fun getAllProperties(): List<Property>{
        return listOf(
            Property(
                id = 1,
                addressId = 1,
                type = PropertyType.APARTMENT,
                price = 152046.50,
                surfaceArea = 56,
                numRooms = 3,
                description = "very nice house",
                agentName = "Fabien Duncan"
            ),
            Property(
                id = 2,
                addressId = 2,
                type = PropertyType.MANOIR,
                price = 305046.50,
                surfaceArea = 102,
                numRooms = 5,
                description = "near a very nice school",
                agentName = "Fabien Duncan"
            ),
            Property(
                id = 3,
                addressId = 3,
                type = PropertyType.LOFT,
                price = 485046.50,
                surfaceArea = 35,
                numRooms = 2,
                description = "clean house",
                agentName = "Fabien Duncan"
            ),
            Property(
                id = 4,
                addressId = 4,
                type = PropertyType.APARTMENT,
                price = 85433.99,
                surfaceArea = 27,
                numRooms = 2,
                description = "Nice a cosy",
                agentName = "Fabien Duncan"
            ),
        )
    }*/

}
