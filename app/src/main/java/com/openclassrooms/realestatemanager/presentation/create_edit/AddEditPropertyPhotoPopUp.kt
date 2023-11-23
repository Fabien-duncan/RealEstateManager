package com.openclassrooms.realestatemanager.presentation.create_edit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import com.openclassrooms.realestatemanager.R

@Composable
fun PhotoSelectPopup(
    changeIsImageSelectedChoice: () -> Unit,
    onAddPhotoClicked: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    context: Context,
    uri: Uri,
    takePicture: ManagedActivityResultLauncher<Uri, Boolean>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
){

    Popup(onDismissRequest = { changeIsImageSelectedChoice.invoke()}, alignment = Alignment.CenterEnd ) {
        Column(){
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    // Handle confirm button click
                    changeIsImageSelectedChoice.invoke()
                    onAddPhotoClicked.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Image(painter = painterResource(id = R.drawable.photo_gallery_24), contentDescription = "from gallery", modifier = Modifier.padding(end = 4.dp))
                Text("from Gallery")

            }
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED)
                    {
                        println("Launching camera ")
                        takePicture.launch(uri)
                    }
                    else
                    {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                    // Handle confirm button click
                    changeIsImageSelectedChoice.invoke()
                }
            ) {
                Image(painter = painterResource(id = R.drawable.photo_camera_24), contentDescription = "from camera", modifier = Modifier.padding(end = 4.dp))
                Text("from Camera")

            }
        }
    }
}