package com.house.keeper.data

import com.house.keeper.data.database.entities.CategoryEntity
import com.house.keeper.data.database.entities.TransactionType

object DefaultData {
    
    fun getDefaultCategories(): List<CategoryEntity> {
        return listOf(
            // 支出分类
            CategoryEntity(
                name = "餐饮",
                type = TransactionType.EXPENSE,
                icon = "🍽️",
                color = "#FF6B6B"
            ),
            CategoryEntity(
                name = "交通",
                type = TransactionType.EXPENSE,
                icon = "🚗",
                color = "#4ECDC4"
            ),
            CategoryEntity(
                name = "购物",
                type = TransactionType.EXPENSE,
                icon = "🛒",
                color = "#45B7D1"
            ),
            CategoryEntity(
                name = "娱乐",
                type = TransactionType.EXPENSE,
                icon = "🎮",
                color = "#96CEB4"
            ),
            CategoryEntity(
                name = "医疗",
                type = TransactionType.EXPENSE,
                icon = "🏥",
                color = "#FFEAA7"
            ),
            CategoryEntity(
                name = "教育",
                type = TransactionType.EXPENSE,
                icon = "📚",
                color = "#DDA0DD"
            ),
            CategoryEntity(
                name = "住房",
                type = TransactionType.EXPENSE,
                icon = "🏠",
                color = "#FFB6C1"
            ),
            CategoryEntity(
                name = "通讯",
                type = TransactionType.EXPENSE,
                icon = "📱",
                color = "#87CEEB"
            ),
            CategoryEntity(
                name = "服装",
                type = TransactionType.EXPENSE,
                icon = "👕",
                color = "#F0E68C"
            ),
            CategoryEntity(
                name = "其他",
                type = TransactionType.EXPENSE,
                icon = "📦",
                color = "#D3D3D3"
            ),
            
            // 收入分类
            CategoryEntity(
                name = "工资",
                type = TransactionType.INCOME,
                icon = "💰",
                color = "#98D8C8"
            ),
            CategoryEntity(
                name = "奖金",
                type = TransactionType.INCOME,
                icon = "🎁",
                color = "#F7DC6F"
            ),
            CategoryEntity(
                name = "投资",
                type = TransactionType.INCOME,
                icon = "📈",
                color = "#AED6F1"
            ),
            CategoryEntity(
                name = "兼职",
                type = TransactionType.INCOME,
                icon = "💼",
                color = "#A9DFBF"
            ),
            CategoryEntity(
                name = "礼金",
                type = TransactionType.INCOME,
                icon = "🧧",
                color = "#F1948A"
            ),
            CategoryEntity(
                name = "退款",
                type = TransactionType.INCOME,
                icon = "↩️",
                color = "#D7BDE2"
            ),
            CategoryEntity(
                name = "其他",
                type = TransactionType.INCOME,
                icon = "💵",
                color = "#85C1E9"
            )
        )
    }
}
