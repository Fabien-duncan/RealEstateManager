package com.openclassrooms.realestatemanager.presentation.create_edit

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.util.*

/**
 * The composable that deals with the display of the date picker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    openDialog: MutableState<Boolean>,
    onSoldDateChanged: (Date) -> Unit,
) {
    val snackState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackState, Modifier)
    val datePickerState = rememberDatePickerState()


    if (openDialog.value) {

        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        datePickerState.selectedDateMillis?.let { Date(it) }
                            ?.let {
                                onSoldDateChanged.invoke(it)
                            }
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}