package com.house.keeper.data.database.dao

import androidx.room.*
import com.house.keeper.data.database.entities.CategoryEntity
import com.house.keeper.data.database.entities.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC, name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?
    
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY sortOrder ASC, name ASC")
    fun getCategoriesByType(type: TransactionType): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE isDefault = 1 ORDER BY sortOrder ASC, name ASC")
    fun getDefaultCategories(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE isDefault = 0 ORDER BY sortOrder ASC, name ASC")
    fun getCustomCategories(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE name LIKE '%' || :name || '%' ORDER BY sortOrder ASC, name ASC")
    fun searchCategoriesByName(name: String): Flow<List<CategoryEntity>>
    
    @Insert
    suspend fun insertCategory(category: CategoryEntity): Long
    
    @Update
    suspend fun updateCategory(category: CategoryEntity)
    
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
    
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Long)
    
    @Query("SELECT COUNT(*) FROM categories WHERE name = :name AND type = :type")
    suspend fun getCategoryCountByNameAndType(name: String, type: TransactionType): Int
}
