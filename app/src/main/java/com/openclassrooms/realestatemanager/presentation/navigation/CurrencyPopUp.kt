package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.openclassrooms.realestatemanager.enums.CurrencyType

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