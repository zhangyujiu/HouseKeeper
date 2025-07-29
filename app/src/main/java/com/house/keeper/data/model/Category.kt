package com.house.keeper.data.model

import com.house.keeper.data.database.entities.CategoryEntity
import com.house.keeper.data.database.entities.TransactionType

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String,
    val type: TransactionType,
    val color: String,
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        type = type,
        color = color,
        isDefault = isDefault,
        sortOrder = sortOrder
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        type = type,
        color = color,
        isDefault = isDefault,
        sortOrder = sortOrder
    )
}
