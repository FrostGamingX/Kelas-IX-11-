package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var display by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }
    var waitingForSecondNumber by remember { mutableStateOf(false) }

    val buttonColor = Color(0xFF1E1E1E)
    val operatorColor = Color(0xFF00DDFF)
    val specialColor = Color(0xFF333333)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Display
        Text(
            text = display,
            color = Color.White,
            fontSize = 64.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 16.dp),
            maxLines = 1
        )

        // Baris 1: History + Copy icon (kosongkan saja atau tambah nanti)
        Spacer(modifier = Modifier.height(32.dp))

        // Baris tombol
        val buttons = listOf(
            listOf("C", "%", "⌫", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "−"),
            listOf("1", "2", "3", "+"),
            listOf("00", "0", ".", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { label ->
                    val backgroundColor = when (label) {
                        "÷", "×", "−", "+", "=" -> operatorColor
                        "C", "%", "⌫" -> specialColor
                        else -> buttonColor
                    }
                    val textColor = if (label in listOf("÷", "×", "−", "+", "=")) Color.Black else Color.White

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .clip(CircleShape)
                            .background(backgroundColor)
                            .clickable {
                                onButtonClick(
                                    label = label,
                                    display = display,
                                    firstNumber = firstNumber,
                                    operation = operation,
                                    waitingForSecondNumber = waitingForSecondNumber,
                                    onUpdate = { newDisplay, newFirst, newOp, newWaiting ->
                                        display = newDisplay
                                        firstNumber = newFirst
                                        operation = newOp
                                        waitingForSecondNumber = newWaiting
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = textColor,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

private fun onButtonClick(
    label: String,
    display: String,
    firstNumber: String,
    operation: String,
    waitingForSecondNumber: Boolean,
    onUpdate: (String, String, String, Boolean) -> Unit
) {
    when (label) {
        "C" -> onUpdate("0", "", "", false)
        "⌫" -> {
            if (display.length > 1) {
                onUpdate(display.dropLast(1), firstNumber, operation, waitingForSecondNumber)
            } else {
                onUpdate("0", firstNumber, operation, waitingForSecondNumber)
            }
        }
        "%" -> {
            val value = display.toDoubleOrNull() ?: return
            onUpdate((value / 100).toString(), firstNumber, operation, waitingForSecondNumber)
        }
        "+", "−", "×", "÷" -> {
            onUpdate(display, display, label, true)
        }
        "=" -> {
            if (firstNumber.isEmpty() || operation.isEmpty()) return
            val num1 = firstNumber.toDouble()
            val num2 = display.toDoubleOrNull() ?: return
            val result = when (operation) {
                "+" -> num1 + num2
                "−" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> if (num2 != 0.0) num1 / num2 else { onUpdate("Error", "", "", false); return }
                else -> 0.0
            }
            onUpdate(result.toString().removeSuffix(".0"), "", "", false)
        }
        else -> {
            if (waitingForSecondNumber) {
                onUpdate(label, firstNumber, operation, false)
            } else {
                val newDisplay = if (display == "0") label else display + label
                onUpdate(newDisplay, firstNumber, operation, false)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorApp()
}