package com.example.myapplication.simplenotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.simplenotesapp.ui.theme.SimpleNotesAppTheme

data class Note(
    val title: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleNotesAppTheme {
                NotesApp()
            }
        }
    }
}

@Composable
fun NotesApp() {
    var notes by remember { mutableStateOf(mutableListOf<Note>()) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (showDialog) {
                AddNoteDialog(
                    onAddNote = { title, description ->
                        notes.add(Note(title, description))
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }
            NotesList(notes = notes, onDelete = { notes.removeAt(it) })
        }
    }
}

@Composable
fun NotesList(notes: List<Note>, onDelete: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(notes.size) { index ->
            NoteItem(
                note = notes[index],
                onDelete = { onDelete(index) }
            )
        }
    }
}

@Composable
fun NoteItem(note: Note, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.description, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun AddNoteDialog(onAddNote: (String, String) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Note") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        onAddNote(title, description)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NotesAppPreview() {
    SimpleNotesAppTheme {
        NotesApp()
    }
}
