package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyAddressUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(addressId: Long):Flow<Address> = repository.getPropertyAddress(addressId)
}