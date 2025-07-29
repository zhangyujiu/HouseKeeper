package com.house.keeper.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class DatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        insertDefaultCategories(db)
    }

    private fun insertDefaultCategories(db: SupportSQLiteDatabase) {
        // 插入支出分类
        val expenseCategories = arrayOf(
            arrayOf("餐饮", "🍽️", "EXPENSE", "#FF6B6B", 1, 1),
            arrayOf("交通", "🚗", "EXPENSE", "#4ECDC4", 1, 2),
            arrayOf("购物", "🛒", "EXPENSE", "#45B7D1", 1, 3),
            arrayOf("娱乐", "🎮", "EXPENSE", "#96CEB4", 1, 4),
            arrayOf("医疗", "🏥", "EXPENSE", "#FFEAA7", 1, 5),
            arrayOf("教育", "📚", "EXPENSE", "#DDA0DD", 1, 6),
            arrayOf("住房", "🏠", "EXPENSE", "#98D8C8", 1, 7),
            arrayOf("通讯", "📱", "EXPENSE", "#F7DC6F", 1, 8),
            arrayOf("其他", "📝", "EXPENSE", "#BDC3C7", 1, 9)
        )

        // 插入收入分类
        val incomeCategories = arrayOf(
            arrayOf("工资", "💰", "INCOME", "#2ECC71", 1, 1),
            arrayOf("奖金", "🎁", "INCOME", "#3498DB", 1, 2),
            arrayOf("投资", "📈", "INCOME", "#9B59B6", 1, 3),
            arrayOf("兼职", "💼", "INCOME", "#E67E22", 1, 4),
            arrayOf("礼金", "🧧", "INCOME", "#E74C3C", 1, 5),
            arrayOf("其他", "📝", "INCOME", "#95A5A6", 1, 6)
        )

        // 插入支出分类
        expenseCategories.forEach { category ->
            db.execSQL(
                "INSERT INTO categories (name, icon, type, color, isDefault, sortOrder) VALUES (?, ?, ?, ?, ?, ?)",
                category
            )
        }

        // 插入收入分类
        incomeCategories.forEach { category ->
            db.execSQL(
                "INSERT INTO categories (name, icon, type, color, isDefault, sortOrder) VALUES (?, ?, ?, ?, ?, ?)",
                category
            )
        }
    }
}
