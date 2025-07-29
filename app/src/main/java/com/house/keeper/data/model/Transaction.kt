package com.house.keeper.data.model

import com.house.keeper.data.database.entities.TransactionEntity
import com.house.keeper.data.database.entities.TransactionType
import java.util.Date

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val category: Category,
    val description: String = "",
    val date: Date,
    val createTime: Date = Date()
)

fun TransactionEntity.toTransaction(category: Category?): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        type = type,
        category = category ?: Category(
            id = categoryId,
            name = "未知分类",
            type = type,
            icon = "❓",
            color = "#999999"
        ),
        description = description,
        date = date,
        createTime = createTime
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        type = type,
        categoryId = category.id,
        description = description,
        date = date,
        createTime = createTime
    )
}
