package com.example.playlistmaker.data.store

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.domain.api.InternalDataChangerRepository
import com.example.playlistmaker.ui.createPlaylist.CreatePlaylistFragment
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class InternalDataChangerRepositoryImp : InternalDataChangerRepository {
    override fun deleteImg(uri: String) {
        uri.toUri().path?.let { File(it) }?.delete()
    }

    override fun saveImg(uri: String, context: Context, playlistName: String) {

        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            CreatePlaylistFragment.PLAYLIST_IMAGE_DIRECTORY
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(
            filePath,
            playlistName + Calendar.getInstance().timeInMillis
        )
        val inputStream = context.contentResolver.openInputStream(uri.toUri())
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 20, outputStream)


    }
}