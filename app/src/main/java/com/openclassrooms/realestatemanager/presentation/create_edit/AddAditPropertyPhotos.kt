package com.openclassrooms.realestatemanager.presentation.create_edit

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: PropertyPhotosModel,
    onPhotoChanged:(String, Int) -> Unit,
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


        OutlinedTextField(
            value ="${TextUtils.capitaliseFirstLetter(photo.caption ?: "")}" ,
            textStyle = TextStyle(
                color = Color.White,

                ),
            onValueChange = {
                println("change caption")
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhotoItem(
    modifier: Modifier = Modifier,
    onAddPhotoClicked: () -> Unit //ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>
){

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp)
            .height(160.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable {
                println("adding a photo")
                /*onAddPhotoClicked.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )*/

                onAddPhotoClicked.invoke()
            }
        //.border(width = 2.dp, color = Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.add_image_48),
            contentDescription = "click to add photos",
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        )
        //Text(text = "Add photos", Modifier.fillMaxWidth().align(Alignment.BottomStart), textAlign = TextAlign.Center)

    }
}
@Composable
private fun EmptyPhotoList(){
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(120.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.secondary)
    ){
        Column() {
            Image(
                painter = painterResource(id = R.drawable.missing_image),
                contentDescription = "No Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(4.dp)
            )
            Text(
                text = "There are no Images for this Property",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}