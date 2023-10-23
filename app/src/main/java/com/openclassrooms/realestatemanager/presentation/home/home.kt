package com.openclassrooms.realestatemanager.presentation.home

import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.domain.model.PropertyModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onPropertyClicked: (Long) -> Unit
){
    when(state.properties){
        is ScreenViewState.Loading -> {
            println("data loading")
            CircularProgressIndicator()
        }

        is ScreenViewState.Success -> {
            val properties = state.properties.data
            println("got data")
            HomePropertyList(properties = properties, modifier = modifier, onPropertyClicked = onPropertyClicked)

        }

        is ScreenViewState.Error -> {
            println("error: $state.properties.message")
            Text(
                text = state.properties.message ?: "Unknown Error",
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }
}

@Composable
private fun HomePropertyList(
    properties: List<PropertyModel>,
    modifier: Modifier,
    onPropertyClicked:(Long) -> Unit
){
    LazyColumn(contentPadding = PaddingValues(top = 0.dp), modifier = modifier){

        itemsIndexed(properties){ index, property ->
            PropertyItem(property = property, onPropertyClicked = onPropertyClicked)
        }

    }
}

@Composable
private fun PropertyItem(
    property: PropertyModel,
    onPropertyClicked:(Long) -> Unit
){
    //val imagePath = Environment.getExternalStorageDirectory().path + "media/external/images/media/IMG_20231020_143821/jpg"
    val imagePath = "https://images.pexels.com/photos/268533/pexels-photo-268533.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
    val imageUri = Uri.parse(imagePath)

    Column  (
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                onPropertyClicked.invoke(property.id)
            },
    ) {
            Row() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f / 3f)
                        .fillMaxHeight()
                ) {

                    //Text(text = "Some Txt")
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "A photo",
                        contentScale = ContentScale.FillHeight
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,


                    ) {
                    Text(
                        text = TextUtils.capitaliseFirstLetter(property.type.toString()),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = property.address.street,
                        color = Color.LightGray,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "\$${property.price}",
                        color = Color(0xFFFE4080),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
    }
