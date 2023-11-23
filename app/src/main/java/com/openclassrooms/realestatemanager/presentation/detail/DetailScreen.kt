package com.openclassrooms.realestatemanager.presentation.detail

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.realestatemanager.common.utils.TextUtils

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    propertyId: Long,
    assistedFactory: DetailAssistedFactory,
    isLargeView:Boolean,
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
    val mapImageLink = viewModel.mapImageLink

    println("property id is ${state.property?.id}")

    DetailScreenView(modifier = modifier, state = state, isLargeView = isLargeView, mapImageLink = mapImageLink)

    BackHandler {
        onBackPressed.invoke()
    }

}

@Composable
private fun DetailScreenView(
    modifier: Modifier,
    state: DetailSate,
    isLargeView:Boolean,
    mapImageLink:String
){
    val scrollState = rememberScrollState()
    var photos = state.property?.photos
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(modifier = modifier
        .padding(bottom = 0.dp)
        .fillMaxHeight()
        .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween) {

        Text(
            text = "Media",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp)
        )
        if (photos != null) {
            if(!photos.isEmpty()) {
                LazyRow(modifier = Modifier.padding(4.dp)) {
                    itemsIndexed(photos) { index, photo ->
                        PhotoItem(photo = photo)
                    }
                }
            }else{
                EmptyPhotoList()
            }
        }

        state.property?.let {
            Text(
                text = "${TextUtils.capitaliseFirstLetter(state.property.type.name)} Description",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = it.description,
                modifier = Modifier
                    .fillMaxWidth()
                    //.heightIn(max = 150.dp)
                    .padding(8.dp)
                    /*.verticalScroll(scrollState)*/,
                color = Color.DarkGray,
            )
        }
        if (!isPortrait){
            Row{
                BaseDetails(
                    state = state,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(8.dp),
                    isLargeView = true
                )

                state.property?.let {
                    AddressDetail(
                        address = it.address,
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        isLargeView = true,
                        mapImageLink = mapImageLink
                    )
                }
            }
        }
        else{
            BaseDetails(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                isLargeView = false
            )

            state.property?.let {
                AddressDetail(
                    address = it.address,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Green),
                    isLargeView = false,
                    mapImageLink = mapImageLink
                )
            }
        }
        ExtraDetails(
            state = state,
            modifier = Modifier,
            isLargeView = isLargeView,
            isPortrait = isPortrait
        )
    }
}