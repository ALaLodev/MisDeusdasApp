package com.alalodev.misdeudas.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.alalodev.misdeudas.presentation.extensions.millisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(onDismiss: () -> Unit, onTransactionAdded: (String, String, Long?) -> Unit) {

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()


    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Text(
                            text = "Confirm",
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 8.dp)
                                .clickable {
                                    date = datePickerState.selectedDateMillis.millisToDate()
                                    showDatePicker = false
                                }
                        )
                    },
                    dismissButton = {
                        Text(
                            text = "Cancel",
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 8.dp)
                                .clickable { showDatePicker = false }
                        )
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Add Transaction", fontSize = 24.sp, modifier = Modifier.padding(24.dp))
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text(text = "Concept") })
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text(text = "Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = date,
                    onValueChange = { },
                    enabled = false,
                    placeholder = { Text(text = "Date") },
                    modifier = Modifier.clickable { showDatePicker = true })
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onTransactionAdded(title, amount, datePickerState.selectedDateMillis)}) {
                    Text(text = "Add transaction")
                }
            }
        }
    }

}