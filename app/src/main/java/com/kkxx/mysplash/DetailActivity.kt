package com.kkxx.mysplash

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ScrollView
import com.bumptech.glide.Glide
import com.kkxx.mysplash.utils.DisplayUtils
import com.kkxx.mysplash.utils.ImageHelper
import com.kkxx.mysplash.view.FreedomImageView

/**
 * @author zsmj
 * @date 2017/11/26
 */

class DetailActivity : AppCompatActivity() {

    private lateinit var imgView: FreedomImageView;
    private var position: Int = Splash.illogicalParams
    private lateinit var url: String
    private lateinit var imgUse: ImageButton
    private lateinit var sv: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_layout)
        position = intent.getIntExtra("click_index", Splash.illogicalParams)
        url = intent.getStringExtra("img_url")
        sv = findViewById(R.id.sv)
        sv.setBackgroundColor(ImageHelper.computeCardBackgroundColor(this@DetailActivity,
                intent.getStringExtra("bg_color")))
        imgView = findViewById(R.id.imgDetail)
        imgView.setSize(intent.getIntExtra("img_width", 0),
                intent.getIntExtra("img_height", 0))
        imgUse = findViewById(R.id.imgUse)
        if (Splash.illogicalParams != position) {
            imgView.setImageDrawable(Splash.detailImg.get(position))
        } else {
            Glide.with(this@DetailActivity).load(url).into(imgView)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
