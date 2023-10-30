package com.openclassrooms.realestatemanager.presentation.home

import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    selectedIndex:Int = -1,
    onItemClicked:(index:Int) -> Unit
){
    when(state.properties){
        is ScreenViewState.Loading -> {
            println("data loading")
            CircularProgressIndicator()
        }

        is ScreenViewState.Success -> {
            val properties = state.properties.data
            println("got data")
            HomePropertyList(properties = properties, modifier = modifier, onItemClicked = onItemClicked, selectedIndex = selectedIndex)

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
    selectedIndex: Int,
    onItemClicked:(index:Int)-> Unit
){
    LazyColumn(contentPadding = PaddingValues(top = 0.dp), modifier = modifier){

        itemsIndexed(properties){ index, property ->
            PropertyItem(property = property, isSelected = index == selectedIndex){
                onItemClicked.invoke(index)
            }
        }

    }
}

@Composable
private fun PropertyItem(
    property: PropertyModel,
    isSelected: Boolean,
    onItemClicked: () -> Unit,

){
    //val imagePath = Environment.getExternalStorageDirectory().path + "media/external/images/media/IMG_20231020_143821/jpg"
    /*val imagePath = "https://images.pexels.com/photos/268533/pexels-photo-268533.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
    val imageUri = Uri.parse(imagePath)*/
    val listOfPhotos = property.photos
    val imageUri = if (!listOfPhotos.isNullOrEmpty()) Uri.parse(listOfPhotos[0].photoPath) else null

    val selectedModifier: Modifier
    val moneyColor :Color
    if(isSelected){
        selectedModifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
        moneyColor = Color.White

    } else{
        selectedModifier = Modifier.background(Color.White)
        moneyColor = MaterialTheme.colorScheme.tertiary
    }

    Column  (
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                onItemClicked.invoke()
            },
    ) {
            Row(
                modifier = selectedModifier
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f / 3f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {

                    //Text(text = "Some Txt")
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "A photo of the property",
                            contentScale = ContentScale.FillHeight
                        )
                    }else{
                        Image(
                            painter = painterResource(id = R.drawable.missing_image),
                            contentDescription = "No Image",
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary)
                                .fillMaxSize(),
                        )
                    }

                    if (property.soldDate != null){
                        Text(
                            text = "Sold!",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .rotate(-45f)
                                .background(
                                    color = Color.Red.copy(alpha = 0.7f),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .width(500.dp)

                        )
                    }
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
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "\$${property.price}",
                        color = moneyColor,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
    }
