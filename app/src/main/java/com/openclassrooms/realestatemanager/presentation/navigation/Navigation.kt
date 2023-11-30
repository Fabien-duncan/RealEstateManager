package com.openclassrooms.realestatemanager.presentation.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.enums.ScreenType
import com.openclassrooms.realestatemanager.enums.WindowSizeType
import com.openclassrooms.realestatemanager.presentation.create_edit.AddEditScreen
import com.openclassrooms.realestatemanager.presentation.create_edit.AddEditViewModel
import com.openclassrooms.realestatemanager.presentation.detail.DetailAssistedFactory
import com.openclassrooms.realestatemanager.presentation.detail.DetailScreen
import com.openclassrooms.realestatemanager.presentation.home.HomeScreen
import com.openclassrooms.realestatemanager.presentation.home.HomeState
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    windowSize: WindowSizeType,
    modifier: Modifier = Modifier,
    assistedFactory: DetailAssistedFactory,
) {
    val homeViewModel: HomeViewModel = viewModel()
    val addEditViewModel: AddEditViewModel = viewModel()
    val currencyViewModel: CurrencyViewModel = viewModel()
    val filterViewModel: FilterViewModel = viewModel()

    Log.d("navigation", "currency is: ${currencyViewModel.getCurrency()}")

    val state by homeViewModel.state.collectAsState()

    val isExpanded = windowSize == WindowSizeType.Expanded

    var index by remember{ mutableIntStateOf(0) }

    var id by remember{ mutableLongStateOf(
            when(state.properties){
                is ScreenViewState.Error -> 1L
                ScreenViewState.Loading -> 1L
                is ScreenViewState.Success -> {
                        (state.properties as ScreenViewState.Success<List<PropertyModel>>).data[0].id
                }
            }
        )
    }

    var isItemOpened by remember { mutableStateOf(false) }

    var isAddOpened by remember { mutableStateOf(false) }
    var isEditOpened by remember { mutableStateOf(false) }

    val currentCurrency = currencyViewModel.currentCurrency

    var homeScreenType = getScreenType(isExpanded = isExpanded, isDetailOpened = isItemOpened, isAddOpened = isAddOpened || isEditOpened)

    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(state){
        when(state.properties){
            is ScreenViewState.Error -> 1L
            ScreenViewState.Loading -> 1L
            is ScreenViewState.Success -> {
                id = (state.properties as ScreenViewState.Success<List<PropertyModel>>).data[0].id
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                homeViewModel = homeViewModel,
                currencyViewModel = currencyViewModel,
                currencyType = currentCurrency,
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
                    addEditViewModel.resetState()
                },
                onSearchedPressed = {
                    showBottomSheet = true
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
                    isLargeScreen = false,
                    viewModel = homeViewModel,
                    currencyViewModel = currencyViewModel
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
                    id = id,
                    viewModel = homeViewModel,
                    currencyViewModel = currencyViewModel
                )
            }

            ScreenType.AddEdit ->{
                val propertiesListSize = if (state.properties is ScreenViewState.Success) (state.properties as ScreenViewState.Success<List<PropertyModel>>).data.size else 0
                println("AddEditPage")
                AddEditScreen(
                    currencyViewModel = currencyViewModel,
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

        if (showBottomSheet){
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }) {

                BottomSheetFilter(
                    filterViewModel = filterViewModel,
                    homeViewModel = homeViewModel,
                    currencyViewModel = currencyViewModel,
                    onCloseSheet = {
                        showBottomSheet = false
                        id = (state.properties as ScreenViewState.Success<List<PropertyModel>>).data[0].id //needs to wait until the properties has been updated
                    },
                )
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
    viewModel: HomeViewModel,
    currencyViewModel: CurrencyViewModel
){
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = modifier.weight(0.4f)){
            HomeScreen(
                state = state,
                onItemClicked = onItemClicked,
                selectedIndex = index,
                isLargeScreen = true,
                viewModel = viewModel,
                currencyViewModel = currencyViewModel
            )
        }
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Column(modifier = modifier.weight(1f)){
            LaunchDetailScreenFromState(
                state = state,
                assistedFactory = assistedFactory,
                isLargeView = true,
                propertyId = id
            ) {

            }
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
    modifier: Modifier = Modifier,
    assistedFactory: DetailAssistedFactory,
    isLargeView:Boolean,
    propertyId:Long,
    onBackPressed:() -> Unit
){
    when(state.properties){
        is ScreenViewState.Loading -> {
            CircularProgressIndicator()
        }

        is ScreenViewState.Success -> {
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
    homeViewModel: HomeViewModel,
    currencyViewModel: CurrencyViewModel,
    currencyType: CurrencyType,
    screenType: ScreenType,
    onSearchedPressed: () -> Unit,
    onBackArrowPressed: (Boolean) -> Unit,
    onEditPressed: () -> Unit,
    onAddPressed:() -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var expanded by remember { mutableStateOf(false) }
    var selectedMenuItem by remember { mutableStateOf("Item 1") }
    var showCurrencyPopup by remember { mutableStateOf(false) }
    // USA flag emoji
    val usaFlag = "\uD83C\uDDFA\uD83C\uDDF8"
    // European Union flag emoji
    val europeFlag = "\uD83C\uDDEA\uD83C\uDDFA"
    val currentCurrencyText = when(currencyType)
    {
        CurrencyType.Euro -> "EUR $europeFlag"
        CurrencyType.Dollar -> "USD $usaFlag"
    }

    TopAppBar(
        title = {
            Text(text = "Real Estate Manager", color = Color.White, fontSize = 20.sp)
        },
        navigationIcon = {
            if (screenType == ScreenType.List || screenType == ScreenType.ListWithDetail  ) {
                IconButton(
                    onClick = { expanded = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )

                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(200.dp)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            selectedMenuItem = "Item 1"
                            expanded = false
                            showCurrencyPopup = true
                        },
                        text = {
                            Text(text = "Currency: $currentCurrencyText")
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            selectedMenuItem = "Item 2"
                            expanded = false
                        },
                        text = {
                            Text(text = "item2")
                        }
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
                if (screenType == ScreenType.ListWithDetail || screenType == ScreenType.List) {
                    IconButton(onClick = { onSearchedPressed.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )

                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
    )
    if (showCurrencyPopup) {
        CurrencyPopup(
            currencyViewModel = currencyViewModel,
            currencyType = currencyType,
            onDismiss = { showCurrencyPopup = false }
        )
    }
}
@Composable
fun CurrencyPopup(
    currencyViewModel: CurrencyViewModel,
    currencyType: CurrencyType,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text("Select Currency")
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currencyType == CurrencyType.Dollar,
                        onClick = {
                            currencyViewModel.setCurrency(CurrencyType.Dollar)
                            onDismiss.invoke()
                        }
                    )
                    Text("USD")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currencyType == CurrencyType.Euro,
                        onClick = {
                            currencyViewModel.setCurrency(CurrencyType.Euro)
                            onDismiss.invoke()
                        }
                    )
                    Text("EUR")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    }
}