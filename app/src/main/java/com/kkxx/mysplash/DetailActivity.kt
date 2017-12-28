package com.kkxx.mysplash

import android.animation.Animator
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.transition.Transition
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.lzyzsd.circleprogress.DonutProgress
import com.kkxx.mysplash.animation.CustomAnimatorListener
import com.kkxx.mysplash.animation.CustomTransitionListener
import com.kkxx.mysplash.utils.DisplayUtils
import com.kkxx.mysplash.utils.ImageHelper
import com.kkxx.mysplash.utils.SplashUtils
import com.kkxx.mysplash.view.FreedomImageView
import com.koushikdutta.ion.Ion
import com.koushikdutta.ion.ProgressCallback
import com.koushikdutta.ion.future.ResponseFuture
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import java.io.File
import java.io.InputStream

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
    private lateinit var mTitleContainer: View
    private lateinit var mTitlesContainer: View
    private lateinit var useImgBtn: ImageButton
    private lateinit var shareImgBtn: ImageButton
    private lateinit var downloadImgBtn: ImageButton
    private val ANIMATION_DURATION_MEDIUM = 300L
    private val ANIMATION_DURATION_SHORT = 150
    private val ANIMATION_DURATION_LONG = 450
    private val ANIMATION_DURATION_EXTRA_LONG = 850
    private lateinit var authorTv: TextView
    private lateinit var dateTv: TextView
    private lateinit var author: String
    private lateinit var date: String
    private lateinit var bgColor: String
    private lateinit var mFabProgress: DonutProgress
    private lateinit var mProgressFabAnimation: Animation
    private var future: ResponseFuture<InputStream>? = null
    private var mDrawablePhoto: Drawable? = null
    private var mDrawableClose: Drawable? = null
    private var mDrawableSuccess: Drawable? = null
    private var mDrawableError: Drawable? = null
    private val ACTIVITY_CROP = 13451
    private val ACTIVITY_SHARE = 13452
    private lateinit var downloadUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_layout)
        author = intent.getStringExtra("img_author")
        date = intent.getStringExtra("img_date")
        position = intent.getIntExtra("click_index", Splash.illogicalParams)
        url = intent.getStringExtra("img_url")
        bgColor = intent.getStringExtra("bg_color")
        downloadUrl = intent.getStringExtra("img_down_url")
        mTitleContainer = findViewById(R.id.detailContainerLayout)
        mTitleContainer.setBackgroundColor(ImageHelper.computeCardBackgroundColor
        (this@DetailActivity, bgColor))
        DisplayUtils.configuredHideYView(mTitleContainer)
        mTitlesContainer = findViewById(R.id.detailTitles)
        useImgBtn = findViewById(R.id.imgUse)
        useImgBtn.scaleX = 0f
        useImgBtn.scaleY = 0f
        shareImgBtn = findViewById(R.id.imgShare)
        shareImgBtn.setOnClickListener(onShareButtonListener)
        downloadImgBtn = findViewById(R.id.imgDownload)
        downloadImgBtn.setOnClickListener(onDownloadButtonListener)
        authorTv = findViewById(R.id.detailAuthor)
        dateTv = findViewById(R.id.detailDate)
        sv = findViewById(R.id.sv)
        imgView = findViewById(R.id.imgDetail)
        imgView.setSize(intent.getIntExtra("img_width", 0),
                intent.getIntExtra("img_height", 0))
        imgUse = findViewById(R.id.imgUse)
        mFabProgress = findViewById(R.id.activity_detail_progress)
        mFabProgress.max = 100
        mFabProgress.scaleX = 0f
        mFabProgress.scaleY = 0f
        if (Splash.illogicalParams != position) {
            imgView.setImageDrawable(Splash.detailImg.get(position))
        } else {
            Glide.with(this@DetailActivity).load(url).into(imgView)
        }
        window.sharedElementEnterTransition.addListener(object : CustomTransitionListener() {

            override fun onTransitionEnd(p0: Transition?) {
                super.onTransitionEnd(p0)
                animateActivityStart()
            }
        })
        mDrawablePhoto = IconicsDrawable(this, FontAwesome.Icon.faw_camera).color(Color.WHITE).sizeDp(24)
        mDrawableClose = IconicsDrawable(this, FontAwesome.Icon.faw_times).color(Color.WHITE).sizeDp(24)
        mDrawableSuccess = IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(24)
        mDrawableError = IconicsDrawable(this, FontAwesome.Icon.faw_exclamation).color(Color.WHITE).sizeDp(24)
    }

    private fun animateActivityStart() {
        val showTitleAnimator = DisplayUtils.showViewByScale(mTitleContainer)
        showTitleAnimator.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator) {
                mTitlesContainer.startAnimation(AnimationUtils.loadAnimation(this@DetailActivity, R.anim.alpha_on))
                mTitlesContainer.visibility = View.VISIBLE

                //animate the fab
                DisplayUtils.showViewByScale(useImgBtn).setDuration(ANIMATION_DURATION_MEDIUM).start()

                //animate the share fab
                DisplayUtils.showViewByScale(shareImgBtn)
                        .setDuration(ANIMATION_DURATION_MEDIUM * 2)
                        .start()
                shareImgBtn.animate()
                        .translationX(-1 * DisplayUtils.convertPxToDp(this@DetailActivity, 58F))
                        .setStartDelay(ANIMATION_DURATION_MEDIUM.toLong())
                        .setDuration(ANIMATION_DURATION_MEDIUM.toLong())
                        .start()

                //animate the download fab
                DisplayUtils.showViewByScale(downloadImgBtn)
                        .setDuration(ANIMATION_DURATION_MEDIUM * 2)
                        .start()
                downloadImgBtn.animate()
                        .translationX(-1 * DisplayUtils.convertPxToDp(this@DetailActivity, 108F))
                        .setStartDelay(ANIMATION_DURATION_MEDIUM.toLong())
                        .setDuration(ANIMATION_DURATION_MEDIUM.toLong())
                        .start()
            }
        })
        showTitleAnimator.start()
        dateTv.text = date
        authorTv.text = author
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            downloadImgBtn.animate()
                    .translationX(0f)
                    .setDuration(ANIMATION_DURATION_MEDIUM.toLong())
                    .setListener(animationFinishListener1)
                    .start()


            //move the share fab below the normal fab (58 because this is the margin top + the half
            shareImgBtn.animate()
                    .translationX(0f)
                    .setDuration(ANIMATION_DURATION_MEDIUM.toLong())
                    .setListener(animationFinishListener1)
                    .start()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private val progressCallback = ProgressCallback { downloaded, total ->
        var progress = (downloaded * 100.0 / total).toInt()
        if (progress < 1) {
            progress += 1
        }
        mFabProgress.progress = progress.toFloat()
    }

    private fun animateStart() {
        //reset progress to prevent jumping
        mFabProgress.progress = 0f

        //hide the share fab
        shareImgBtn.animate().translationX(0f).setDuration(ANIMATION_DURATION_SHORT.toLong()).start()
        downloadImgBtn.animate().translationX(0f).setDuration(ANIMATION_DURATION_SHORT.toLong()).start()

        //some nice button animations
        DisplayUtils.showViewByScale(mFabProgress).setDuration(ANIMATION_DURATION_MEDIUM).start()
        mFabProgress.progress = 1f

        mProgressFabAnimation = RotateAnimation(0.0f, 360.0f, (mFabProgress.width / 2).toFloat(), (mFabProgress.height / 2).toFloat()) as Animation
        mProgressFabAnimation.duration = (ANIMATION_DURATION_EXTRA_LONG * 2).toLong()
        mProgressFabAnimation.interpolator = LinearInterpolator()
        mProgressFabAnimation.repeatCount = Animation.INFINITE
        mProgressFabAnimation.repeatMode = -1
        mFabProgress.startAnimation(mProgressFabAnimation)

        useImgBtn.setImageDrawable(mDrawableClose)

        //animate the button back to blue. just do it the first time
        if (useImgBtn.tag != null) {
            val transition = useImgBtn.background as TransitionDrawable
            transition.reverseTransition(ANIMATION_DURATION_LONG)
            useImgBtn.tag = null
        }

        if (shareImgBtn.tag != null) {
            val transition = shareImgBtn.background as TransitionDrawable
            transition.reverseTransition(ANIMATION_DURATION_LONG)
            shareImgBtn.tag = null
        }

        if (downloadImgBtn.tag != null) {
            val transition = downloadImgBtn.background as TransitionDrawable
            transition.reverseTransition(ANIMATION_DURATION_LONG)
            downloadImgBtn.tag = null
        }
    }

    private val onShareButtonListener = View.OnClickListener {
        if (future == null) {
            //prepare the call
//            future = Ion.with(this@DetailActivity)
//                    .load(mSelectedImage.getHighResImage(mWallpaperWidth, mWallpaperHeight))
//                    .progressHandler(progressCallback)
//                    .asInputStream()

            animateStart()

            useImgBtn.animate().rotation(360f).setDuration(ANIMATION_DURATION_LONG.toLong()).setListener(object : CustomAnimatorListener() {
                override fun onAnimationEnd(animation: Animator) {
                    downloadAndSetOrShareImage(false)
                    super.onAnimationEnd(animation)
                }

                override fun onAnimationCancel(animation: Animator) {
                    downloadAndSetOrShareImage(false)
                    super.onAnimationCancel(animation)
                }
            }).start()
        } else {
            animateReset(false)
        }
    }

    private val onDownloadButtonListener = View.OnClickListener {
        if (future == null) {
            if (!SplashUtils.isExternalStorageWritable()) {
                Toast.makeText(this@DetailActivity, R.string.error_no_storage, Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            //prepare the call
            future = Ion.with(this@DetailActivity)
                    .load(downloadUrl)
                    .progressHandler(progressCallback)
                    .asInputStream()
            Log.d("DetailActivity", "---download url----- " + downloadUrl);
            animateStart()

            useImgBtn.animate().rotation(360f).setDuration(ANIMATION_DURATION_LONG.toLong()).setListener(object : CustomAnimatorListener() {
                override fun onAnimationEnd(animation: Animator) {
                    downloadImage(null)
                    super.onAnimationEnd(animation)
                }

                override fun onAnimationCancel(animation: Animator) {
                    downloadImage(null)
                    super.onAnimationCancel(animation)
                }
            }).start()
        } else {
            animateReset(false)
        }
    }

    private fun downloadImage(customLocation: String?) {
        if (future != null) {
            //set the callback and start downloading
            future!!.withResponse().setCallback { e, result ->
                var success = false
                if (e == null && result != null && result.result != null) {
                    try {
                        //prepare the file name
                        val url = downloadUrl
                        val fileName = url.substring(url.lastIndexOf('/') + 1, url.length) + ".jpg"

                        val dir: File
                        if (TextUtils.isEmpty(customLocation)) {
                            //create a temporary directory within the cache folder
                            dir = SplashUtils.getAlbumStorageDir("mysplash")
                        } else {
                            dir = File(customLocation!!)
                        }
                        //create the file
                        val file = File(dir, fileName)
                        if (!file.exists()) {
                            file.createNewFile()
                        }

                        //copy the image onto this file
                        SplashUtils.copyInputStreamToFile(result.result, file)

                        //animate the first elements
                        animateCompleteFirst(true)

                        success = true
                    } catch (ex: Exception) {
                        Log.e("un:splash", ex.toString())
                    }

                    //animate after complete
                    animateComplete(success)
                } else {
                    animateReset(true)
                }
            }
        }
    }

    private fun downloadAndSetOrShareImage(set: Boolean) {
        if (future != null) {
            //set the callback and start downloading
            future!!.withResponse().setCallback { e, result ->
                var success = false
                if (e == null && result != null && result.result != null) {
                    try {
                        //create a temporary directory within the cache folder
                        val dir = File(this@DetailActivity.cacheDir.toString() + "/images")
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }

                        //create the file
                        val file = File(dir, "unsplash.jpg")
                        if (!file.exists()) {
                            file.createNewFile()
                        }

                        //copy the image onto this file
                        SplashUtils.copyInputStreamToFile(result.result, file)

                        //get the contentUri for this file and start the intent
                        val contentUri = FileProvider.getUriForFile(this@DetailActivity, "com.kkxx.fileprovider", file)

                        if (set) {
                            //get crop intent
                            val intent = WallpaperManager.getInstance(this@DetailActivity).getCropAndSetWallpaperIntent(contentUri)
                            //start activity for result so we can animate if we finish
                            this@DetailActivity.startActivityForResult(intent, ACTIVITY_CROP)
                        } else {
                            //share :D
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.data = contentUri
                            shareIntent.type = "image/jpg"
                            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                            //start activity for result so we can animate if we finish
                            this@DetailActivity.startActivityForResult(Intent.createChooser(shareIntent, "Share Via"), ACTIVITY_SHARE)
                        }

                        success = true
                    } catch (ex: Exception) {
                        Log.e("un:splash", ex.toString())
                        ex.printStackTrace()
                    }

                    //animate after complete
                    animateComplete(success)
                } else {
                    animateReset(true)
                }
            }
        }
    }

    /**
     * animate the reset of the view
     */
    private fun animateReset(error: Boolean) {
        future!!.cancel(true)
        future = null

        //animating everything back to default :D
        DisplayUtils.hideViewByScaleXY(mFabProgress).setDuration(ANIMATION_DURATION_MEDIUM).start()
        mProgressFabAnimation.cancel()
        //SplashUtils.animateViewElevation(mFabButton, 0, mElavationPx);

        if (error) {
            useImgBtn.setImageDrawable(mDrawableError)
        } else {
            useImgBtn.setImageDrawable(mDrawablePhoto)
        }

        useImgBtn.animate().rotation(360f).setDuration(ANIMATION_DURATION_MEDIUM).start()

        shareImgBtn.animate().translationX(-1 * DisplayUtils.convertPxToDp(this@DetailActivity,
                58F)).setDuration(ANIMATION_DURATION_MEDIUM).start()
        downloadImgBtn.animate().translationX(-1 * DisplayUtils.convertPxToDp
        (this@DetailActivity, 108F)).setDuration(ANIMATION_DURATION_MEDIUM).start()
    }

    /**
     * animate the first parts of the UI after the download has successfully finished
     */
    private fun animateCompleteFirst(success: Boolean) {
        //some nice animations so the user knows the wallpaper was set properly
        useImgBtn.animate().rotation(720f).setDuration(ANIMATION_DURATION_EXTRA_LONG.toLong()).start()
        useImgBtn.setImageDrawable(mDrawableSuccess)

        //animate the button to green. just do it the first time
        if (useImgBtn.tag == null) {
            val transition = useImgBtn.getBackground() as TransitionDrawable
            transition.startTransition(ANIMATION_DURATION_LONG)
            useImgBtn.tag = ""
        }

        if (shareImgBtn.tag == null) {
            val transition = shareImgBtn.background as TransitionDrawable
            transition.startTransition(ANIMATION_DURATION_LONG)
            shareImgBtn.tag = ""
        }

        if (downloadImgBtn.tag == null) {
            val transition = downloadImgBtn.background as TransitionDrawable
            transition.startTransition(ANIMATION_DURATION_LONG)
            downloadImgBtn.tag = ""
        }
    }

    /**
     * finish the animations of the ui after the download is complete. reset the button to the start
     *
     * @param success
     */
    private fun animateComplete(success: Boolean) {
        //hide the progress again :D
        DisplayUtils.hideViewByScaleXY(mFabProgress).setDuration(ANIMATION_DURATION_MEDIUM).start()
        mProgressFabAnimation.cancel()

        //show the fab again ;)
        shareImgBtn.animate().translationX(-1 * DisplayUtils.convertPxToDp(this@DetailActivity,
                58F)).setDuration(ANIMATION_DURATION_MEDIUM).start()
        downloadImgBtn.animate().translationX(-1 * DisplayUtils.convertPxToDp(this@DetailActivity, 108F))
                .setDuration(ANIMATION_DURATION_MEDIUM).start()

        // if we were not successful remove the x again :D
        if (!success) {
            //SplashUtils.animateViewElevation(mFabButton, 0, mElavationPx);
            useImgBtn.setImageDrawable(mDrawablePhoto)
            useImgBtn.animate().rotation(360f).setDuration(ANIMATION_DURATION_MEDIUM.toLong()).start()
        }
        future = null
    }

    private val animationFinishListener1 = object : CustomAnimatorListener() {
        private var animateFinish1 = 0

        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            process()
        }

        override fun onAnimationCancel(animation: Animator) {
            super.onAnimationCancel(animation)
            process()
        }

        private fun process() {
            animateFinish1 = animateFinish1 + 1
            if (animateFinish1 >= 2) {
                //create the fab animation and hide fabProgress animation, set an delay so those will hide after the shareFab is below the main fab
                DisplayUtils.hideViewByScaleXY(downloadImgBtn)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .setListener(animationFinishListener2)
                        .start()
                DisplayUtils.hideViewByScaleXY(shareImgBtn)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .setListener(animationFinishListener2)
                        .start()
                DisplayUtils.hideViewByScaleXY(mFabProgress)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .setListener(animationFinishListener2)
                        .start()
                DisplayUtils.hideViewByScaleXY(useImgBtn)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .setListener(animationFinishListener2)
                        .start()
            }
        }
    }

    private val animationFinishListener2 = object : CustomAnimatorListener() {
        private var animateFinish2 = 0

        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            process()
        }

        override fun onAnimationCancel(animation: Animator) {
            super.onAnimationCancel(animation)
            process()
        }

        private fun process() {
            animateFinish2 += 1
            if (animateFinish2 >= 4) {
                val hideFabAnimator = DisplayUtils.hideViewByScaleY(mTitleContainer)
                hideFabAnimator.setListener(object : CustomAnimatorListener() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        onBackPressed()
                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
