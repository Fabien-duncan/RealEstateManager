package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.enums.ScreenType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currencyViewModel: CurrencyViewModel,
    currencyType: CurrencyType,
    screenType: ScreenType,
    onSearchedPressed: () -> Unit,
    onBackArrowPressed: (Boolean) -> Unit,
    onEditPressed: () -> Unit,
    onLoanCalculatorPressed: () -> Unit,
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
                            onLoanCalculatorPressed.invoke()
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Simulate Loan")
                                Image(
                                    painter = painterResource(id = R.drawable.loan_image),
                                    contentDescription = "loan"
                                )
                            }
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
        colors = TopAppBarDefaults.topAppBarColors(
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