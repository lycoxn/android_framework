package com.android.example.github.di;

import com.android.example.github.test.TestActivity;
import com.android.example.github.test.TestActivity2;
import com.android.example.github.ui.activity.StartActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract TestActivity testActivityInjector();

    @ContributesAndroidInjector
    abstract TestActivity2 testActivity2Injector();

    @ContributesAndroidInjector
    abstract StartActivity testStartActivity();

}
