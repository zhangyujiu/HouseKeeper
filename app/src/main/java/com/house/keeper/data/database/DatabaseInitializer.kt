package com.house.keeper.data.database

import com.house.keeper.data.DefaultData
import com.house.keeper.data.database.dao.CategoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val categoryDao: CategoryDao
) {
    
    fun initializeDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            // 检查是否已经有分类数据
            val existingCategories = categoryDao.getCategoryCount()
            
            if (existingCategories == 0) {
                // 插入默认分类
                val defaultCategories = DefaultData.getDefaultCategories()
                defaultCategories.forEach { category ->
                    categoryDao.insertCategory(category)
                }
            }
        }
    }
}
