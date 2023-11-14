package com.openclassrooms.realestatemanager.domain.model

data class PropertyPhotosModel(
    val id:Long = 0,
    val photoPath: String,
    val caption: String? = null
)