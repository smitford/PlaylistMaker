package com.example.playlistmaker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.trackRecycleView.CustomRecyclerAdapter
import com.example.playlistmaker.trackRecycleView.ITunesResponse
import com.example.playlistmaker.trackRecycleView.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var textSearch: String
    var listOfSongs = mutableListOf<Track>()
    lateinit var recyclerViewSongs: RecyclerView
    private lateinit var downloadFailButton: Button

    private val iTunesAPI = "https://itunes.apple.com"  //Base URL for iTunesAPI

    // Initialisation of retrofit component
    private val retrofitITunes =
        Retrofit.Builder()
            .baseUrl(iTunesAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesAPIService = retrofitITunes.create(ITunesAPI::class.java)

    interface ITunesAPI {
        @GET("/search?entity=song")
        fun searchTrack(@Query("term") text: String): Call<ITunesResponse>
    }

    companion object {
        const val textOfSearch = "TEXT_OF_SEARCH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerViewSongs = findViewById(R.id.recyclerView_songs)
        recyclerViewSongs.layoutManager = LinearLayoutManager(this)
        recyclerViewSongs.adapter = CustomRecyclerAdapter(listOfSongs)

        downloadFailButton= findViewById(R.id.button_download_fail)

        val buttonBack = findViewById<Button>(R.id.back_button_search_act)
        buttonBack.setOnClickListener {
            finish()
        }


        inputEditText = findViewById(R.id.search_bar)

        if (savedInstanceState != null) {
            inputEditText.setText(textSearch)
        }

        downloadFailButton.setOnClickListener { search() }

        val clearButton = findViewById<Button>(R.id.clear_text_search)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            listOfSongs.clear()
            recyclerViewSongs.adapter?.notifyDataSetChanged()
        }

        val searchActivityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }

            fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                listOfSongs.clear()
                search()
            }
            false
        }
        inputEditText.addTextChangedListener(searchActivityTextWatcher)
    }

    private fun search() {

        iTunesAPIService.searchTrack(text = inputEditText.text.toString())
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
                                recyclerViewSongs.adapter?.notifyDataSetChanged()
                                errorVisibility(
                                    recycle = 0,
                                    liner = 8,
                                    button = 8
                                ) //запрос выполнен успешно

                            } else {
                                errorVisibility(
                                    recycle = 8,
                                    liner = 0,
                                    button = 8
                                ) // получен ответ от сервера, но передано 0 элементов
                            }
                        }
                        else -> {
                            errorVisibility(recycle = 8, liner = 0, button = 0) // прочие ошибки
                        }
                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    errorVisibility(recycle = 8, liner = 0, button = 0) // прочие ошибки
                }
            })
    }


    /**
     * Функция позволяющая скрывать или показывать визуальные элементы, уведомляющие пользователя
     * о наличии ошибки при выполнении поискового запроса.
     * Переменные recycle, liner, button - соответствуют состояню параметра visibility соответствующих View
     * 0 - visible, 8 - gone. При значении button - 0 текст передаваемый в TextView: "Проблема со связью
     * Загрузка не удалась. Проверьте подключение к интернету".
     */

    private fun errorVisibility(recycle: Int, liner: Int, button: Int) {
        val linearLayoutDownloadFail: LinearLayout = findViewById(R.id.linearlayout_download_fail)
        val downloadFailTextView: TextView = findViewById(R.id.textview_download_fail)
        val downloadFailImageView: ImageView = findViewById(R.id.imageview_download_fail)


        if (button == 0) {
            downloadFailImageView.setImageResource(R.drawable.no_internet_connection)
            downloadFailTextView.setText(R.string.internet_lost_connection)
        } else {
            downloadFailImageView.setImageResource(R.drawable.serch_zero)
            downloadFailTextView.setText(R.string.search_fail)
        }
        recyclerViewSongs.visibility = recycle
        linearLayoutDownloadFail.visibility = liner
        downloadFailButton.visibility = button

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