package com.house.keeper.data.repository.impl

import com.house.keeper.data.database.dao.BudgetDao
import com.house.keeper.data.database.dao.CategoryDao
import com.house.keeper.data.model.Budget
import com.house.keeper.data.model.toBudget
import com.house.keeper.data.model.toEntity
import com.house.keeper.data.model.toCategory
import com.house.keeper.data.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao
) : BudgetRepository {
    
    override fun getAllBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllBudgets().combine(
            categoryDao.getAllCategories()
        ) { budgets, categories ->
            budgets.map { budgetEntity ->
                budgetEntity.toBudget(
                    categories.find { it.id == budgetEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override suspend fun getBudgetById(id: Long): Budget? {
        val budgetEntity = budgetDao.getBudgetById(id) ?: return null
        val category = categoryDao.getCategoryById(budgetEntity.categoryId)
        return budgetEntity.toBudget(category?.toCategory())
    }
    
    override fun getBudgetsByCategory(categoryId: Long): Flow<List<Budget>> {
        return budgetDao.getBudgetsByCategory(categoryId).combine(
            categoryDao.getAllCategories()
        ) { budgets, categories ->
            budgets.map { budgetEntity ->
                budgetEntity.toBudget(
                    categories.find { it.id == budgetEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override fun getBudgetsByDateRange(startDate: Date, endDate: Date): Flow<List<Budget>> {
        return budgetDao.getBudgetsByDateRange(startDate, endDate).combine(
            categoryDao.getAllCategories()
        ) { budgets, categories ->
            budgets.map { budgetEntity ->
                budgetEntity.toBudget(
                    categories.find { it.id == budgetEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override fun getActiveBudgets(): Flow<List<Budget>> {
        return budgetDao.getActiveBudgets(Date()).combine(
            categoryDao.getAllCategories()
        ) { budgets, categories ->
            budgets.map { budgetEntity ->
                budgetEntity.toBudget(
                    categories.find { it.id == budgetEntity.categoryId }?.toCategory()
                )
            }
        }
    }
    
    override suspend fun insertBudget(budget: Budget): Long {
        return budgetDao.insertBudget(budget.toEntity())
    }
    
    override suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget.toEntity())
    }
    
    override suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget.toEntity())
    }
    
    override suspend fun deleteBudgetById(id: Long) {
        budgetDao.deleteBudgetById(id)
    }
    
    override suspend fun getBudgetUsage(budgetId: Long): Double {
        val budget = budgetDao.getBudgetById(budgetId) ?: return 0.0
        return budgetDao.getBudgetUsage(budget.categoryId, budget.startDate, budget.endDate)
    }

    override suspend fun isBudgetExceeded(budgetId: Long): Boolean {
        val budget = budgetDao.getBudgetById(budgetId) ?: return false
        return budgetDao.isBudgetExceeded(budget.categoryId, budget.startDate, budget.endDate, budget.amount)
    }
}
