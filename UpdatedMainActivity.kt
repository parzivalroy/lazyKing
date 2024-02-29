package com.example.jsonreader

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jsonreader.ui.theme.JsonReaderTheme
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JsonReaderTheme {

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


                MyApp(dataList)

                val updatedJson = dataList.toJson()
                writeJsonToFile(applicationContext, "content.json", dataList)
                Log.d("TAG", "After Updating $updatedJson")
            }
        }
    }
}

data class MyData(
    val title: String,
    val author: String,
)


@Composable
fun MyApp(dataList: MutableList<MyData>) {
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

fun MutableList<MyData>.toJson(): String {
    val jsonArray = JSONArray()
    this.forEach { myData ->

        val jsonObject = JSONObject().apply {
            put("title", myData.title)
            put("author", myData.author)
        }
        jsonArray.put(jsonObject)
    }
    return jsonArray.toString()
}

fun writeJsonToFile(context: Context, fileName: String, newDataList: List<MyData>) {
    try {
        val existingJsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val existingJsonArray = JSONArray(existingJsonString)

        // Clear the existing JSON array and add new data
        existingJsonArray.put(JSONObject().apply {
            newDataList.forEach { newData ->
                put("title", newData.title)
                put("author", newData.author)
            }
        })

        // Write the updated JSON back to the file
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            OutputStreamWriter(output).use { writer ->
                writer.write(existingJsonArray.toString())
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JsonReaderTheme {

    }
}