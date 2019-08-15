// Copyright (c) 2019 . Wilberforce Uwadiegwu. All Rights Reserved.

package com.jadebyte.jadeplayer.main.playlist

import android.app.Application
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.StringRes
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hunter.library.debug.HunterDebug
import com.jadebyte.jadeplayer.R
import com.jadebyte.jadeplayer.common.App
import com.jadebyte.jadeplayer.main.common.utils.ImageUtils
import com.jadebyte.jadeplayer.main.common.utils.UriFileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class WritePlaylistViewModel(application: Application) : AndroidViewModel(application) {

    private val _data = MutableLiveData<WriteResponse>()
    val data: LiveData<WriteResponse> get() = _data

    fun createPlaylist(playlistName: String, tempThumbUri: Uri?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
                val values = ContentValues(1)
                values.put(MediaStore.Audio.Playlists.NAME, playlistName)
                val playlistUri = getApplication<App>().contentResolver.insert(uri, values)

                if (playlistUri?.toString().isNullOrEmpty()) {
                    _data.postValue(WriteResponse(false, R.string.something_went_wrong))
                    return@withContext
                }

                writeImageFile(ContentUris.parseId(playlistUri), tempThumbUri)
                _data.postValue(WriteResponse(true))
            }
        }

    }

    fun editPlaylist(name: String, id: Long, tempThumbUri: Uri?, deleteImageFile: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
                val values = ContentValues(2)
                val where = MediaStore.Audio.Playlists._ID + " =? "
                val whereVal = arrayOf(id.toString())
                values.put(MediaStore.Audio.Playlists.NAME, name)
                values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis())
                val rowsUpdated = getApplication<App>().contentResolver.update(uri, values, where, whereVal)
                if (rowsUpdated < 1) {
                    _data.postValue(WriteResponse(false, R.string.sth_went_wrong))
                    return@withContext
                }

                writeImageFile(id, tempThumbUri, deleteImageFile)
                _data.postValue(WriteResponse(true))
            }
        }
    }

    fun deletePlaylist(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val where = MediaStore.Audio.Playlists._ID + "=?"
                val whereVal = arrayOf(id.toString())
                val rows = getApplication<Application>().contentResolver
                    .delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereVal)
                if (rows > 0) {
                    writeImageFile(id, deleteImageFile = true)
                    _data.postValue(WriteResponse(true, R.string.sth_deleted))
                } else {
                    _data.postValue(WriteResponse(false, R.string.sth_went_wrong))
                }
            }
        }
    }

    @HunterDebug
    @WorkerThread
    private fun writeImageFile(playlistId: Long, tempThumbUri: Uri? = null, deleteImageFile: Boolean = false) {
        val app = getApplication<App>()
        val resultPath = ImageUtils.getImagePathForPlaylist(playlistId, app)

        if (deleteImageFile) {
            val file = File(resultPath)
            if (file.exists()) file.delete()
        }

        if (tempThumbUri == null) return

        val path = UriFileUtils.getPathFromUri(app, tempThumbUri)
        if (path != null) {
            if (resultPath != null) {
                ImageUtils.resizeImageIfNeeded(path, 300.0, 300.0, 80, resultPath)
            }
        }
        return
    }
}

data class WriteResponse(val success: Boolean, @StringRes val message: Int? = null)