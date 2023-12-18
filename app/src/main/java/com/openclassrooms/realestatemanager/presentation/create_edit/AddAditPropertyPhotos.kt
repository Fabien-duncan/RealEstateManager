package com.openclassrooms.realestatemanager.presentation.create_edit

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel

/**
 * Composable function used to format and display the Photos of the property
 */
@Composable
fun PhotoItem(
    photo: PropertyPhotosModel,
    onPhotoChanged:(String, Int) -> Unit,
    onRemovePhotoClicked: (Int) -> Unit,
    index: Int
){
    val imageUri = Uri.parse(photo.photoPath)

    val photoDescription =
        if(photo.caption == null) "A photo with no caption"
        else "A photo of ${photo.caption}"
    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp)
            .height(160.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.primary)
    ) {

        AsyncImage(
            model = imageUri,
            contentDescription = photoDescription,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.delete_icon),
            contentDescription = "delete photo",
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.TopEnd)
                .background(Color.DarkGray.copy(alpha = 0.7f))
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable { onRemovePhotoClicked.invoke(index) },
            tint = Color.White
        )

        OutlinedTextField(
            value = TextUtils.capitaliseFirstLetter(photo.caption ?: ""),
            textStyle = TextStyle(
                color = Color.White,

                ),
            onValueChange = {
                onPhotoChanged.invoke(it, index)
            },
            placeholder = { Text(text = "Enter caption", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.5f)),
        )
    }
}

/**
 * The composable to represent the button that allows a user to add a Photo to the property
 */
@Composable
fun AddPhotoItem(
    onAddPhotoClicked: () -> Unit
){

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp)
            .height(160.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable {
                onAddPhotoClicked.invoke()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.add_image_48),
            contentDescription = "click to add photos",
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
        )
    }
}