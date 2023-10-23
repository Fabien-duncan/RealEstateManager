

package com.openclassrooms.realestatemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.presentation.create_property.CreatePropertyViewModel
import com.openclassrooms.realestatemanager.presentation.home.HomeScreen
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quantity = Utils.convertDollarToEuro(100)

        setContent {
            RealEstateManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TopBar()
                    //RealEstateApp()
                }
            }
        }
    }
}

@Composable
fun RealEstateApp(modifier: Modifier = Modifier){
    val homeViewModel: HomeViewModel = viewModel()
    val createPropertyViewModel: CreatePropertyViewModel = viewModel()
    val navController = rememberNavController()
    val state by homeViewModel.state.collectAsState()

    HomeScreen(modifier = modifier ,state = state, onPropertyClicked = { println("element $it has been clicked") })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(){
    val homeViewModel: HomeViewModel = viewModel()
    val createPropertyViewModel: CreatePropertyViewModel = viewModel()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state by homeViewModel.state.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Real Estate Manager", color = Color.White, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Go Back",
                            tint = Color.White
                        )

                    }
                },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-8).dp),
                        modifier = Modifier.padding(0.dp)
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Create Property",
                                tint = Color.White
                            )

                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Property",
                                tint = Color.White
                            )

                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )

                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.secondary),
            )
        }
    ) {values ->
        RealEstateApp(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        )

    }
}
@Composable
fun MainText(
    modifier: Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Le premier bien immobilier enregistré vaut ${Utils.convertDollarToEuro(100)}")
    }

}


