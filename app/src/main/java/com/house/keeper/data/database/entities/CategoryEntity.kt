package com.house.keeper.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    
    val icon: String,
    
    val type: TransactionType,
    
    val color: String,
    
    val isDefault: Boolean = false,
    
    val sortOrder: Int = 0
)
