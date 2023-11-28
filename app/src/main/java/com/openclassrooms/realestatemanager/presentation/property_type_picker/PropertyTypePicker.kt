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
import com.openclassrooms.realestatemanager.presentation.create_edit.AddEditViewModel
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel

@Composable
fun PropertyTypePicker(
    onChangedTypePicker: (PropertyType) -> Unit,
    propertyType: PropertyType?
){
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var isTypePickerExpanded by remember { mutableStateOf(false) }
    val icon = if (isTypePickerExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    OutlinedTextField(
        value = propertyType?.name ?: PropertyType.HOUSE.name,
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
            /*.border(border = BorderStroke(width = 1.dp, color = Color.DarkGray))*/
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
    }
}