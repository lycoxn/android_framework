package com.android.example.github.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.android.example.github.R
import com.android.example.github.databinding.ActivityTestBinding
import com.android.example.github.ui.user.UserViewModel
import com.chanjet.architecture.base.BaseActivity
import com.chanjet.architecture.widget.DialogWait
import kotlinx.android.synthetic.main.activity_test.*
import javax.inject.Inject

/**
 * Created by liuyicen on 2019-07-03 15:23.
 */
class TestActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        text.setOnClickListener { Toast.makeText(this, "123", Toast.LENGTH_SHORT).show() }
    }
}

