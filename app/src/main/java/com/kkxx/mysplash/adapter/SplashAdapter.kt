package com.kkxx.mysplash.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kkxx.mysplash.R
import com.kkxx.mysplash.model.SplashInfo

/**
 * @author chenwei
 * 2017/11/3
 */
class SplashAdapter(splashList: List<SplashInfo>, context: Context) : RecyclerView.Adapter<SplashAdapter.SplashHolder>() {

    var splashInfoes: List<SplashInfo> = splashList
    var context: Context = context

    override fun getItemCount(): Int {
        return splashInfoes.size
    }

    override fun onBindViewHolder(holder: SplashHolder, position: Int) {
        val splash = splashInfoes[position]
        holder.splashDate.text = splash.date
        holder.splashAuthor.text = splash.author
        Glide.with(context).load(splash.preUrl).into(holder.splashPreImage)
        if (splash.isFavorite) {
            holder.splashCollect.setBackgroundResource(android.R.drawable.star_big_on)
        } else {
            holder.splashCollect.setBackgroundResource(android.R.drawable.star_big_off)
        }
        holder.splashAddress.text = splash.address
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SplashHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.splash_item_layout, null)
        return SplashHolder(itemView)
    }

    class SplashHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var splashPreImage: ImageView = itemView.findViewById(R.id.splashPreviewImg)
        var splashCollect: ImageView = itemView.findViewById(R.id.splashCollect)
        var splashAuthor: TextView = itemView.findViewById(R.id.splashAuthor)
        var splashDate: TextView = itemView.findViewById(R.id.splashDate)
        var splashAddress: TextView = itemView.findViewById(R.id.splashAddress)
    }

    fun setSplashInfos(splashList: List<SplashInfo>) {
        this.splashInfoes = splashList
        notifyDataSetChanged()
    }
}