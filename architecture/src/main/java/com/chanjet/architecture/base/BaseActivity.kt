package com.chanjet.architecture.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chanjet.architecture.Constants
import com.chanjet.architecture.R
import com.chanjet.architecture.widget.ArchitectToolbar
import javax.inject.Inject

/**
 * Created by liuyicen on 2019-07-03 10:30.
 */
abstract class BaseActivity<VM : ViewModel, VB : ViewDataBinding> : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected var mBinding: VB? = null
    protected lateinit var mViewModel: VM
    protected lateinit var pre: BasePresenter
    protected var mToolbar: ArchitectToolbar? = null
    private var mThemeColor = Color.WHITE
    private var mExitReceiver: ExitBroadcastReceiver? = ExitBroadcastReceiver()

    protected abstract fun getLayoutId(): Int
    protected abstract fun getViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getOverrideContentView())
            mBinding = setContentView(this, getLayoutId())
        else {
            initTheme()
            setContentView(R.layout.activity_base)
            val rootView = findViewById<LinearLayout>(R.id.root_layout)
            mBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), rootView, false)
            initImmersionBar()
            initToolbar(rootView)
            if (mBinding != null) {
                rootView.addView(mBinding?.root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            } else {
                rootView.addView(layoutInflater.inflate(getLayoutId(), null))
            }
        }
        mViewModel = getViewModel()
        pre = getPresenter()
        registerExitReceiver()
    }

    abstract fun getPresenter(): BasePresenter

    /**
     * 沉浸式布局设置
     */
    private fun initImmersionBar() {

    }

    private fun initTheme() {
        if (mThemeColor == Color.WHITE) {
            val typedValue = TypedValue()
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            mThemeColor = typedValue.data
        }
    }

    /**
     * 也可在ActivityLifecycleCallbacks中动态注入，在XML里引入布局，比较适合ToolBar样式多变的App
     * ToolBar样式单一的App，可以考虑统一实现，提高开发效率
     */
    protected fun initToolbar(rootView: ViewGroup) {
        val toolBar = getToolBar()
        //            toolBar.setBackgroundColor(mThemeColor);
        toolBar?.let {
            rootView.addView(it)
            val tool = findViewById<Toolbar>(R.id.toolbar)
            if (tool != null) {
                setSupportActionBar(tool)
            }
            mToolbar = ArchitectToolbar(tool, supportActionBar!!, mThemeColor)
            mToolbar?.setTitle(title.toString())
            tool.setNavigationOnClickListener { finish() }
        }
    }

    protected fun getToolBar(): View? {
        return layoutInflater.inflate(R.layout.toolbar_base, null)
    }

    protected open fun getOverrideContentView(): Boolean = false

    protected inner class ExitBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val exitAction = "$packageName+${Constants.EXIT_APP}"
            if (exitAction == action && !isFinishing) {
                finish()
            }
        }
    }

    private fun registerExitReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("$packageName+${Constants.EXIT_APP}")
        registerReceiver(mExitReceiver, intentFilter)
    }

    override fun onDestroy() {
        try {
            if (mExitReceiver != null) {
                unregisterReceiver(mExitReceiver)
                mExitReceiver = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }

}