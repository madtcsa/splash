package com.kkxx.mysplash.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kkxx.mysplash.R
import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto
import com.kkxx.mysplash.utils.ImageHelper
import com.kkxx.mysplash.view.FreedomImageView
import java.util.*

/**
 * @author chenwei
 * 2017/11/3
 */
class SplashAdapter(splashList: List<SplashPhoto>, context: Context) : RecyclerView.Adapter<SplashAdapter.SplashHolder>() {

    var splashInfoes: List<SplashPhoto> = splashList
    var context: Context = context

    override fun getItemCount(): Int {
        return splashInfoes.size
    }

    override fun onBindViewHolder(holder: SplashHolder, position: Int) {
        val splashPhoto = splashInfoes[position]
        holder.cardView.setBackgroundColor(ImageHelper.computeCardBackgroundColor(context,
                splashPhoto.color))
        holder.splashDate.text = splashPhoto.created_at
        holder.splashAuthor.text = splashPhoto.user.name
        holder.splashPreImage.setSize(splashPhoto.width, splashPhoto.height)
        holder.splashPreImage.setShowShadow(false)
        Glide.with(context).load(splashPhoto.urls.regular).into(holder.splashPreImage)
        if (splashPhoto.liked_by_user) {
            holder.splashCollect.setBackgroundResource(android.R.drawable.star_big_on)
        } else {
            holder.splashCollect.setBackgroundResource(android.R.drawable.star_big_off)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SplashHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.splash_item_layout, null)
        return SplashHolder(itemView)
    }

    class SplashHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView = itemView.findViewById(R.id.cardview)
        var splashPreImage: FreedomImageView = itemView.findViewById(R.id.splashPreviewImg)
        var splashCollect: ImageView = itemView.findViewById(R.id.splashCollect)
        var splashAuthor: TextView = itemView.findViewById(R.id.splashAuthor)
        var splashDate: TextView = itemView.findViewById(R.id.splashDate)
    }

    fun setSplashInfos(splashList: List<SplashPhoto>) {
        this.splashInfoes = splashList
        notifyDataSetChanged()
    }

    fun appendSplashInfos(splashList: List<SplashPhoto>) {
        if(this.splashInfoes is ArrayList) {
            (this.splashInfoes as ArrayList<SplashPhoto>).addAll(splashList)
        }
        Log.d("SplashAdapter","-----size----- "+this.splashInfoes.size)
        notifyDataSetChanged()
    }
}