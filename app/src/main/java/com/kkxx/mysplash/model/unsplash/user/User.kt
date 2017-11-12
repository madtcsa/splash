package com.kkxx.mysplash.model.unsplash.user

/**
 * author chenwei
 * Date 2017/11/11
 */
data class User(var id:String, var updated_at:String, var username:String, var name:String,
                var first_name:String, var last_name:String, var twitter_username:String,
                var portfolio_url:String, var bio:String, var location:String, var total_likes:Int,
                var total_photos:Int, var total_collections:Int, var
                profile_image: UserProfileImageUrl, var links: UserLinks)