import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.THEME_PREFERENCES_AND_HISTORY

fun sharedPreferencesInit(context: Context): SharedPreferences =
   context.getSharedPreferences(THEME_PREFERENCES_AND_HISTORY, MODE_PRIVATE)