package com.kkxx.mysplash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.kkxx.mysplash.adapter.SplashAdapter
import com.kkxx.mysplash.model.SplashViewModel
import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.app_bar_main_page.*

class MainPage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: SplashViewModel
    private var splashAdapter: SplashAdapter? = null
    private lateinit var splashView: XRecyclerView
    private lateinit var context: Context
    private var pageIndex: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        context = this
        initView()
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        loadData(false)
        splashView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> Glide.with(this@MainPage).resumeRequests()
                    else -> Glide.with(this@MainPage).pauseRequests()
                }
            }
        })
    }

    private fun loadData(isMore: Boolean) {
        viewModel.getSplashList(pageIndex)!!.observe(this@MainPage,
                Observer<List<SplashPhoto>> { t: List<SplashPhoto>? ->
                    if (splashAdapter == null) {
                        splashAdapter = SplashAdapter(t!!, context)
                        splashView.adapter = splashAdapter
                    } else {
                        if (isMore) {
                            splashAdapter!!.appendSplashInfos(t!!)
                            splashView.loadMoreComplete()
                        } else {
                            splashAdapter!!.setSplashInfos(t!!)
                            splashView.refreshComplete()
                        }
                    }
                })
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val toggle = ActionBarDrawerToggle(
                this@MainPage, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this@MainPage)
        initSplashListView()
    }

    private fun initSplashListView() {
        splashView = findViewById(R.id.splashRecyclerView)
        splashView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager
                .VERTICAL)
        splashView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader)
        splashView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate)
        splashView.setArrowImageView(R.drawable.iconfont_downgrey)
        splashView.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                ++pageIndex
                loadData(true)
            }

            override fun onRefresh() {
                pageIndex = 1
                loadData(false)
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
