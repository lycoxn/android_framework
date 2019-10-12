package com.chanjet.architecture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

import com.chanjet.architecture.inter.OnItemClickListener

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.Comparator

/**
 * RecyclerView通用Adapter，可继承扩展
 * Created by liuyicen on 2017/7/5.
 */

abstract class CommonRecyclerAdapter<T, H : CommonRecyclerAdapter.ViewHolder<*>>
@JvmOverloads constructor(private val mLayoutId: Int, protected var mBrId: Int, objects: Collection<T>? = null) : RecyclerView.Adapter<H>() {
    private var context: Context? = null
    private val mObjects: MutableList<T>
    private var mNotifyOnChange = true

    constructor(context: Context, layoutId: Int, brId: Int, objects: Collection<T>) : this(layoutId, brId, objects) {
        this.context = context
    }

    init {
        mObjects = ArrayList()
        if (objects != null) {
            mObjects.addAll(objects)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, mLayoutId, parent, false)
        val viewHolder = getHolder(binding.root)
        viewHolder.binding = binding as Nothing?
        return viewHolder
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.binding!!.setVariable(mBrId, getItem(position))
        holder.binding!!.executePendingBindings()
    }

    abstract fun getHolder(view: View): H

    override fun getItemCount(): Int {
        return mObjects.size
    }

    fun getItem(position: Int): T {
        return mObjects[position]
    }

    fun getPosition(item: T): Int {
        return mObjects.indexOf(item)
    }

    class ViewHolder<B : ViewDataBinding> : RecyclerView.ViewHolder {
        private var onItemClickListener: OnItemClickListener? = null

        var binding: B? = null

        constructor(itemView: View) : super(itemView) {

        }

        constructor(itemView: View, onItemClickListener: OnItemClickListener?) : super(itemView) {
            this.onItemClickListener = onItemClickListener
            if (onItemClickListener != null) {
                itemView.setOnClickListener { v -> onItemClickListener.onItemClick(v, adapterPosition) }
            }
        }
    }

    fun getmObjects(): List<T> {
        return mObjects
    }

    fun add(objects: Collection<T>) {
        mObjects.addAll(objects)
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    fun replace(objects: Collection<T>) {
        mObjects.clear()
        mObjects.addAll(objects)
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    fun add(objects: Array<T>) {
        mObjects.addAll(Arrays.asList(*objects))
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    fun add(`object`: T) {
        mObjects.add(`object`)
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    fun insert(`object`: T, index: Int) {
        mObjects.add(index, `object`)
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    fun remove(`object`: T) {

        mObjects.remove(`object`)
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    fun remove(position: Int): T {
        val t = mObjects.removeAt(position)
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
        return t
    }

    fun remove(collection: Collection<T>) {

        mObjects.removeAll(collection)
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    /**
     * Remove all elements from the list.
     */
    fun clear() {
        mObjects.clear()
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    fun sort(comparator: Comparator<in T>) {
        Collections.sort(mObjects, comparator)
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }

    fun getContext(): Context {
        return context ?: throw IllegalArgumentException("请实现带Context的构造方法")
    }

    fun getmLayoutId(): Int {
        return mLayoutId
    }
}
