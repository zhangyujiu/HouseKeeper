package com.house.keeper.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val categoryId: Long,
    
    val amount: Double,
    
    val period: BudgetPeriod,
    
    val startDate: Date,
    
    val endDate: Date,
    
    val createTime: Date = Date()
)

enum class BudgetPeriod {
    MONTHLY,    // 月度预算
    YEARLY      // 年度预算
}
