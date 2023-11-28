package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.currency_converter.CurrencyRepository
import com.openclassrooms.realestatemanager.enums.CurrencyType
import javax.inject.Inject

class GetPriceInCurrentCurrencyUseCase @Inject constructor(
    private val repository: CurrencyRepository
){
    operator fun invoke(price:Int): String = repository.getPriceInCurrentCurrency(price)
}