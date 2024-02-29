package com.example.exercise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exercise.ui.theme.ExerciseTheme
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

data class MyData(
    val title: String,
    val author: String,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExerciseTheme {
                val jsonString = applicationContext.assets
                    .open("content.json")
                    .bufferedReader()
                    .use { it.readText() }

                val dataArray = JSONArray(jsonString)
                val dataList = mutableListOf<MyData>()

                for (i in 0 until dataArray.length()) {
                    val item = dataArray.getJSONObject(i)
                    val title = item.getString("title")
                    val author = item.getString("author")
                    dataList.add(MyData(title, author))
                }

                MyApp(dataList) { updatedDataList ->
                    // When the dataList is updated (e.g., after adding data), write it to the file
                    val updatedJsonArray = JSONArray()
                    for (data in updatedDataList) {
                        val jsonItem = JSONObject()
                        jsonItem.put("title", data.title)
                        jsonItem.put("author", data.author)
                        updatedJsonArray.put(jsonItem)
                    }

                    val updatedJsonString = updatedJsonArray.toString()

                    val file = File(filesDir, "content.json")
                    val writer = BufferedWriter(FileWriter(file))
                    Log.d("Tag", updatedJsonString)
                    // Write the JSON content to the file
                    try {
                        // Write the JSON content to the file
                        writer.write(updatedJsonString)

                        // Close the writer to release resources
                        writer.close()

                        Log.d("TAG", "Content successfully written to $file")
                    } catch (e: Exception) {
                        Log.e("TAG", "Error writing content to file: ${e.message}")
                    }


                }
            }
        }
    }
}

@Composable
fun MyApp(dataList: MutableList<MyData>, onDataListChanged: (List<MyData>) -> Unit) {
    var newTitle by remember { mutableStateOf("") }
    var newAuthor by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row {
            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                label = { Text("New Title") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = newAuthor,
                onValueChange = { newAuthor = it },
                label = { Text("New Author") },
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = {
                dataList.add(MyData(newTitle, newAuthor))
                onDataListChanged(dataList)
                newTitle = ""
                newAuthor = ""
            },
            modifier = Modifier.align(Alignment.End)

        ) {
            Text("Add New Item")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(dataList.size) {item->
                Box (modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                ){
                    Column(modifier = Modifier.padding(25.dp)) {
                        Text(dataList[item].title)
                        Text(dataList[item].author)

                    }
                    Divider()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    ExerciseTheme {
        // You can provide a preview of your Compose UI here
        // Example: MyApp(mutableListOf(MyData("Sample Title", "Sample Author")))
    }
}
