package com.house.keeper.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.house.keeper.data.database.dao.BudgetDao
import com.house.keeper.data.database.dao.CategoryDao
import com.house.keeper.data.database.dao.TransactionDao
import com.house.keeper.data.database.entities.BudgetEntity
import com.house.keeper.data.database.entities.CategoryEntity
import com.house.keeper.data.database.entities.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    

}
