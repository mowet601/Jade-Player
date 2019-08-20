// Copyright (c) 2019 . Wilberforce Uwadiegwu. All Rights Reserved.

package com.jadebyte.jadeplayer.main.songs

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import com.jadebyte.jadeplayer.main.common.data.BaseMediaStoreRepository
import com.jadebyte.jadeplayer.main.common.view.BaseMediaStoreViewModel

/**
 * Created by Wilberforce on 19/04/2019 at 15:06.
 */
open class SongsViewModel(application: Application) : BaseMediaStoreViewModel<Song>(application) {

    override var repository: BaseMediaStoreRepository<Song> = SongsRepository(application)

    override var selection: String? = basicSongsSelection

    override var selectionArgs: Array<String>? = basicSongsSelectionArgs

    override var sortOrder: String? = basicSongsSongsOrder

    override var uri: Uri = basicSongUri

    override var projection: Array<String>? = basicSongsProjection
}

val basicSongsProjection = arrayOf(
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.ALBUM_ID,
    MediaStore.Audio.Media.ALBUM,
    MediaStore.Audio.Media.DURATION,
    MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.TRACK,
    MediaStore.Audio.Media.ALBUM_ID,
    MediaStore.Audio.Media.ARTIST_ID,
    MediaStore.Audio.Media.TITLE_KEY,
    MediaStore.Audio.Media.ALBUM_KEY,
    MediaStore.Audio.Media._ID
)

// Sort with the title in ascending case-insensitive order
const val basicSongsSongsOrder = "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"

const val basicSongsSelection = "${MediaStore.Audio.Media.IS_MUSIC} != ?"

val basicSongUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

val basicSongsSelectionArgs get() = arrayOf(basicSongsSelectionArg)

const val basicSongsSelectionArg = "0"