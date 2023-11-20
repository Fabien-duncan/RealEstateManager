package com.openclassrooms.realestatemanager.presentation.create_edit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.*

@Composable
fun DatePickerButton() {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var isDatePickerVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val density = LocalDensity.current.density.toInt()
    val screenWidth = LocalDensity.current.density.dp.value.toInt()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { isDatePickerVisible = true },
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Select Date")
        }

        if (isDatePickerVisible) {
            Dialog(
                onDismissRequest = { isDatePickerVisible = false },
                content = {
                    DatePicker(
                        selectedDate = selectedDate,
                        onDateChange = { newDate ->
                            selectedDate = newDate
                            isDatePickerVisible = false
                            println("date : ${newDate.time}")
                        },
                        screenWidth = screenWidth,
                        density = density,
                        context = context
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    selectedDate: Calendar,
    onDateChange: (Calendar) -> Unit,
    screenWidth: Int,
    density: Int,
    context: android.content.Context
) {
    var year by remember { mutableStateOf(selectedDate.get(Calendar.YEAR).toString()) }
    var month by remember { mutableStateOf(selectedDate.get(Calendar.MONTH).toString()) }
    var day by remember { mutableStateOf(selectedDate.get(Calendar.DAY_OF_MONTH).toString()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .width(350.dp)
            .height(500.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val datePickerModifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)

        // Year
        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = datePickerModifier
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Month
        OutlinedTextField(
            value = month,
            onValueChange = { month = it},
            label = { Text("Month") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = datePickerModifier
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Day
        OutlinedTextField(
            value = day,
            onValueChange = { day = it },
            label = { Text("Day") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = datePickerModifier
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year.toInt())
                    set(Calendar.MONTH, month.toInt()-1) // Months are 0-indexed in Calendar
                    set(Calendar.DAY_OF_MONTH, day.toInt())
                }
                onDateChange(newDate)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("OK")
        }
    }
}