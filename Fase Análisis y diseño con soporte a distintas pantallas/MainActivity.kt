package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.ui.theme.ToDoListAppTheme
import androidx.compose.ui.tooling.preview.Preview

data class Task(
    val title: String,
    val description: String,
    val date: String,
    val time: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ToDoListApp()
                }
            }
        }
    }
}

@Composable
fun ToDoListApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tareas", "Papelera")

    var tasks = remember { mutableStateListOf<Task>() }
    val trashTasks = remember { mutableStateListOf<Task>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Aplicación de Tareas (TM)",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimary, // El color será blanco en modo oscuro y negro en modo claro
            modifier = Modifier.padding(16.dp)
        )

        TabRow(selectedTabIndex = selectedTab, containerColor = MaterialTheme.colorScheme.surface) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        when (selectedTab) {
            0 -> TareasScreen(
                tasks = tasks,
                onAddTask = { newTask -> tasks.add(newTask) },
                onDeleteTask = { task ->
                    tasks.remove(task)
                    trashTasks.add(task)
                },
                onClearTasks = { tasks.clear() }
            )
            1 -> PapeleraScreen(
                trashTasks = trashTasks,
                onRestoreTask = { task ->
                    trashTasks.remove(task)
                    tasks.add(task)
                },
                onDeleteTaskPermanently = { trashTasks.remove(it) },
                onClearTrash = { trashTasks.clear() }
            )
        }
    }
}


@Composable
fun TareasScreen(
    tasks: List<Task>,
    onAddTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onClearTasks: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) { Text("Añadir Tarea") }
        Button(onClick = onClearTasks) { Text("Limpiar Tareas") }

        Text(
            text = "Mis Tareas",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mostrar lista de tareas
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            tasks.forEach { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedTask = task }, // Click para previsualizar la tarea
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = task.title, modifier = Modifier.weight(1f))
                    Button(onClick = { onDeleteTask(task) }) {
                        Text("Eliminar")
                    }
                }
            }
        }

        // Mostrar los detalles de la tarea seleccionada
        selectedTask?.let { task ->
            AlertDialog(
                onDismissRequest = { selectedTask = null },
                title = { Text("Detalles de Tarea") },
                text = {
                    Column {
                        Text("Descripción: ${task.description}")
                        Text("Fecha: ${task.date}")
                        Text("Hora: ${task.time}")
                    }
                },
                confirmButton = {
                    Button(onClick = { selectedTask = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }

        // Diálogo para añadir nueva tarea
        if (showDialog) {
            TaskDialog(onDismiss = { showDialog = false }, onSaveTask = { task ->
                onAddTask(task)  // Añadir la nueva tarea a la lista
                showDialog = false
            })
        }
    }
}

@Composable
fun TaskDialog(onDismiss: () -> Unit, onSaveTask: (Task) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Añadir Tarea") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = date, onValueChange = { date = it }, label = { Text("Fecha") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = time, onValueChange = { time = it }, label = { Text("Hora") })
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    onSaveTask(Task(title, description, date, time)) // Guardar tarea con todos los datos
                    onDismiss()
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun PapeleraScreen(
    trashTasks: List<Task>,
    onRestoreTask: (Task) -> Unit,
    onDeleteTaskPermanently: (Task) -> Unit,
    onClearTrash: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Papelera",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            trashTasks.forEach { task: Task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = task.title, modifier = Modifier.weight(1f))
                    Button(onClick = { onRestoreTask(task) }) {
                        Text("Restaurar")
                    }
                    Button(onClick = { onDeleteTaskPermanently(task) }) {
                        Text("Eliminar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onClearTrash) {
            Text("Limpiar Papelera")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoListAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ToDoListApp()
        }
    }
}
