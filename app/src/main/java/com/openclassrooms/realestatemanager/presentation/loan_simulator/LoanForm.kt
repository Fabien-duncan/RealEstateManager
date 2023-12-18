package com.openclassrooms.realestatemanager.presentation.loan_simulator

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.presentation.navigation.CurrencyViewModel

/**
 * Composable that deals with the display of the Loan Pop up form
 */
@Composable
fun LoanForm(
    loanCalculatorViewModel: LoanCalculatorViewModel,
    loanAmount: Double = 0.0,
){
    val currencyViewModel: CurrencyViewModel = viewModel()

    if (loanAmount > 0) loanCalculatorViewModel.setLoanAmount(currencyViewModel.getPriceInCurrentCurrency(loanAmount))

    val state = loanCalculatorViewModel.state
    val loan = loanCalculatorViewModel.monthlyPayment

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
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(8.dp)
        )
        LoanInput(
            label = "Loan Amount",
            value = state.loanAmount.toString(),
            onValueChange = { loanCalculatorViewModel.onLoanAmountChanged(it)},
            keyboardType = KeyboardType.Number,
            isInputValid = { loanCalculatorViewModel.checkDoubleAmountIsValid(state.loanAmount) },
            currencyType = currencyViewModel.currentCurrency,
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
            modifier = Modifier.padding(8.dp),
            monthlyPayment = loan,
            loanInterest =
                if (state.loanAmount != null && loan != null && state.loanTerm != null) {
                    (state.loanTerm * loan) - state.loanAmount
                }else{
                    null
                }
        )
        LoanButtons(
            loanCalculatorViewModel = loanCalculatorViewModel
        )
    }
}

/**
 * Composable for the different Inputs of the Loan
 */
@Composable
fun LoanInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isInputValid: () -> Boolean,
    keyboardType: KeyboardType,
    currencyType: CurrencyType? = null,
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = if(value == "null") "" else value,
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
            isError = !isInputValid.invoke(),
            leadingIcon = {
                if (currencyType != null){
                    Image(
                        painter = when (currencyType) {
                            CurrencyType.Dollar -> painterResource(id = R.drawable.dollar_image)
                            CurrencyType.Euro -> painterResource(id = R.drawable.euro_image)
                        },
                        contentDescription = "dollar",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }
        )
    }
}

/**
 * Composable for the Loan button to calculate the loan
 */
@Composable
fun LoanButtons(
    loanCalculatorViewModel: LoanCalculatorViewModel,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { loanCalculatorViewModel.clearLoanState() }
        ) {
            Text(text = "Clear")
        }
        Button(
            onClick = { loanCalculatorViewModel.calculateLoan() },
            enabled = loanCalculatorViewModel.isFormValid
        ) {
            Text(text = "Calculate")
        }
    }
    if (!loanCalculatorViewModel.isFormValid){
        Text(
            text = "You need to fill in all the required fields",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.error,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

/**
 * Composable for displaying the output of the loan
 */
@Composable
fun LoanOutput(
    modifier: Modifier,
    monthlyPayment: Double?,
    loanInterest: Double?,
){
    val monthlyPaymentFormatted =
        if (monthlyPayment!=null){
            String.format("%.2f", monthlyPayment).toDouble()
        }else{
            ""
        }
    val loanInterestFormatted =
        if (loanInterest!=null){
            String.format("%.2f", loanInterest).toDouble()
        }else{
            ""
        }
    Row(modifier = modifier) {
        Text(text = "Monthly amount: $monthlyPaymentFormatted")
    }
    Row(modifier = modifier)  {
        Text(text = "Interest Total: $loanInterestFormatted")
    }
}