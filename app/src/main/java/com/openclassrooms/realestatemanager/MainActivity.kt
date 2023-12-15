

package com.openclassrooms.realestatemanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.openclassrooms.realestatemanager.enums.WindowSizeType
import com.openclassrooms.realestatemanager.presentation.common.rememberWindowSizeClass
import com.openclassrooms.realestatemanager.presentation.detail.DetailAssistedFactory
import com.openclassrooms.realestatemanager.presentation.navigation.Navigation
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var assistedFactory: DetailAssistedFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RealEstateManagerTheme {
                val windowSize = rememberWindowSizeClass()

                RealEstateApp(windowSize = windowSize, onGoToAppSettingsClicked = ::openAppSettings)
            }
        }
    }
    @Composable
    fun RealEstateApp(modifier: Modifier = Modifier, windowSize:WindowSizeType, onGoToAppSettingsClicked: () -> Unit){

        Navigation(
            windowSize = windowSize,
            modifier = modifier,
            assistedFactory = assistedFactory,
            onGoToAppSettingsClicked = onGoToAppSettingsClicked,
        )

    }
}
fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}



