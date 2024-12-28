package com.project.minicalculator

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

class CalculatorViewModel : ViewModel() {
    private val _calculateBox = mutableStateOf("0")
    val calculateBox: State<String> get() = _calculateBox

    private val _answer = mutableStateOf("")
    val answer: State<String> get() = _answer

    private var currentExpression = "" // Stores the full calculation expression
    private var isExpectingNextNumber = false // Tracks if a new number should start after an operator or result
    private val decimalFormat = DecimalFormat("#,###.###") // Formats numbers with commas

    // Formats numbers with commas
    private fun formatNumber(input: String): String {
        return try {
            if (input.contains(".") || input.contains(" ")) input // Skip formatting for decimals or expressions
            else decimalFormat.format(input.toDouble())
        } catch (e: NumberFormatException) {
            input // Return input unaltered if it's not a valid number
        }
    }

    // Handles number button clicks
    fun onClickButton(number: String) {
        if (isExpectingNextNumber) {
            // Reset currentExpression and start a new number
            currentExpression = number
            isExpectingNextNumber = false
        } else {
            currentExpression += number
        }
        updateCalculateBox()
    }

    // Handles operator button clicks
    fun operationButton(operation: Char) {
        if (isExpectingNextNumber) {
            // Replace the last operator if one was already entered
            if (currentExpression.isNotEmpty() && currentExpression.last().isWhitespace()) {
                currentExpression = currentExpression.dropLast(2) + "$operation "
            }
        } else {
            if (currentExpression.isNotEmpty() && currentExpression.last().isDigit()) {
                currentExpression += " $operation "
                isExpectingNextNumber = false
            }
        }
        updateCalculateBox()
    }

    // Handles the equals button click
    fun equalToClick() {
        if (currentExpression.isNotEmpty() && currentExpression.last().isDigit()) {
            try {
                val result = evaluateExpression(currentExpression)
                _answer.value = if (result % 1 == 0.0) {
                    formatNumber(result.toInt().toString())
                } else {
                    result.toString()
                }

                // Leave currentExpression intact
                _calculateBox.value = currentExpression
                isExpectingNextNumber = true
            } catch (e: Exception) {
                _answer.value = "Error"
            }
        }
    }

    // Evaluates the mathematical expression
    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split(" ")
        val values = mutableListOf<Double>()
        val operators = mutableListOf<Char>()

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> values.add(token.toDouble())
                token.length == 1 -> operators.add(token[0])
            }
        }

        while (operators.isNotEmpty()) {
            val operator = operators.removeAt(0)
            val num1 = values.removeAt(0)
            val num2 = values.removeAt(0)
            val result = when (operator) {
                '+' -> num1 + num2
                '-' -> num1 - num2
                '*' -> num1 * num2
                '/' -> if (num2 != 0.0) num1 / num2 else throw ArithmeticException("Division by zero")
                else -> 0.0
            }
            values.add(0, result)
        }
        return values[0]
    }

    // Cancels the last entered number or operator
    fun cancelLastNumber() {
        if (currentExpression.isNotEmpty()) {
            val parts = currentExpression.split(" ")
            currentExpression = if (parts.last().length == 1) {
                parts.dropLast(1).joinToString(" ")
            } else {
                currentExpression.dropLast(1)
            }
            updateCalculateBox()
        }
    }

    // Clears the entire calculation
    fun onClearButton() {
        _calculateBox.value = "0"
        _answer.value = ""
        currentExpression = ""
        isExpectingNextNumber = false
    }

    // Updates the calculateBox with formatted currentExpression
    private fun updateCalculateBox() {
        _calculateBox.value = formatNumber(currentExpression)
    }
}
