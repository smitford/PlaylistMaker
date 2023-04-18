package com.example.playlistmaker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.trackRecycleView.CustomRecyclerAdapter
import com.example.playlistmaker.trackRecycleView.Track


class SearchActivity : AppCompatActivity() {

    lateinit var inputEditText: EditText
    var textSearch: String = String()


    companion object {
        const val textOfSearch = "TEXT_OF_SEARCH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerViewSongs: RecyclerView = findViewById(R.id.recyclerView_songs)

        recyclerViewSongs.layoutManager = LinearLayoutManager(this)
        recyclerViewSongs.adapter = CustomRecyclerAdapter(fillList())

        val buttonBack = findViewById<Button>(R.id.back_button_search_act)
        buttonBack.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.search_bar)

        if (savedInstanceState != null) {
            inputEditText.setText(textSearch)
        }


        val clearButton = findViewById<Button>(R.id.clear_text_search)

        clearButton.setOnClickListener {
            inputEditText.setText("")
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
        inputEditText.addTextChangedListener(searchActivityTextWatcher)


    }

    private fun fillList(): List<Track>{
        val data = mutableListOf<Track>()
        val track = this.resources.getStringArray(R.array.tracks)
        val trackTime = this.resources.getStringArray(R.array.trackTime)
        val artistName = this.resources.getStringArray(R.array.artistName)
        val artworkUrl = this.resources.getStringArray(R.array.artworkUrl)

        for (i in 0..4){
            data.add(Track(trackName = track[i], artistName = artistName[i], trackTime = trackTime[i], artworkUrl100 = artworkUrl[i]))
        }
        return data
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