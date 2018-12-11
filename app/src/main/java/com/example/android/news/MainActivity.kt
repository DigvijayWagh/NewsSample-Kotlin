package com.example.android.news

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {

    private lateinit var newsViewModel: NewsViewModel
    lateinit var mSwipeRefreshLayout:SwipeRefreshLayout
    val url: String = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NewsListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_container)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        // Get a new or existing ViewModel from the ViewModelProvider.
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)

        newsViewModel.allNews.observe(this, Observer { news ->
            // Update the cached copy of the words in the adapter.
            news?.let { adapter.setNews(it) }
        })

        /**
         * Showing Swipe Refresh animation on activity create
         */
        mSwipeRefreshLayout.post {
            mSwipeRefreshLayout.isRefreshing = true
            // Fetching data from server
            receiveDataFromServer()
        }
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

                        var title:String
                        var description:String
                        var imageUrl:String

                        //Skip news if all field are null
                        if (jsonInner.isNull("title")&&jsonInner.isNull("description")&&jsonInner.isNull("imageHref"))
                            continue

                        if(jsonInner.isNull("title"))
                            title="TestTitle"
                        else
                            title=jsonInner.get("title").toString()


                        if(jsonInner.isNull("description"))
                            description="TestDescription"
                        else
                            description=jsonInner.get("description").toString()


                        if(jsonInner.isNull("imageHref"))
                            imageUrl="http://3.bp.blogspot.com/__mokxbTmuJM/RnWuJ6cE9cI/AAAAAAAAATw/6z3m3w9JDiU/s400/019843_31.jpg"
                        else
                            imageUrl=jsonInner.get("imageHref").toString()


                            val news = News(title,description,imageUrl)
                            newsViewModel.insert(news)

                    }

                    // Stopping swipe refresh
                    mSwipeRefreshLayout.isRefreshing = false;
                },
                Response.ErrorListener {

                    fun onErrorResponse(error: VolleyError) {

                        Toast.makeText(this,"Error: " + error.message,Toast.LENGTH_LONG).show()

                        mSwipeRefreshLayout.isRefreshing = false;
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

    override fun onRefresh() {

        try {
            newsViewModel.clearData()
            receiveDataFromServer()
        } catch (e: Exception) {
            mSwipeRefreshLayout.isRefreshing = false;
            e.printStackTrace()
        }
    }
}
