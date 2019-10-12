package com.chanjet.architecture.adapter

import android.view.View

import androidx.databinding.ViewDataBinding

import com.chanjet.architecture.inter.OnItemClickListener

/**
 * RecyclerView通用Adapter，可直接使用
 */

class SimpleCommonRecyclerAdapter<T>(layoutId: Int, brId: Int) : CommonRecyclerAdapter<T, CommonRecyclerAdapter.ViewHolder<ViewDataBinding>>(layoutId, brId) {

    private var onItemClickListener: OnItemClickListener? = null

    /**
     * 设置监听事件
     *
     * @param onItemClickListener
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getHolder(view: View): ViewHolder<ViewDataBinding> {
        return ViewHolder(view, onItemClickListener)
    }

}
