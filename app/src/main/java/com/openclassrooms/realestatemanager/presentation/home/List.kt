package com.openclassrooms.realestatemanager.presentation.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.presentation.navigation.CurrencyViewModel

@Composable
fun HomePropertyList(
    currencyViewModel: CurrencyViewModel,
    properties: List<PropertyModel>,
    modifier: Modifier,
    selectedIndex: Int,
    isLargeScreen: Boolean,
    onItemClicked:(index:Int)-> Unit
){
    LazyColumn(contentPadding = PaddingValues(top = 0.dp), modifier = modifier){

        itemsIndexed(properties){ index, property ->
            PropertyItemCard(
                currencyViewModel = currencyViewModel,
                property = property,
                isSelected = index == selectedIndex,
                isLargeScreen = isLargeScreen
            ){
                onItemClicked.invoke(index)
            }
        }

    }
}
