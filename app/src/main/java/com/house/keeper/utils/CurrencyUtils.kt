package com.house.keeper.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object CurrencyUtils {
    
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.CHINA)
    private val decimalFormat = DecimalFormat("#,##0.00")
    private val simpleDecimalFormat = DecimalFormat("#,##0.##")
    
    fun formatCurrency(amount: Double): String {
        return currencyFormat.format(amount)
    }
    
    fun formatAmount(amount: Double): String {
        return decimalFormat.format(amount)
    }
    
    fun formatSimpleAmount(amount: Double): String {
        return simpleDecimalFormat.format(amount)
    }
    
    fun formatAmountWithSymbol(amount: Double, isIncome: Boolean = false): String {
        val formattedAmount = formatAmount(amount)
        return if (isIncome) "+$formattedAmount" else "-$formattedAmount"
    }
    
    fun parseAmount(amountString: String): Double? {
        return try {
            // 移除所有非数字和小数点的字符
            val cleanString = amountString.replace(Regex("[^\\d.]"), "")
            if (cleanString.isEmpty()) null else cleanString.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }
    
    fun isValidAmount(amountString: String): Boolean {
        return parseAmount(amountString) != null
    }
    
    fun formatPercentage(value: Double): String {
        return "${(value * 100).toInt()}%"
    }
}
