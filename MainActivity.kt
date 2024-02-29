package com.example.jsonreader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jsonreader.ui.theme.JsonReaderTheme
import org.json.JSONArray

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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JsonReaderTheme {

    }
}