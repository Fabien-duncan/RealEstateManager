package com.openclassrooms.realestatemanager.presentation.property_type_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.openclassrooms.realestatemanager.enums.PropertyType

/**
 * Composable for selecting the Property Type. this is used byt the add/Edit view and the Filter form
 */
@Composable
fun PropertyTypePicker(
    onChangedTypePicker: (PropertyType?) -> Unit,
    propertyType: PropertyType?,
    hasNoTypeChoice:Boolean
){
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var isTypePickerExpanded by remember { mutableStateOf(false) }
    val icon = if (isTypePickerExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    OutlinedTextField(
        value = propertyType?.name ?: "Select Type",
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            },
        trailingIcon = {
            Icon(
                icon, "selection arrow",
                Modifier.clickable { isTypePickerExpanded = !isTypePickerExpanded },
            )
        }
    )
    DropdownMenu(
        expanded = isTypePickerExpanded,
        onDismissRequest = { isTypePickerExpanded = false },
        modifier = Modifier
            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            .background(Color.LightGray)
    ) {
        PropertyType.values().forEach { type ->
            DropdownMenuItem(
                onClick = {
                    isTypePickerExpanded = false
                    onChangedTypePicker.invoke(type)
                },
                text = { Text(text = type.name) }
            )
        }
        if(hasNoTypeChoice){
            DropdownMenuItem(
                onClick = {
                    isTypePickerExpanded = false
                    onChangedTypePicker.invoke(null)
                },
                text = { Text(text = "NO TYPE") }
            )
        }
    }
}