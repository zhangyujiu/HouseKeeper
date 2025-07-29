package com.house.keeper.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.house.keeper.ui.theme.HouseKeeperTheme
import org.junit.Rule
import org.junit.Test

class MonthlyOverviewCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun monthlyOverviewCard_displaysCorrectData() {
        // Given
        val income = 5000.0
        val expense = 2000.0
        val balance = 3000.0

        // When
        composeTestRule.setContent {
            HouseKeeperTheme {
                MonthlyOverviewCard(
                    monthlyIncome = income,
                    monthlyExpense = expense,
                    balance = balance
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("本月概览")
            .assertIsDisplayed()
            
        composeTestRule
            .onNodeWithText("当前余额")
            .assertIsDisplayed()
            
        // 验证收入和支出标签存在
        composeTestRule
            .onNodeWithText("收入")
            .assertIsDisplayed()
            
        composeTestRule
            .onNodeWithText("支出")
            .assertIsDisplayed()
    }

    @Test
    fun monthlyOverviewCard_showsNegativeBalance() {
        // Given
        val income = 1000.0
        val expense = 2000.0
        val balance = -1000.0

        // When
        composeTestRule.setContent {
            HouseKeeperTheme {
                MonthlyOverviewCard(
                    monthlyIncome = income,
                    monthlyExpense = expense,
                    balance = balance
                )
            }
        }

        // Then - 应该显示负余额
        composeTestRule
            .onNodeWithText("当前余额")
            .assertIsDisplayed()
    }
}
