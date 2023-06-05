package com.example.playlistmaker.activityclasses


import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ITunesAPI
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.trackrecycleview.AdapterSearch
import com.example.playlistmaker.trackrecycleview.AdapterSearchHistory
import com.example.playlistmaker.api.ITunesResponse
import com.example.playlistmaker.trackrecycleview.Track
import handler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sharedPreferencesInit
import textOfSearch
import SEARCH_DEBOUNCE_DELAY as SEARCH_DEBOUNCE_DELAY1


class SearchActivity : AppCompatActivity() {

    private lateinit var textViewSearchHistory: TextView
    private lateinit var buttonClearSearchHistory: Button
    private lateinit var searchField: EditText
    private lateinit var textSearch: String
    private lateinit var searchProgressBar: ProgressBar
    lateinit var recyclerViewSongs: RecyclerView
    private lateinit var downloadFailButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    var listOfSongs = mutableListOf<Track>()
    private val searchRequest = Runnable { search() }
    private val iTunesAPI = "https://itunes.apple.com"  //Base URL for iTunesAPI

    // Initialisation of retrofit component
    private val retrofitITunes =
        Retrofit.Builder()
            .baseUrl(iTunesAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesAPIService = retrofitITunes.create(ITunesAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPreferences = sharedPreferencesInit(this.applicationContext)

        textSearch = ""

        SearchHistory.sharedPreferences = sharedPreferences
        SearchHistory.refreshHistory()

        recyclerViewSongs = findViewById(R.id.recyclerView_songs)
        recyclerViewSongs.layoutManager = LinearLayoutManager(this)
        recyclerViewSongs.adapter = AdapterSearch(listOfSongs)

        downloadFailButton = findViewById(R.id.button_download_fail)

        val buttonBack = findViewById<Button>(R.id.back_button_search_act)

        textViewSearchHistory = findViewById(R.id.text_view_search_history)

        buttonClearSearchHistory = findViewById(R.id.button_clear_search_history)

        val clearButton = findViewById<Button>(R.id.clear_text_search)

        searchField = findViewById(R.id.search_bar)

        searchProgressBar = findViewById(R.id.search_progressBar)

        buttonBack.setOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            searchField.setText(textSearch)
        }

        buttonClearSearchHistory.setOnClickListener {
            SearchHistory.clearHistory()
            searchHistoryVisib(SearchHistoryVisibility.GONE)
            recyclerViewSongs.adapter?.notifyDataSetChanged()

        }

        downloadFailButton.setOnClickListener { search() }

        clearButton.setOnClickListener {
            searchField.setText("")
            listOfSongs.clear()
            recyclerViewSongs.adapter?.notifyDataSetChanged()
            errorVisibility(
                SearchActItemsVisib.SUCCESS
            )
        }

        val searchActivityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                recyclerViewSongs.adapter?.notifyDataSetChanged()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (searchField.hasFocus() && searchField.text.isEmpty() && SearchHistory.notEmpty()) {
                    searchHistoryVisib(SearchHistoryVisibility.VISIBLE)
                    SearchHistory.refreshHistory()
                    recyclerViewSongs.adapter = AdapterSearchHistory(SearchHistory.getHistory())
                    recyclerViewSongs.adapter?.notifyDataSetChanged()
                } else {
                    searchHistoryVisib(SearchHistoryVisibility.GONE)
                    recyclerViewSongs.adapter = AdapterSearch(listOfSongs)
                    recyclerViewSongs.adapter?.notifyDataSetChanged()
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                textSearch = searchField.text.toString()
            }

            fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

        searchField.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus && searchField.text.isEmpty() && SearchHistory.notEmpty()) {
                searchHistoryVisib(SearchHistoryVisibility.VISIBLE)
                SearchHistory.refreshHistory()
                recyclerViewSongs.adapter = AdapterSearchHistory(SearchHistory.getHistory())
                recyclerViewSongs.adapter?.notifyDataSetChanged()

            } else searchHistoryVisib(SearchHistoryVisibility.GONE)

        }

        searchField.addTextChangedListener(searchActivityTextWatcher)
    }

    private fun search() {
        searchProgressBar.visibility = View.VISIBLE

        iTunesAPIService.searchTrack(text = searchField.text.toString())
            .enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.resultCount!! > 0) {
                                listOfSongs.clear()
                                listOfSongs.addAll(response.body()?.results!!)
                                listOfSongs.forEach { it.changeFormat() }
                                recyclerViewSongs.adapter?.notifyDataSetChanged()
                                errorVisibility(
                                    SearchActItemsVisib.SUCCESS
                                ) //запрос выполнен успешно
                            } else {
                                errorVisibility(
                                    SearchActItemsVisib.EMPTY_SEARCH
                                ) // получен ответ от сервера, но передано 0 элементов
                            }
                        }
                        else -> {
                            errorVisibility(
                                SearchActItemsVisib.CONNECTION_ERROR
                            ) // прочие ошибки
                        }
                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    errorVisibility(
                        SearchActItemsVisib.CONNECTION_ERROR
                    ) // прочие ошибки
                }
            })
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRequest)
        handler.postDelayed(searchRequest, SEARCH_DEBOUNCE_DELAY1)
    }

    fun searchHistoryVisib(visibility: SearchHistoryVisibility) {
        when (visibility) {
            SearchHistoryVisibility.VISIBLE -> textViewSearchHistory.visibility = View.VISIBLE
            SearchHistoryVisibility.GONE -> textViewSearchHistory.visibility = View.GONE
        }
        buttonClearSearchHistory.visibility = textViewSearchHistory.visibility
    }

    /**
     * Функция позволяющая скрывать или показывать визуальные элементы, уведомляющие пользователя
     * о наличии ошибки при выполнении поискового запроса.
     * Переменные recycle, liner, button - соответствуют состояню параметра visibility соответствующих View
     */

    private fun errorVisibility(result: SearchActItemsVisib) {
        val linearLayoutDownloadFail: LinearLayout = findViewById(R.id.linearlayout_download_fail)
        val downloadFailTextView: TextView = findViewById(R.id.textview_download_fail)
        val downloadFailImageView: ImageView = findViewById(R.id.imageview_download_fail)

        when (result) {
            SearchActItemsVisib.SUCCESS -> {
                recyclerViewSongs.visibility = View.VISIBLE
                linearLayoutDownloadFail.visibility = View.GONE
                downloadFailButton.visibility = View.GONE

            }

            SearchActItemsVisib.EMPTY_SEARCH -> {
                recyclerViewSongs.visibility = View.GONE
                linearLayoutDownloadFail.visibility = View.VISIBLE
                downloadFailButton.visibility = View.GONE
                downloadFailImageView.setImageResource(R.drawable.serch_zero)
                downloadFailTextView.setText(R.string.search_fail)
            }
            SearchActItemsVisib.CONNECTION_ERROR -> {
                recyclerViewSongs.visibility = View.GONE
                linearLayoutDownloadFail.visibility = View.VISIBLE
                downloadFailButton.visibility = View.VISIBLE
                downloadFailImageView.setImageResource(R.drawable.no_internet_connection)
                downloadFailTextView.setText(R.string.internet_lost_connection)
            }
        }
        searchProgressBar.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(textOfSearch, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(textOfSearch, "")
    }
}


enum class SearchActItemsVisib {
    CONNECTION_ERROR,
    EMPTY_SEARCH,
    SUCCESS
}

enum class SearchHistoryVisibility {
    VISIBLE,
    GONE
}