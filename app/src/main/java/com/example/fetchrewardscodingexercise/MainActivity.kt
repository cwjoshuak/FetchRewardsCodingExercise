package com.example.fetchrewardscodingexercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val adapter = HireDataAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        run("https://fetch-hiring.s3.amazonaws.com/hiring.json", adapter)
    }
    private fun run(url: String, adapter: HireDataAdapter) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response)  {
                response.use {
                    val gson = Gson()
                    var result = gson.fromJson(response.body()!!.string(),Array<RSListData>::class.java).toList()
                    result = result.filter { p -> p.name != "" && p.name != null }
                    result = result.sortedWith(compareBy({it.listId}, {it.name.split(" ")[1].toInt()}))


                    val ids = result.map { p -> p.listId }.toSet()

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        var sections: ArrayList<SimpleSectionedRecyclerViewAdapter.Section> = arrayListOf()
                        var sum = 0
                        for( id in ids) {
                            sections.add(SimpleSectionedRecyclerViewAdapter.Section(sum, "Section $id"))
                            sum += result.filter{p -> p.listId == id}.size
                        }
                        adapter.setData(result)
                        adapter.notifyDataSetChanged()
                        val mSectionedAdapter = SimpleSectionedRecyclerViewAdapter(this@MainActivity, R.layout.section, R.id.section_text, adapter)
                        mSectionedAdapter.setSections(sections.toTypedArray())
                        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
                        recyclerView.adapter = mSectionedAdapter

                    })
                }
            }
        })
    }
}