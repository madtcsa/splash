package com.kkxx.mysplash.model.unsplash.collection

import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto
import com.kkxx.mysplash.model.unsplash.user.User

/**
 * author zsmj
 * Date 2017/11/12
 */
data class PhotoCollection(var id: Int, var title: String, var description: String, var published_at: String,
                           var updated_at: String, var curated: Boolean, var total_photos: Int, var
                           privateX: Boolean, var share_key: String, var cover_photo: SplashPhoto,
                           var user: User, var links: CollectionLinks) {
}