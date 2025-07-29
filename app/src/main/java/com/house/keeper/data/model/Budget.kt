package com.house.keeper.data.model

import com.house.keeper.data.database.entities.BudgetEntity
import com.house.keeper.data.database.entities.BudgetPeriod
import java.util.Date

data class Budget(
    val id: Long = 0,
    val category: Category,
    val amount: Double,
    val period: BudgetPeriod,
    val startDate: Date,
    val endDate: Date,
    val createTime: Date = Date()
)

fun BudgetEntity.toBudget(category: Category?): Budget {
    return Budget(
        id = id,
        category = category ?: Category(
            id = categoryId,
            name = "未知分类",
            type = com.house.keeper.data.database.entities.TransactionType.EXPENSE,
            icon = "❓",
            color = "#999999"
        ),
        amount = amount,
        period = period,
        startDate = startDate,
        endDate = endDate,
        createTime = createTime
    )
}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        categoryId = category.id,
        amount = amount,
        period = period,
        startDate = startDate,
        endDate = endDate,
        createTime = createTime
    )
}
