# lazyKing

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
