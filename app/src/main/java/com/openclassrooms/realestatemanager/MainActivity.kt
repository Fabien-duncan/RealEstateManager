

package com.openclassrooms.realestatemanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.enums.WindowSizeType
import com.openclassrooms.realestatemanager.presentation.common.rememberWindowSizeClass
import com.openclassrooms.realestatemanager.presentation.create_edit.AddEditViewModel
import com.openclassrooms.realestatemanager.presentation.detail.DetailAssistedFactory
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel
import com.openclassrooms.realestatemanager.presentation.navigation.Navigation
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var assistedFactory: DetailAssistedFactory
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quantity = Utils.convertDollarToEuro(100)
        setContent {
            RealEstateManagerTheme {
                // A surface container using the 'background' color from the theme
                val windowSize = rememberWindowSizeClass()

                RealEstateApp(windowSize = windowSize, onGoToAppSettingsClicked = ::openAppSettings)

                /*Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TopBar()
                    //RealEstateApp()
                }*/
            }
        }
    }
    @Composable
    fun RealEstateApp(modifier: Modifier = Modifier, windowSize:WindowSizeType, onGoToAppSettingsClicked: () -> Unit,){

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



