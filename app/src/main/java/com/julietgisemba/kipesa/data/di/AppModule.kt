package com.julietgisemba.kipesa.data.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.julietgisemba.kipesa.data.dao.BudgetDao
import com.julietgisemba.kipesa.data.dao.GoalDao
import com.julietgisemba.kipesa.data.dao.TransactionDao
import com.julietgisemba.kipesa.data.database.FinanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FinanceDatabase =
        Room.databaseBuilder(context, FinanceDatabase::class.java, "fintrack_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTransactionDao(db: FinanceDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideBudgetDao(db: FinanceDatabase): BudgetDao = db.budgetDao()

    @Provides
    fun provideGoalDao(db: FinanceDatabase): GoalDao = db.goalDao()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}