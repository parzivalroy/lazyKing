# lazyKing

## Reading
```
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
```

## Writing

```
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

```
