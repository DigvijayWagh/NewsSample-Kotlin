package com.example.android.news

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var newsViewModel: NewsViewModel
    val url: String = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NewsListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider.
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)

        newsViewModel.allNews.observe(this, Observer { news ->
            // Update the cached copy of the words in the adapter.
            news?.let { adapter.setNews(it) }
        })

        receiveDataFromServer()
    }

    fun receiveDataFromServer(){

        val queue = Volley.newRequestQueue(this)

        val stringReq = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->

                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)

                    val heading = jsonObj.get("title").toString()
                    setTitle(heading)

                    val jsonArray: JSONArray = jsonObj.getJSONArray("rows")

                    for (i in 0 until jsonArray.length()) {
                        val jsonInner: JSONObject = jsonArray.getJSONObject(i)

                        if(!(jsonInner.get("title")==null&&jsonInner.get("description")==null&&jsonInner.get("imageHref")==null)) {
                            val news = News(jsonInner.get("title").toString(), jsonInner.get("description").toString(), jsonInner.get("imageHref").toString())
                            newsViewModel.insert(news)
                        }
                    }
                },
                Response.ErrorListener {

                    fun onErrorResponse(error: VolleyError) {

                        Log.d("data_error", "Error: " + error.message)
                        if (error is NetworkError)
                        {}
                        else if (error is ServerError)
                        {}
                        else if (error is AuthFailureError)
                        {}
                        else if (error is ParseError)
                        {}
                        else if (error is NoConnectionError)
                        {}
                        else if (error is TimeoutError)
                        {}
                    }
                })
        queue.add(stringReq)
    }
}
