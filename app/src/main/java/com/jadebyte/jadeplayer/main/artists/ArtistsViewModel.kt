// Copyright (c) 2019 . Wilberforce Uwadiegwu. All Rights Reserved.

package com.jadebyte.jadeplayer.main.artists

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import com.jadebyte.jadeplayer.main.common.data.BaseMediaStoreRepository
import com.jadebyte.jadeplayer.main.common.view.BaseMediaStoreViewModel

/**
 * Created by Wilberforce on 2019-04-25 at 00:57.
 */
class ArtistsViewModel(application: Application) : BaseMediaStoreViewModel<Artist>(application) {
    override var repository: BaseMediaStoreRepository<Artist> = ArtistsRepository(application)

    override var sortOrder: String? = "${MediaStore.Audio.Albums.ARTIST} COLLATE NOCASE ASC"

    override var uri: Uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI

    override var projection: Array<String>? = arrayOf(
        MediaStore.Audio.Artists.ARTIST,
        MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
        MediaStore.Audio.Artists._ID
    )
}
