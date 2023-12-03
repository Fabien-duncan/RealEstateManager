package com.openclassrooms.realestatemanager.presentation.loan_simulator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoanForm(
    loanCalculatorViewModel: LoanCalculatorViewModel,
    modifier: Modifier = Modifier,
    loanAmount: Double = 0.0,
){
    if (loanAmount > 0) loanCalculatorViewModel.setLoanAmount(loanAmount)

    val state = loanCalculatorViewModel.state

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            text = "Loan Simulator",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp)
        )
        LoanInput(
            label = "Loan Amount",
            value = state.loanAmount.toString(),
            onValueChange = { loanCalculatorViewModel.onLoanAmountChanged(it)},
            keyboardType = KeyboardType.Number,
            isInputValid = { loanCalculatorViewModel.checkDoubleAmountIsValid(state.loanAmount) }
        )
        LoanInput(
            label = "Interest Rate",
            value = state.interestRate.toString(),
            onValueChange = { loanCalculatorViewModel.onInterestRateChanged(it)},
            keyboardType = KeyboardType.Decimal,
            isInputValid = { loanCalculatorViewModel.checkDoubleAmountIsValid(state.interestRate) }
        )
        LoanInput(
            label = "Number of months",
            value = state.loanTerm.toString(),
            onValueChange = { loanCalculatorViewModel.onLoanTermChanged(it) },
            keyboardType = KeyboardType.Number,
            isInputValid = { loanCalculatorViewModel.checkIntAmountIsValid(state.loanTerm) }
        )

        LoanOutput(
            modifier = Modifier.padding(8.dp)
        )
        LoanButtons(

        )
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoanInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isInputValid: () -> Boolean,
    keyboardType: KeyboardType
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = if(value.equals("null")) "" else value,
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = label) },
            label = { Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            isError = !isInputValid.invoke()
        )
    }
}
@Composable
fun LoanButtons(

){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { /*TODO*/ }

        ) {
            Text(text = "Clear")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Calculate")
        }

    }
}
@Composable
fun LoanOutput(
    modifier: Modifier,
){
    Row(modifier = modifier) {
        Text(text = "Monthly amount: ")
    }
    Row(modifier = modifier)  {
        Text(text = "Interest Total: ")
    }

}