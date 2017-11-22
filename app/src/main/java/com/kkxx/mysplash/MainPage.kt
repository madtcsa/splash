package com.kkxx.mysplash

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.kkxx.mysplash.adapter.SplashAdapter
import com.kkxx.mysplash.model.SplashViewModel
import com.kkxx.mysplash.model.unsplash.photo.SplashPhoto
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.app_bar_main_page.*

class MainPage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: SplashViewModel
    private var splashAdapter: SplashAdapter? = null
    private lateinit var splashView: XRecyclerView
    private lateinit var context: Context
    private var pageIndex: Int = 1
    private lateinit var fab: FloatingActionButton
    private lateinit var fabIV: ImageView
    private var splashCategoryId =Splash.illogicalParams
    private lateinit var circleMenu:FloatingActionMenu
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
        viewModel.getSplashList(pageIndex, splashCategoryId)!!.observe(this@MainPage,
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
        val toggle = ActionBarDrawerToggle(
                this@MainPage, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this@MainPage)
        initSplashListView()
        initCircleMenu()
    }

    private fun initCircleMenu() {

        fabIV = ImageView(this@MainPage)
        fabIV.setImageDrawable(resources.getDrawable(R.drawable.ic_action_new_light))
        fab = FloatingActionButton.Builder(this)
                .setContentView(fabIV)
                .setPosition(FloatingActionButton.POSITION_RIGHT_CENTER)
                .build()

        val sabBuilder: SubActionButton.Builder = SubActionButton.Builder(this@MainPage)
        sabBuilder.setBackgroundDrawable(resources.getDrawable(R.drawable.button_action_blue_selector))
        val blueSubActionButtonSize = resources.getDimensionPixelSize(R.dimen.blue_sub_action_button_size)
        val blueSubActionButtonContentMargin = resources.getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin)
        val fabActionMenuRadius = resources.getDimensionPixelSize(R.dimen.fab_action_menu_radius)
        val blueContentParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin)
        sabBuilder.setLayoutParams(blueContentParams)
        val blueParams = FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize)
        sabBuilder.setLayoutParams(blueParams)

        val buildingsIV = ImageView(this@MainPage)
        buildingsIV.setImageResource(R.drawable.ic_buildings)
        val foodDrinkIV = ImageView(this@MainPage)
        foodDrinkIV.setImageResource(R.drawable.ic_food_drink)
        val natureIV = ImageView(this@MainPage)
        natureIV.setImageResource(R.drawable.ic_nature)
        val objectsIV = ImageView(this@MainPage)
        objectsIV.setImageResource(R.drawable.ic_object)
        val peopleIV = ImageView(this@MainPage)
        peopleIV.setImageResource(R.drawable.ic_people)
        val technologyIV = ImageView(this@MainPage)
        technologyIV.setImageResource(R.drawable.ic_technology)

         circleMenu = FloatingActionMenu.Builder(this@MainPage)
                .addSubActionView(sabBuilder.setContentView(buildingsIV, blueContentParams).build())
                .addSubActionView(sabBuilder.setContentView(foodDrinkIV, blueContentParams).build())
                .addSubActionView(sabBuilder.setContentView(natureIV, blueContentParams).build())
                .addSubActionView(sabBuilder.setContentView(objectsIV, blueContentParams).build())
                .addSubActionView(sabBuilder.setContentView(peopleIV, blueContentParams).build())
                .addSubActionView(sabBuilder.setContentView(technologyIV, blueContentParams).build())
                .setRadius(fabActionMenuRadius)
                .attachTo(fab)
                .setStartAngle(90)
                .setEndAngle(270)
                .build()

        circleMenu.setStateChangeListener(object : FloatingActionMenu.MenuStateChangeListener {
            override fun onMenuOpened(menu: FloatingActionMenu) {
                val pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45F)
                val animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR)
                animation.start()
            }

            override fun onMenuClosed(menu: FloatingActionMenu) {
                val pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0F)
                val animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR)
                animation.start()
            }
        })
        buildingsIV.setOnClickListener {
            loadCategoryData(Splash.CATEGORY_BUILDINGS_ID)
        }
        foodDrinkIV.setOnClickListener {
            loadCategoryData(Splash.CATEGORY_BUILDINGS_ID)
        }
        natureIV.setOnClickListener { loadCategoryData(Splash.CATEGORY_NATURE_ID) }
        objectsIV.setOnClickListener { loadCategoryData(Splash.CATEGORY_OBJECTS_ID) }
        peopleIV.setOnClickListener { loadCategoryData(Splash.CATEGORY_PEOPLE_ID) }
        technologyIV.setOnClickListener { loadCategoryData(Splash.CATEGORY_TECHNOLOGY_ID) }
    }

    private fun loadCategoryData(categoryId: Int) {
        pageIndex = 1
        splashCategoryId = categoryId
        circleMenu.close(true)
        loadData(false)
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
