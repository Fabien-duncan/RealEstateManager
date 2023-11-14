package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.enums.ScreenType
import com.openclassrooms.realestatemanager.enums.WindowSizeType
import com.openclassrooms.realestatemanager.presentation.create_edit.AddEditScreen
import com.openclassrooms.realestatemanager.presentation.create_edit.AddEditViewModel
import com.openclassrooms.realestatemanager.presentation.detail.DetailAssistedFactory
import com.openclassrooms.realestatemanager.presentation.detail.DetailScreen
import com.openclassrooms.realestatemanager.presentation.home.HomeScreen
import com.openclassrooms.realestatemanager.presentation.home.HomeState
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel
import okhttp3.internal.wait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    windowSize: WindowSizeType,
    modifier: Modifier = Modifier,
    assistedFactory: DetailAssistedFactory,
) {
    val homeViewModel: HomeViewModel = viewModel()
    val addEditViewModel: AddEditViewModel = viewModel()

    val state by homeViewModel.state.collectAsState()

    val isExpanded = windowSize == WindowSizeType.Expanded

    var index by remember{ mutableStateOf(0) }

    var id by remember{ mutableStateOf(1L) }

    var isItemOpened by remember { mutableStateOf(false) }

    var isAddOpened by remember { mutableStateOf(false) }
    var isEditOpened by remember { mutableStateOf(false) }

    var homeScreenType = getScreenType(isExpanded = isExpanded, isDetailOpened = isItemOpened, isAddOpened = isAddOpened || isEditOpened)

    Scaffold(
        topBar = {
            TopBar(
                screenType = homeScreenType,
                onBackArrowPressed = {
                    isItemOpened = it
                    isAddOpened = false
                    isEditOpened = false

                },
                onAddPressed = {
                    isAddOpened = true
                    isItemOpened = false
                    isEditOpened = false
                    addEditViewModel.resetState()
                },
                onEditPressed = {
                    isEditOpened = true
                    isItemOpened = false

                }
            )
        }
    ) {
        when (homeScreenType) {
            ScreenType.List -> {
                HomeScreen(
                    state = state,
                    modifier = modifier.padding(it),
                    onItemClicked = {
                        index = it
                        id = (state.properties as ScreenViewState.Success<List<PropertyModel>>).data[index].id
                        isItemOpened = true
                    },
                    isLargeScreen = false
                )
            }

            ScreenType.Detail -> {
                LaunchDetailScreenFromState(
                    state = state,
                    modifier = modifier.padding(it),
                    assistedFactory = assistedFactory,
                    isLargeView = false,
                    propertyId = id
                ) {
                    isItemOpened = false
                }
            }

            ScreenType.ListWithDetail -> {
                println("tablet mode index is $index")
                ListAndDetailScreen(
                    state = state,
                    onItemClicked = {
                        index = it
                        id = (state.properties as ScreenViewState.Success<List<PropertyModel>>).data[index].id
                    },
                    assistedFactory = assistedFactory,
                    modifier = modifier.padding(it),
                    index = index,
                    id = id
                )
            }

            ScreenType.AddEdit ->{
                val propertiesListSize = if (state.properties is ScreenViewState.Success) (state.properties as ScreenViewState.Success<List<PropertyModel>>).data.size else 0
                println("AddEditPage")
                AddEditScreen(
                    propertyId = if(isEditOpened) id else -1L,
                    isLargeView = isExpanded,
                    addEditViewModel = addEditViewModel,
                    modifier = modifier.padding(it),
                    onCreatedClicked = { newId->
                        id = newId
                        index = propertiesListSize-1
                        isItemOpened = true
                        isAddOpened = false
                        isEditOpened = false
                    },
                ) {
                    isAddOpened = false
                }
            }
        }
    }
}

@Composable
private fun ListAndDetailScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onItemClicked:(index:Int) -> Unit,
    assistedFactory: DetailAssistedFactory,
    index: Int,
    id:Long,
){
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreen(
            state = state,
            onItemClicked = onItemClicked,
            modifier = modifier.width(350.dp),
            selectedIndex = index,
            isLargeScreen = true
        )
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        LaunchDetailScreenFromState(
            state = state,
            modifier = modifier,
            assistedFactory = assistedFactory,
            isLargeView = true,
            propertyId = id
        ) {

        }

    }

}

@Composable
fun getScreenType(
    isExpanded:Boolean,
    isDetailOpened:Boolean,
    isAddOpened:Boolean
): ScreenType = when(isExpanded){
    false -> {
        if(isDetailOpened){
            ScreenType.Detail
        }else if(isAddOpened){
            ScreenType.AddEdit
        }else{
            ScreenType.List
        }
    }
    true -> {
        if (isAddOpened){
            ScreenType.AddEdit
        }else{
            ScreenType.ListWithDetail
        }
    }
}

@Composable
fun LaunchDetailScreenFromState(
    state: HomeState,
    modifier: Modifier,
    assistedFactory: DetailAssistedFactory,
    isLargeView:Boolean,
    propertyId:Long,
    onBackPressed:() -> Unit
){
    when(state.properties){
        is ScreenViewState.Loading -> {
            //println("data loading")
            CircularProgressIndicator()
        }

        is ScreenViewState.Success -> {
            val properties = state.properties.data

            //println("go to data and index is $index")

            DetailScreen(
                propertyId = propertyId,
                assistedFactory = assistedFactory,
                modifier = modifier,
                isLargeView = isLargeView,
                onBackPressed = onBackPressed
            )

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    screenType: ScreenType,
    onBackArrowPressed: (Boolean) -> Unit,
    onEditPressed: () -> Unit,
    onAddPressed:() -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    TopAppBar(
        title = {
            Text(text = "Real Estate Manager", color = Color.White, fontSize = 20.sp)
        },
        navigationIcon = {
            if (screenType == ScreenType.List || screenType == ScreenType.ListWithDetail  ) {
                IconButton(
                    onClick = { println("menu pressed") },
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )

                }
            } else {
                IconButton(
                    onClick = {
                        onBackArrowPressed.invoke(false)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "go back",
                        tint = Color.White
                    )

                }
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy((-8).dp),
                modifier = Modifier.padding(0.dp)
            ) {
                IconButton(onClick = { onAddPressed.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Property",
                        tint = Color.White
                    )

                }
                if(screenType == ScreenType.Detail || screenType == ScreenType.ListWithDetail){
                    IconButton(onClick = { onEditPressed.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Property",
                            tint = Color.White
                        )

                    }
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