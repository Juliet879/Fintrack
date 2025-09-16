package com.julietgisemba.fintrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julietgisemba.fintrack.data.repository.FinanceRepository
import com.julietgisemba.fintrack.model.Budget
import com.julietgisemba.fintrack.model.BudgetType
import com.julietgisemba.fintrack.model.Goal
import com.julietgisemba.fintrack.model.TransactionEntity
import com.julietgisemba.fintrack.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val repository: FinanceRepository
) : ViewModel() {

    val transactions = repository.getTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val budgetList = repository.getBudgets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val goalList = repository.getGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTransaction(
        amount: Double,
        title: String = "Income",
        category: String = "General",
        type: TransactionType,
        note: String? = null
    ) {
        val transaction = TransactionEntity(
            title = title,
            date = Date(),
            category = category,
            amount = amount,
            isIncome = true,
            note = note,
            type = type
        )
        viewModelScope.launch {
            repository.addTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun addGoal(
        title: String, target: Double, saved: Double = 0.0, deadline: Date? = null
    ) {
        val goal = Goal(
            title = title, target = target, saved = saved, deadline = deadline, isActive = true
        )
        viewModelScope.launch {
            repository.addGoal(goal)
        }
    }

    fun updateGoal( goal: Goal) {
        viewModelScope.launch {
            repository.updateGoal(goal)
        }
    }

    fun addBudget(
        categoryName: String,
        limit: Double,
        spent: Double,
        type: BudgetType,
        isRecurring: Boolean = false,
        startDate: Date? = null,
        endDate: Date? = null
    ) {
        val budget = Budget(
            categoryName = categoryName,
            limit = limit,
            spent = spent,
            type = type,
            isRecurring = isRecurring,
            startDate = startDate,
            endDate = endDate
        )
        viewModelScope.launch {
            repository.addBudget(budget)
        }
    }

    fun updateBudget( budget: Budget) {
        viewModelScope.launch {
            repository.updateBudget(budget)
        }
    }
}
