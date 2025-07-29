package com.house.keeper.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

@Entity(
    tableName = "transactions",
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
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val amount: Double,
    
    val type: TransactionType,
    
    val categoryId: Long,
    
    val description: String = "",
    
    val date: Date,
    
    val createTime: Date = Date()
)

enum class TransactionType {
    INCOME,    // 收入
    EXPENSE    // 支出
}
