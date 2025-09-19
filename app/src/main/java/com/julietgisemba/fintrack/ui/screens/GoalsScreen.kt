package com.julietgisemba.fintrack.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.julietgisemba.fintrack.model.Goal
import com.julietgisemba.fintrack.model.QuickAddType
import com.julietgisemba.fintrack.ui.components.GoalItem
import com.julietgisemba.fintrack.ui.components.QuickAddSheet
import com.julietgisemba.fintrack.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(viewModel: FinanceViewModel = hiltViewModel()) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var currentType by remember { mutableStateOf(QuickAddType.GOAL) }
    val goals by viewModel.goalList.collectAsState()
    var editingGoal by remember { mutableStateOf<Goal?>(null) }

    val emergencyFund = Goal("Emergency Fund", saved = 3600.0, target = 5000.0)

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Goals", fontWeight = FontWeight.Bold
                )
            }, actions = {
                Button(
                    onClick = {}, colors = ButtonDefaults.buttonColors(Color(0xFF2C8A5B))
                ) {
                    Text("+ New goal")
                }
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
        Column(modifier = Modifier
            .padding(15.dp, 0.dp)
            .padding(innerPadding)) {
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            Spacer(Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.4.dp, Color.Gray),
                modifier = Modifier
            ) {
                GoalItem(goal = emergencyFund, icon = Icons.Default.DateRange, showActions = true)
            }

            Spacer(Modifier.height(20.dp))

            Text("Active goals", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.3.dp, Color.Gray),
                modifier = Modifier
            ) {
                LazyColumn {
                    items(goals) { goal ->
                        GoalItem(
                            goal = goal,
                            icon = Icons.Default.DateRange,
                            onClick = {
                                editingGoal = goal
                                showSheet = true
                            }
                        )

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
                editingGoal = editingGoal,
                onSaveTransaction = { transaction ->
                    viewModel.addTransaction(
                        transaction.amount,
                        transaction.title,
                        transaction.category,
                        transaction.type,
                        transaction.note
                    )
                    showSheet = false
                },
                onSaveGoal = { goal ->
                    if (editingGoal != null) {
                        // Update goal
                        viewModel.updateGoal(goal)
                    } else {
                        viewModel.addGoal(goal.title, goal.target, goal.saved, goal.deadline)
                    }
                    showSheet = false
                    editingGoal = null
                },
                onSaveBudget = { budget ->
                    viewModel.addBudget(
                        budget.categoryName,
                        budget.limit,
                        budget.spent,
                        budget.type,
                        budget.isRecurring,
                        budget.startDate,
                        budget.endDate
                    )
                    showSheet = false
                },
                onCancel = { showSheet = false }
            )

        }
    }
}