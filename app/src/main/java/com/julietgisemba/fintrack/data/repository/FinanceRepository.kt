package com.julietgisemba.fintrack.data.repository

import com.julietgisemba.fintrack.data.dao.BudgetDao
import com.julietgisemba.fintrack.data.dao.GoalDao
import com.julietgisemba.fintrack.data.dao.TransactionDao
import com.julietgisemba.fintrack.model.Budget
import com.julietgisemba.fintrack.model.Goal
import com.julietgisemba.fintrack.model.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRepository @Inject constructor(private val transactionDao: TransactionDao, private val budgetDao: BudgetDao, private val goalDao: GoalDao) {
    suspend fun addTransaction(transactionEntity: TransactionEntity) = transactionDao.insertTransaction(transactionEntity)
    suspend fun updateTransaction(transactionEntity: TransactionEntity) = transactionDao.updateTransaction(transactionEntity)
    suspend fun addBudget(budget: Budget) = budgetDao.insertBudget(budget)
    suspend fun addGoal(goal: Goal) = goalDao.insertGoal(goal)

    suspend fun updateBudget(budget: Budget) = budgetDao.updateBudget(budget)
    suspend fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)

    fun getTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    fun getBudgets(): Flow<List<Budget>> = budgetDao.getBudgets()
    fun getGoals() : Flow<List<Goal>> = goalDao.getGoals()

}