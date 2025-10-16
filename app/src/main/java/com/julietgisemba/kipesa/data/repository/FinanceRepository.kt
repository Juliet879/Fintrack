package com.julietgisemba.kipesa.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julietgisemba.kipesa.data.dao.BudgetDao
import com.julietgisemba.kipesa.data.dao.GoalDao
import com.julietgisemba.kipesa.data.dao.TransactionDao
import com.julietgisemba.kipesa.model.Budget
import com.julietgisemba.kipesa.model.Goal
import com.julietgisemba.kipesa.model.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRepository @Inject constructor(private val transactionDao: TransactionDao, private val budgetDao: BudgetDao, private val goalDao: GoalDao, private val firestore: FirebaseFirestore) {
    private val user_Id = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
    val db = FirebaseFirestore.getInstance()

    suspend fun addTransaction(transactionEntity: TransactionEntity) {
        transactionDao.insertTransaction(transactionEntity)
        saveTransactionToFirestore(user_Id,transactionEntity)
    }
    suspend fun updateTransaction(transactionEntity: TransactionEntity) {
        transactionDao.updateTransaction(transactionEntity)
        saveTransactionToFirestore(user_Id,transactionEntity)
    }
    suspend fun addBudget(budget: Budget) {
        budgetDao.insertBudget(budget)
        saveBudgetToFirestore(user_Id, budget)
    }
    suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget)
        saveBudgetToFirestore(user_Id, budget)
    }
    suspend fun addGoal(goal: Goal) {
        goalDao.insertGoal(goal)
        saveGoalToFirestore(user_Id, goal)
    }

    suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal)
        saveGoalToFirestore(user_Id, goal)
    }

    fun getTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    fun getBudgets(): Flow<List<Budget>> = budgetDao.getBudgets()
    fun getGoals() : Flow<List<Goal>> = goalDao.getGoals()


    //saving to firestore
    fun saveTransactionToFirestore(userId: String, transaction: TransactionEntity) {
        db.collection("users")
            .document(userId)
            .collection("transactions")
            .add(transaction) // 👈 use add(), not set()
            .addOnSuccessListener { Log.d("Firestore", "Transaction saved for $userId") }
            .addOnFailureListener { Log.e("Firestore", "Error saving transaction", it) }
    }

    fun saveBudgetToFirestore(userId: String, budget: Budget) {
        db.collection("users")
            .document(userId)
            .collection("budgets")
            .add(budget)
            .addOnSuccessListener { Log.d("Firestore", "Budget saved for $userId") }
            .addOnFailureListener { Log.e("Firestore", "Error saving budget", it) }
    }

    fun saveGoalToFirestore(userId: String, goal: Goal) {
        db.collection("users")
            .document(userId)
            .collection("goals")
            .add(goal)
            .addOnSuccessListener { Log.d("Firestore", "Goal saved for $userId") }
            .addOnFailureListener { Log.e("Firestore", "Error saving goal", it) }
    }

}