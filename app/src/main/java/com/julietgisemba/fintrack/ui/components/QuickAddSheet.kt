package com.julietgisemba.fintrack.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.julietgisemba.fintrack.model.*
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddSheet(
    type: QuickAddType,
    onSaveTransaction: (TransactionEntity) -> Unit = {},
    onSaveGoal: (Goal) -> Unit = {},
    onSaveBudget: (Budget) -> Unit = {},
    onCancel: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf("") }
    var limit by remember { mutableStateOf("") }
    var spent by remember { mutableStateOf("") }

    // Toggle for transaction type
    var isIncome by remember { mutableStateOf(true) }

    // Budget fields
    var budgetType by remember { mutableStateOf(BudgetType.FIXED) }
    var isRecurring by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<Date?>(null) }
    var endDate by remember { mutableStateOf<Date?>(null) }

    // Goal fields
    var deadline by remember { mutableStateOf<Date?>(null) }
    var isActive by remember { mutableStateOf(true) }

    // For showing DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) deadline = Date(millis)
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    var datePickerTarget by remember { mutableStateOf(DatePickerTarget.NONE) }

    if (datePickerTarget != DatePickerTarget.NONE) {
        DatePickerDialog(
            onDismissRequest = { datePickerTarget = DatePickerTarget.NONE },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        when (datePickerTarget) {
                            DatePickerTarget.DEADLINE -> deadline = Date(millis)
                            DatePickerTarget.START -> startDate = Date(millis)
                            DatePickerTarget.END -> endDate = Date(millis)
                            else -> {}
                        }
                    }
                    datePickerTarget = DatePickerTarget.NONE
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { datePickerTarget = DatePickerTarget.NONE }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = when (type) {
                QuickAddType.INCOME, QuickAddType.EXPENSE -> "Add Transaction"
                QuickAddType.GOAL -> "New Goal"
                QuickAddType.BUDGET -> "New Budget"
            },
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        // --- Transaction fields ---
        if (type == QuickAddType.INCOME || type == QuickAddType.EXPENSE) {
            // Toggle (Income / Expense)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = isIncome,
                    onClick = { isIncome = true },
                    label = { Text("Income") }
                )
                FilterChip(
                    selected = !isIncome,
                    onClick = { isIncome = false },
                    label = { Text("Expense") }
                )
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- Goal fields ---
        if (type == QuickAddType.GOAL) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Goal Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = target,
                onValueChange = { target = it },
                label = { Text("Target Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = saved,
                onValueChange = { saved = it },
                label = { Text("Saved Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = deadline?.toString() ?: "",
                onValueChange = {},
                label = { Text("Deadline") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick date")
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = isActive,
                    onCheckedChange = { isActive = it }
                )
                Text("Active")
            }
        }


        // Budget ----------------------------

        if (type == QuickAddType.BUDGET) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = limit,
                onValueChange = { limit = it },
                label = { Text("Budget Limit") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = spent,
                onValueChange = { spent = it },
                label = { Text("Budget Spent") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
        // Budget Type dropdown
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = budgetType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Budget Type") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                BudgetType.values().forEach { typeOption ->
                    DropdownMenuItem(
                        text = { Text(typeOption.name) },
                        onClick = {
                            budgetType = typeOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(
                checked = isRecurring,
                onCheckedChange = { isRecurring = it }
            )
            Text("Recurring")
        }
        OutlinedTextField(
            value = startDate?.toString() ?: "",
            onValueChange = {},
            label = { Text("Start Date") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { datePickerTarget = DatePickerTarget.START }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Pick date")
                }
            }
        )
        OutlinedTextField(
            value = endDate?.toString() ?: "",
            onValueChange = {},
            label = { Text("End Date") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Pick date")
                }
            }
        )
    }

    Spacer(Modifier.height(16.dp))

        // --- Buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onCancel, ) { Text("Cancel", color = Color(0xFF2C8A5B) )}
            Button(onClick = {
                when (type) {
                    QuickAddType.INCOME, QuickAddType.EXPENSE -> {
                        onSaveTransaction(
                            TransactionEntity(
                                title = title,
                                date = Date(),
                                category = category,
                                amount = amount.toDoubleOrNull() ?: 0.0,
                                isIncome = isIncome,
                                type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                                note = note.ifBlank { null }
                            )
                        )
                    }
                    QuickAddType.GOAL -> {
                        onSaveGoal(
                            Goal(
                                title = title,
                                target = target.toDoubleOrNull() ?: 0.0,
                                saved = saved.toDoubleOrNull() ?: 0.0,
                                deadline = null,
                                isActive = true
                            )
                        )
                    }
                    QuickAddType.BUDGET -> {
                        onSaveBudget(
                            Budget(
                                categoryName = category,
                                limit = limit.toDoubleOrNull() ?: 0.0,
                                spent = spent.toDoubleOrNull() ?: 0.0,
                                type = budgetType,
                                isRecurring = isRecurring,
                                startDate = startDate,
                                endDate = endDate
                            )
                        )
                    }
                }
            },
                colors = ButtonDefaults.buttonColors(Color(0xFF2C8A5B))
            ) {
                Text("Save")
            }
        }
    }
}

enum class DatePickerTarget { NONE, DEADLINE, START, END }


