package com.chanjet.architecture.api

import android.content.Context
import androidx.lifecycle.Observer
import com.chanjet.architecture.base.BasePresenter
import com.chanjet.architecture.util.toast
import okio.JvmOverloads

abstract class CommonObserver<T> constructor(private val presenter: BasePresenter) : Observer<ApiResponse<BaseResponse<T>>> {

    init {
        presenter.showWaitingDialog()
    }

    override fun onChanged(apiResponse: ApiResponse<BaseResponse<T>>?) {
//        if (apiResponse?.code !== 200) {
//            onError(apiResponse?.errorMessage)
//        } else if (apiResponse.body == null) {
//            onError("数据解析异常，未包含status节点")
//        } else {
//            handleSuccessBody(apiResponse.body)
//        }
        when (apiResponse) {
            is ApiSuccessResponse -> {
                presenter.dismissWaitingDialog()
                handleSuccessBody(apiResponse.body)
//                Resource.success(apiResponse.nextPage != null)
            }
            is ApiEmptyResponse -> {
                presenter.dismissWaitingDialog()
                onComplete(null)
//                Resource.success(false)
            }
            is ApiErrorResponse -> {
                presenter.dismissWaitingDialog()
                onResponseError(apiResponse.errorMessage)
//                Resource.error(apiResponse.errorMessage, true)
            }
        }
    }

    private fun onResponseError(errorMessage: String) {
        toast(errorMessage)
    }

    private fun handleSuccessBody(body: BaseResponse<T>) {
        when (body.code) {
            "00" -> onComplete(body.data)
            else -> onError(body.message)
        }
        onTotalData(body)
    }

    open fun onTotalData(body: BaseResponse<T>) {

    }

    open fun onError(message: String?) {
        toast(message!!)
    }

    abstract fun onComplete(t: T?)
}