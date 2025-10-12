package com.example.basiccalculator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basiccalculator.ui.theme.BasicCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var firstNumber by remember { mutableStateOf("") }
    var secondNumber by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Simple Calculator",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = firstNumber,
            onValueChange = { firstNumber = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("First Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = secondNumber,
            onValueChange = { secondNumber = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Second Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { operation = "+" },
                modifier = Modifier.weight(1f)
            ) {
                Text("+")
            }
            Button(
                onClick = { operation = "-" },
                modifier = Modifier.weight(1f)
            ) {
                Text("-")
            }
            Button(
                onClick = { operation = "*" },
                modifier = Modifier.weight(1f)
            ) {
                Text("×")
            }
            Button(
                onClick = { operation = "/" },
                modifier = Modifier.weight(1f)
            ) {
                Text("÷")
            }
        }

        Button(
            onClick = {
                result = calculateResult(firstNumber, secondNumber, operation)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        if (result.isNotEmpty()) {
            Text(
                text = "Result: $result",
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

fun calculateResult(firstNumber: String, secondNumber: String, operation: String): String {
    if (firstNumber.isEmpty() || secondNumber.isEmpty() || operation.isEmpty()) {
        return "Please enter numbers and select an operation"
    }

    return try {
        val num1 = firstNumber.toDouble()
        val num2 = secondNumber.toDouble()
        when (operation) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "*" -> (num1 * num2).toString()
            "/" -> if (num2 != 0.0) (num1 / num2).toString() else "Cannot divide by zero"
            else -> "Invalid operation"
        }
    } catch (e: NumberFormatException) {
        "Invalid input"
    }
}