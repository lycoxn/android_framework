package com.chanjet.architecture.widget

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.chanjet.architecture.R


/**
 * Created by liuyicen on 2019-07-05 14:20.
 */

class ArchitectToolbar(private val toolbar: Toolbar, private val actionBar: ActionBar) {
    private val tvTitle: TextView?
    private val tvRight: TextView?
    private val ivRight: ImageView?

    init {
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        tvTitle = this.toolbar.findViewById(R.id.tv_toolbar_title)
        tvRight = this.toolbar.findViewById(R.id.btn_toolbar_right)
        ivRight = this.toolbar.findViewById(R.id.iv_toolbar_right)
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_back_gray)
    }

    constructor(toolbar: Toolbar, actionBar: ActionBar, themeColor: Int) : this(toolbar, actionBar) {
        if (themeColor != Color.WHITE) {
            tvTitle?.setTextColor(Color.WHITE)
            tvRight?.setTextColor(Color.WHITE)
            this.actionBar.setHomeAsUpIndicator(R.mipmap.ic_back_white)
        }
    }

    fun show() {
        toolbar.setVisibility(View.VISIBLE)
    }

    fun hide() {
        toolbar.setVisibility(View.GONE)
    }

    fun setTitle(charSequence: CharSequence): TextView? {
        if (tvTitle != null) {
            tvTitle.text = charSequence
        }
        return tvTitle
    }

    fun setTitle(charSequence: CharSequence, color: Int): TextView? {
        if (tvTitle != null) {
            tvTitle.text = charSequence
            tvTitle.setTextColor(color)
        }
        return tvTitle
    }

    fun setTvRight(charSequence: CharSequence, onClickListener: View.OnClickListener): TextView? {
        if (tvRight != null) {
            tvRight.text = charSequence
            tvRight.setOnClickListener(onClickListener)
            tvRight.visibility = View.VISIBLE
        }
        return tvRight
    }

    fun setIvRight(resId: Int, onClickListener: View.OnClickListener): TextView? {
        if (ivRight != null) {
            ivRight.setImageResource(resId)
            ivRight.setOnClickListener(onClickListener)
            ivRight.visibility = View.VISIBLE
        }
        return tvRight
    }

    /**
     * 设置左上角返回键是否显示
     *
     * @param enabled
     */
    fun setNavigation(enabled: Boolean) {
        if (enabled) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back_gray)
        } else {
            toolbar.setNavigationIcon(null)
        }
    }

    fun setNavigationIndicator(resId: Int) {
        actionBar.setHomeAsUpIndicator(resId)
    }

    //设置返回监听事件
    fun setNavigationOnClickListener(onClickListener: View.OnClickListener) {
        toolbar.setNavigationOnClickListener(onClickListener)
    }

    //设置背景颜色
    fun setBackgroundColor(color: Int) {
        toolbar.setBackgroundColor(color)
    }

//    fun setOnQueryTextListener(onQueryTextListener: ClearEditText.OnTextChangeListener) {
//        searchView.setOnTextChangeListener(onQueryTextListener)
//    }
}
