package com.example.to_doapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast

@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var newTask by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "To-Do List",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = newTask,
            onValueChange = { newTask = it },
            label = { Text("New Task") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (newTask.isNotBlank()) {
                    viewModel.addTask(newTask)
                    newTask = ""
                    Toast.makeText(context, "Task added", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            viewModel.removeTask(task)
                            Toast.makeText(context, "Task '$task' removed", Toast.LENGTH_SHORT).show()
                        }
                ) {
                    Text(
                        text = task,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}