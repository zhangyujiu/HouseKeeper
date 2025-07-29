package com.house.keeper.data.repository.impl

import com.house.keeper.data.database.dao.CategoryDao
import com.house.keeper.data.database.entities.TransactionType
import com.house.keeper.data.model.Category
import com.house.keeper.data.model.toCategory
import com.house.keeper.data.model.toEntity
import com.house.keeper.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    
    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toCategory() }
        }
    }
    
    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)?.toCategory()
    }
    
    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type).map { entities ->
            entities.map { it.toCategory() }
        }
    }
    
    override fun getDefaultCategories(): Flow<List<Category>> {
        return categoryDao.getDefaultCategories().map { entities ->
            entities.map { it.toCategory() }
        }
    }
    
    override fun getCustomCategories(): Flow<List<Category>> {
        return categoryDao.getCustomCategories().map { entities ->
            entities.map { it.toCategory() }
        }
    }
    
    override fun searchCategoriesByName(name: String): Flow<List<Category>> {
        return categoryDao.searchCategoriesByName(name).map { entities ->
            entities.map { it.toCategory() }
        }
    }
    
    override suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category.toEntity())
    }
    
    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }
    
    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }
    
    override suspend fun deleteCategoryById(id: Long) {
        categoryDao.deleteCategoryById(id)
    }
    
    override suspend fun isCategoryNameExists(name: String, type: TransactionType): Boolean {
        return categoryDao.getCategoryCountByNameAndType(name, type) > 0
    }
}
