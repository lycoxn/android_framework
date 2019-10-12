package com.chanjet.architecture.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.chanjet.architecture.R
import com.chanjet.architecture.util.autoCleared

abstract class BaseFragment<VM : ViewModel, VB : ViewDataBinding> : Fragment() {
    protected lateinit var mViewModel: VM
    protected lateinit var rootView: LinearLayout
    protected var mBinding by autoCleared<VB>()

    //获取宿主Activity
    protected val holdingActivity: BaseActivity<*, *>
        get() = activity as BaseActivity<*, *>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_base, null) as LinearLayout
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        rootView.findViewById<LinearLayout>(R.id.container).addView(mBinding.root)
        initView(rootView)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = getViewModel()
        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    protected abstract fun initView(view: View)

    protected abstract fun getViewModel(): VM

    protected abstract fun getLayoutId(): Int

}