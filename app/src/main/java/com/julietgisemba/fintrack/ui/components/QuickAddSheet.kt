package com.julietgisemba.fintrack.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
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
    onCancel: () -> Unit,
    editingTransaction: TransactionEntity? = null,
    editingGoal: Goal? = null,
    editingBudget: Budget? = null
) {
    var amount by remember(editingTransaction, editingBudget) {
        mutableStateOf(
            editingTransaction?.amount?.toString() ?: editingBudget?.limit?.toString() ?: ""
        )
    }

    var title by remember(editingTransaction, editingGoal) {
        mutableStateOf(
            editingTransaction?.title ?: editingGoal?.title ?: ""
        )
    }

    var category by remember(editingTransaction, editingBudget) {
        mutableStateOf(
            editingTransaction?.category ?: editingBudget?.categoryName ?: ""
        )
    }

    var note by remember(editingTransaction) {
        mutableStateOf(
            editingTransaction?.note ?: ""
        )
    }

    var target by remember { mutableStateOf(editingGoal?.target?.toString() ?: "") }
    var saved by remember { mutableStateOf(editingGoal?.saved?.toString() ?: "") }

    var limit by remember { mutableStateOf(editingBudget?.limit?.toString() ?: "") }
    var spent by remember { mutableStateOf(editingBudget?.spent?.toString() ?: "") }

    // Toggle for transaction type
    var isIncome by remember { mutableStateOf(editingTransaction?.isIncome ?: true) }

    // Budget fields
    var budgetType by remember { mutableStateOf(editingBudget?.type ?: BudgetType.FIXED) }
    var isRecurring by remember { mutableStateOf(editingBudget?.isRecurring ?: false) }
    var startDate by remember { mutableStateOf(editingBudget?.startDate) }
    var endDate by remember { mutableStateOf(editingBudget?.endDate) }

    // Goal fields
    var deadline by remember { mutableStateOf(editingGoal?.deadline) }
    var isActive by remember { mutableStateOf(editingGoal?.isActive ?: true) }

    // For showing DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
            TextButton(onClick = {
                val millis = datePickerState.selectedDateMillis
                if (millis != null) deadline = Date(millis)
                showDatePicker = false
            }) { Text("OK") }
        }, dismissButton = {
            TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
        }) {
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
                TextButton(onClick = {
                    datePickerTarget = DatePickerTarget.NONE
                }) { Text("Cancel") }
            }) {
            DatePicker(state = datePickerState)
        }
    }

    val headerText = when {
        editingTransaction != null -> "Edit Transaction"
        editingGoal != null -> "Edit Goal"
        editingBudget != null -> "Edit Budget"
        else -> when (type) {
            QuickAddType.INCOME, QuickAddType.EXPENSE -> "Add Transaction"
            QuickAddType.GOAL -> "New Goal"
            QuickAddType.BUDGET -> "New Budget"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = headerText, style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        // --- Transaction fields ---
        if (type == QuickAddType.INCOME || type == QuickAddType.EXPENSE || editingTransaction != null) {
            // Toggle (Income / Expense)
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = isIncome,
                    onClick = { isIncome = true },
                    label = { Text("Income") })
                FilterChip(
                    selected = !isIncome,
                    onClick = { isIncome = false },
                    label = { Text("Expense") })
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
        if (type == QuickAddType.GOAL || editingGoal != null) {
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
                })

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = isActive, onCheckedChange = { isActive = it })
                Text("Active")
            }
        }

        // Budget ----------------------------

        if (type == QuickAddType.BUDGET || editingBudget != null) {
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
                expanded = expanded, onExpandedChange = { expanded = !expanded }) {
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
                    expanded = expanded, onDismissRequest = { expanded = false }) {
                    BudgetType.values().forEach { typeOption ->
                        DropdownMenuItem(text = { Text(typeOption.name) }, onClick = {
                            budgetType = typeOption
                            expanded = false
                        })
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = isRecurring, onCheckedChange = { isRecurring = it })
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
                })
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
                })
        }

        Spacer(Modifier.height(16.dp))

        // --- Buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onCancel) { Text("Cancel", color = Color(0xFF2C8A5B)) }
            Button(
                onClick = {
                    when {
                        editingTransaction != null -> onSaveTransaction(
                            editingTransaction.copy(
                                title = title,
                                category = category,
                                amount = amount.toDoubleOrNull() ?: 0.0,
                                isIncome = isIncome,
                                note = note
                            )
                        )

                        editingGoal != null -> onSaveGoal(
                            editingGoal.copy(
                                title = title,
                                target = target.toDoubleOrNull() ?: 0.0,
                                saved = saved.toDoubleOrNull() ?: 0.0,
                                deadline = deadline,
                                isActive = isActive
                            )
                        )

                        editingBudget != null -> onSaveBudget(
                            editingBudget.copy(
                                categoryName = category,
                                limit = limit.toDoubleOrNull() ?: 0.0,
                                spent = spent.toDoubleOrNull() ?: 0.0,
                                type = budgetType,
                                isRecurring = isRecurring,
                                startDate = startDate,
                                endDate = endDate
                            )
                        )

                        type == QuickAddType.INCOME || type == QuickAddType.EXPENSE -> {
                            onSaveTransaction(
                                TransactionEntity(
                                title = title,
                                date = Date(),
                                category = category,
                                amount = amount.toDoubleOrNull() ?: 0.0,
                                isIncome = isIncome,
                                type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                                note = note.ifBlank { null }))
                        }

                        type == QuickAddType.GOAL -> {
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

                        type == QuickAddType.BUDGET -> {
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
                }, colors = ButtonDefaults.buttonColors(Color(0xFF2C8A5B))
            ) {
                Text("Save")
            }
        }
    }
}

enum class DatePickerTarget { NONE, DEADLINE, START, END }


