package com.openclassrooms.realestatemanager.domain.model

data class PropertyPhotosModel(
    val id:Long,
    val photoPath: String,
    val caption: String?
)