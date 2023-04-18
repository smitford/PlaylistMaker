package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText

class SearchActivity : AppCompatActivity() {

    var inputEditText: EditText? = null

    var textSearch: String = String()

    companion object {
        const val textOfSearch = "textOfSearch"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<Button>(R.id.back_button_search_act)
        buttonBack.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.search_bar)

        if (savedInstanceState != null) {
            inputEditText!!.setText(textSearch)
        }


        val clearButton = findViewById<Button>(R.id.clear_text_search)

        clearButton.setOnClickListener {
            inputEditText!!.setText("")
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
        inputEditText!!.addTextChangedListener(searchActivityTextWatcher)


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(textOfSearch, textSearch)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(textOfSearch, "не работает")
    }


}