package com.julietgisemba.kipesa.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.julietgisemba.kipesa.data.dao.BudgetDao
import com.julietgisemba.kipesa.data.dao.GoalDao
import com.julietgisemba.kipesa.data.dao.TransactionDao
import com.julietgisemba.kipesa.model.Budget
import com.julietgisemba.kipesa.model.Goal
import com.julietgisemba.kipesa.model.TransactionEntity
import com.julietgisemba.kipesa.room.Converters

@Database(entities = [TransactionEntity::class, Budget::class, Goal::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FinanceDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
}