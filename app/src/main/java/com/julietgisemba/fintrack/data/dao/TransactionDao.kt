package com.julietgisemba.fintrack.data.dao

import androidx.room.*
import com.julietgisemba.fintrack.model.Budget
import com.julietgisemba.fintrack.model.Goal
import com.julietgisemba.fintrack.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

}

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Query("SELECT * FROM budgets")
    fun getBudgets(): Flow<List<Budget>>
}

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<Goal>>
}
