package com.julietgisemba.kipesa.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.julietgisemba.kipesa.model.Budget
import com.julietgisemba.kipesa.model.Goal
import com.julietgisemba.kipesa.model.QuickAddType
import com.julietgisemba.kipesa.model.TransactionEntity
import com.julietgisemba.kipesa.ui.components.ActionButton
import com.julietgisemba.kipesa.ui.components.DashboardCard
import com.julietgisemba.kipesa.ui.components.GoalItem
import com.julietgisemba.kipesa.ui.components.TransactionItem
import com.julietgisemba.kipesa.ui.components.BudgetItem
import com.julietgisemba.kipesa.ui.components.QuickAddSheet
import com.julietgisemba.kipesa.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: FinanceViewModel = hiltViewModel()) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var currentType by remember { mutableStateOf(QuickAddType.EXPENSE) }
    val transactions by viewModel.transactions.collectAsState()
    val budgets by viewModel.budgetList.collectAsState()
    val goals by viewModel.goalList.collectAsState()
    var editingGoal by remember { mutableStateOf<Goal?>(null) }
    var editingBudget by remember { mutableStateOf<Budget?>(null) }
    var editingTransaction by remember { mutableStateOf<TransactionEntity?>(null) }

    val goalSumTarget = goals.sumOf { it.target }
    val progress = if (goalSumTarget > 0) {
        (goals.sumOf { it.saved } / goalSumTarget).toFloat().coerceIn(0f, 1f)
    } else 0f

    val goalText =
        "$" + String.format("%,.0f", goals.sumOf { it.saved }) + " / $" + String.format(
            "%,.0f",
            goalSumTarget
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kipesa", fontWeight = FontWeight.Bold
                    )
                })
        }, containerColor = Color(0x54EFFBF6),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = Color(0xFF2C8A5B),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Quick Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(15.dp, 0.dp, 10.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        "Search transactions, budgets, goals",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") })
            Spacer(Modifier.height(10.dp))
            DashboardCard(
                totalBalance = transactions.filter { it.isIncome }
                    .sumOf { it.amount } + transactions.filter { !it.isIncome }.sumOf { it.amount },
                income = transactions.filter { it.isIncome }.sumOf { it.amount },
                spent = transactions.filter { !it.isIncome }.sumOf { it.amount },
                saved = goals.sumOf { it.saved },
                progress = progress,
                goalText = goalText
            )
            Spacer(Modifier.height(14.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                ActionButton(Icons.Default.ArrowForward, "Add Income")
                ActionButton(Icons.Default.ArrowBack, "Add Expense")
                ActionButton(Icons.Default.Build, "New Goal")
            }
            Spacer(Modifier.height(20.dp))

            Text("Recent transactions", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.3.dp, Color.Gray),
                modifier = Modifier
            ) {
                Column {
                    if (transactions.isEmpty()) {
                        Text("No transactions available", fontWeight = FontWeight.Light)
                    } else {
                        transactions.take(3).forEach { transaction ->
                            TransactionItem(
                                transaction,
                                onClick = {
                                    editingTransaction = transaction
                                    currentType =
                                        if (transaction.isIncome) QuickAddType.INCOME else QuickAddType.EXPENSE
                                    showSheet = true
                                })
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            Text("Budgets", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.3.dp, Color.Gray),
                modifier = Modifier
            ) {
                Column {
                    if (budgets.isEmpty()) {
                        Text("No budgets available", fontWeight = FontWeight.Light)
                    } else {
                        budgets.take(3).forEach { budget ->
                            BudgetItem(
                                budget.icon,
                                budget.categoryName,
                                budget.spent,
                                budget.limit,
                                (budget.spent / budget.limit).toFloat()
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            Text("Goals", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.3.dp, Color.Gray),
                modifier = Modifier
            ) {
                Column {
                    if (goals.isEmpty()) {
                        Text("No goals available", fontWeight = FontWeight.Light)
                    } else {
                        goals.take(3).forEach { goal ->
                            GoalItem(goal)
                        }
                    }
                }
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            QuickAddSheet(
                type = currentType,
                onSaveTransaction = { transaction ->
                    if (editingTransaction != null) {
                        viewModel.updateTransaction(transaction)
                    } else {
                        viewModel.addTransaction(
                            transaction.amount,
                            transaction.title,
                            transaction.category,
                            transaction.type,
                            transaction.note
                        )
                    }
                    editingTransaction = null
                    showSheet = false
                },
                onSaveGoal = { goal ->
                    if (editingGoal != null) {
                        viewModel.updateGoal(goal)
                    } else {
                        viewModel.addGoal(goal.title, goal.target, goal.saved, goal.deadline)
                    }
                    editingGoal = null
                    showSheet = false
                },
                onSaveBudget = { budget ->
                    if (editingBudget != null) {
                        viewModel.updateBudget(budget)
                    } else {
                        viewModel.addBudget(
                            budget.categoryName,
                            budget.limit,
                            budget.spent,
                            budget.type,
                            budget.isRecurring,
                            budget.startDate,
                            budget.endDate
                        )
                    }
                    editingBudget = null
                    showSheet = false
                },
                onCancel = { showSheet = false }
            )

        }
    }
    LaunchedEffect(transactions) {
        Log.d("DB_CHECK", "Transactions in DB: $transactions")
    }
}