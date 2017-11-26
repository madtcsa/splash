package com.kkxx.mysplash.model.unsplash.photo

import com.kkxx.mysplash.model.unsplash.category.PhotoCategory
import com.kkxx.mysplash.model.unsplash.collection.PhotoCollection
import com.kkxx.mysplash.model.unsplash.user.User

/**
 * author zsmj
 * Date 2017/11/12
 */
data class SplashPhoto(var id: String, var created_at: String, var updated_at: String, var width: Int,
                       var height: Int, var color: String, var likes: Int, var liked_by_user: Boolean,
                       var description: String, var user: User, var
                       current_user_collections: List<PhotoCollection>,
                       var urls: PhotoUrls, var categories: List<PhotoCategory>, var links: PhotoDownloadLinks)