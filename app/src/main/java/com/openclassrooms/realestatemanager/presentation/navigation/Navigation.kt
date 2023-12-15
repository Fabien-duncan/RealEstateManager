package com.openclassrooms.realestatemanager.presentation.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.openclassrooms.realestatemanager.presentation.home.MissingProperties
import com.openclassrooms.realestatemanager.presentation.loan_simulator.LoanCalculatorViewModel
import com.openclassrooms.realestatemanager.presentation.loan_simulator.LoanForm

/**
 * Composable used to manage the navigation between all the different screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    windowSize: WindowSizeType,
    modifier: Modifier = Modifier,
    assistedFactory: DetailAssistedFactory,
    onGoToAppSettingsClicked: () -> Unit,
) {
    val homeViewModel: HomeViewModel = viewModel()
    val addEditViewModel: AddEditViewModel = viewModel()
    val currencyViewModel: CurrencyViewModel = viewModel()
    val filterViewModel: FilterViewModel = viewModel()
    val loanCalculatorViewModel: LoanCalculatorViewModel = viewModel()


    val state by homeViewModel.state.collectAsState()
    val isExpanded = windowSize == WindowSizeType.Expanded
    val currentCurrency = currencyViewModel.currentCurrency
    val homeScreenType = getScreenType(isExpanded = isExpanded, isDetailOpened = homeViewModel.isItemOpened, isAddOpened = homeViewModel.isAddOpened || homeViewModel.isEditOpened)

    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopBar(
                currencyViewModel = currencyViewModel,
                currencyType = currentCurrency,
                screenType = homeScreenType,
                onBackArrowPressed = {
                    homeViewModel.isItemOpened = it
                    homeViewModel.isAddOpened = false
                    homeViewModel.isEditOpened = false

                },
                onAddPressed = {
                    homeViewModel.isAddOpened = true
                    homeViewModel.isItemOpened = false
                    homeViewModel.isEditOpened = false
                    addEditViewModel.resetState()
                },
                onEditPressed = {
                    homeViewModel.isEditOpened = true
                    homeViewModel.isItemOpened = false
                    addEditViewModel.resetState()
                },
                onSearchedPressed = {
                    showBottomSheet = true
                },
                onLoanCalculatorPressed = {
                    homeViewModel.isLoanPopUpOpened = true
                }

            )
        }
    ) { paddingValues ->
        when (homeScreenType) {
            ScreenType.List -> {
                HomeScreen(
                    state = state,
                    modifier = modifier.padding(paddingValues),
                    onItemClicked = {
                        homeViewModel.propertyIndex = it
                        homeViewModel.currentId = if((state.properties as ScreenViewState.Success<List<PropertyModel>>).data.isEmpty())-1L else (state.properties as ScreenViewState.Success<List<PropertyModel>>).data[homeViewModel.propertyIndex].id
                        homeViewModel.isItemOpened = true
                    },
                    isLargeScreen = false,
                    viewModel = homeViewModel,
                    currencyViewModel = currencyViewModel,
                    onGoToAppSettingsClicked = onGoToAppSettingsClicked
                )
            }

            ScreenType.Detail -> {
                LaunchDetailScreenFromState(
                    loanCalculatorViewModel = loanCalculatorViewModel,
                    state = state,
                    modifier = modifier.padding(paddingValues),
                    assistedFactory = assistedFactory,
                    isLargeView = false,
                    propertyId = homeViewModel.currentId,
                ) {
                    homeViewModel.isItemOpened = false
                }
            }

            ScreenType.ListWithDetail -> {
                ListAndDetailScreen(
                    loanCalculatorViewModel = loanCalculatorViewModel,
                    state = state,
                    onItemClicked = {
                        homeViewModel.propertyIndex = it
                        homeViewModel.currentId = if((state.properties as ScreenViewState.Success<List<PropertyModel>>).data.isEmpty())-1L else (state.properties as ScreenViewState.Success<List<PropertyModel>>).data[homeViewModel.propertyIndex].id
                    },
                    assistedFactory = assistedFactory,
                    modifier = modifier.padding(paddingValues),
                    index = homeViewModel.propertyIndex,
                    id = homeViewModel.currentId,
                    viewModel = homeViewModel,
                    currencyViewModel = currencyViewModel,
                    onGoToAppSettingsClicked = onGoToAppSettingsClicked,

                )
            }

            ScreenType.AddEdit ->{
                val propertiesListSize = if (state.properties is ScreenViewState.Success) (state.properties as ScreenViewState.Success<List<PropertyModel>>).data.size else 0
                println("AddEditPage")
                AddEditScreen(
                    currencyViewModel = currencyViewModel,
                    propertyId = if(homeViewModel.isEditOpened) homeViewModel.currentId else -1L,
                    addEditViewModel = addEditViewModel,
                    modifier = modifier.padding(paddingValues),
                    onCreatedClicked = { newId->
                        homeViewModel.currentId = newId
                        homeViewModel.propertyIndex = propertiesListSize-1
                        homeViewModel.isItemOpened = true
                        homeViewModel.isAddOpened = false
                        homeViewModel.isEditOpened = false
                    },
                ) {
                    homeViewModel.isAddOpened = false
                }
            }
        }

        if (showBottomSheet){
            val properties = if(homeViewModel.currentId == -1L) listOf() else (state.properties as ScreenViewState.Success<List<PropertyModel>>).data
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }) {

                BottomSheetFilter(
                    filterViewModel = filterViewModel,
                    homeViewModel = homeViewModel,
                    currencyViewModel = currencyViewModel,
                    onCloseSheet = {
                        showBottomSheet = false
                        homeViewModel.currentId = if(properties.isEmpty()) -1L else properties[0].id
                    },
                )
            }
        }
        if (homeViewModel.isLoanPopUpOpened){
            Dialog(
                onDismissRequest = {
                    homeViewModel.isLoanPopUpOpened = false
                },
            ) {
                LoanForm(loanCalculatorViewModel = loanCalculatorViewModel)
            }

        }

    }

}

/**
 * Composable to launch the List and detail screen for a table screen
 */
@Composable
private fun ListAndDetailScreen(
    loanCalculatorViewModel:LoanCalculatorViewModel,
    modifier: Modifier = Modifier,
    state: HomeState,
    onItemClicked:(index:Int) -> Unit,
    assistedFactory: DetailAssistedFactory,
    index: Int,
    id:Long,
    viewModel: HomeViewModel,
    onGoToAppSettingsClicked: () -> Unit,
    currencyViewModel: CurrencyViewModel
){
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = modifier.weight(if (isPortrait)0.7F else 0.4f)){
            HomeScreen(
                state = state,
                onItemClicked = onItemClicked,
                selectedIndex = index,
                isLargeScreen = true,
                viewModel = viewModel,
                currencyViewModel = currencyViewModel,
                onGoToAppSettingsClicked = onGoToAppSettingsClicked
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
                loanCalculatorViewModel = loanCalculatorViewModel,
                state = state,
                assistedFactory = assistedFactory,
                isLargeView = true,
                propertyId = id
            ) {

            }
        }

    }

}

/**
 * Composable for retrieving the screen type in order to adapt the UI for the user
 */
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

/**
 * Composable for launching the Detail screen depending on the state
 * an error screen is launched if no property are retrieved
 */
@Composable
fun LaunchDetailScreenFromState(
    loanCalculatorViewModel: LoanCalculatorViewModel,
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
                loanCalculatorViewModel = loanCalculatorViewModel,
                propertyId = propertyId,
                assistedFactory = assistedFactory,
                modifier = modifier,
                isLargeView = isLargeView,
                onBackPressed = onBackPressed
            )

        }
        is ScreenViewState.Error -> {
            MissingProperties(modifier = modifier, message = "No property")
        }
    }
}

