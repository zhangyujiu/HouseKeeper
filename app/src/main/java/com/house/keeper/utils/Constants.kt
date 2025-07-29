package com.house.keeper.utils

object Constants {
    
    // 数据库相关
    const val DATABASE_NAME = "house_keeper_database"
    const val DATABASE_VERSION = 1
    
    // 分页相关
    const val DEFAULT_PAGE_SIZE = 20
    const val RECENT_TRANSACTIONS_LIMIT = 10
    
    // 金额相关
    const val MAX_AMOUNT = 999999999.99
    const val MIN_AMOUNT = 0.01
    
    // 日期相关
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val TIME_FORMAT = "HH:mm"
    const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm"
    const val MONTH_FORMAT = "yyyy-MM"
    const val YEAR_FORMAT = "yyyy"
    
    // 分类相关
    const val MAX_CATEGORY_NAME_LENGTH = 20
    const val MAX_CUSTOM_CATEGORIES = 50
    
    // 备注相关
    const val MAX_DESCRIPTION_LENGTH = 100
    
    // 预算相关
    const val MAX_BUDGET_AMOUNT = 999999999.99
    const val MIN_BUDGET_AMOUNT = 1.0
    
    // 统计相关
    const val CHART_ANIMATION_DURATION = 1000
    const val MAX_CHART_ITEMS = 10
    
    // 颜色相关
    val DEFAULT_CATEGORY_COLORS = listOf(
        "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEAA7",
        "#DDA0DD", "#98D8C8", "#F7DC6F", "#BDC3C7", "#2ECC71",
        "#3498DB", "#9B59B6", "#E67E22", "#E74C3C", "#95A5A6"
    )
    
    // 图标相关
    val DEFAULT_EXPENSE_ICONS = listOf(
        "🍽️", "🚗", "🛒", "🎮", "🏥", "📚", "🏠", "📱", "📝"
    )
    
    val DEFAULT_INCOME_ICONS = listOf(
        "💰", "🎁", "📈", "💼", "🧧", "📝"
    )
}
