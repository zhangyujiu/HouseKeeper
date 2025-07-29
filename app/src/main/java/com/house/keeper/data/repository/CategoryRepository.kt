package com.house.keeper.data.repository

import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    
    fun getAllCategories(): Flow<List<Category>>
    
    suspend fun getCategoryById(id: Long): Category?
    
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    
    fun getDefaultCategories(): Flow<List<Category>>
    
    fun getCustomCategories(): Flow<List<Category>>
    
    fun searchCategoriesByName(name: String): Flow<List<Category>>
    
    suspend fun insertCategory(category: Category): Long
    
    suspend fun updateCategory(category: Category)
    
    suspend fun deleteCategory(category: Category)
    
    suspend fun deleteCategoryById(id: Long)
    
    suspend fun isCategoryNameExists(name: String, type: TransactionType): Boolean
}
