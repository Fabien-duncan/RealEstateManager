package com.openclassrooms.realestatemanager.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R
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
    if (!properties.isEmpty()) {
        LazyColumn(contentPadding = PaddingValues(top = 0.dp), modifier = modifier) {

            itemsIndexed(properties) { index, property ->
                PropertyItemCard(
                    currencyViewModel = currencyViewModel,
                    property = property,
                    isSelected = index == selectedIndex,
                    isLargeScreen = isLargeScreen
                ) {
                    onItemClicked.invoke(index)
                }
            }

        }
    }else{
        MissingProperties(modifier = modifier)
    }
}
@Composable
fun MissingProperties(
    modifier: Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.missing_properties),
            contentDescription = "missing properties",
            contentScale = ContentScale.FillWidth
        )
        Text(text = "There are no properties", modifier = modifier)

    }
}