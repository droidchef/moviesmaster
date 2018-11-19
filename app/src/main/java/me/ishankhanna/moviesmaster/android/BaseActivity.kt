package me.ishankhanna.moviesmaster.android

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.ishankhanna.moviesmaster.presenter.BasePresenter
import me.ishankhanna.moviesmaster.view.MvpView

abstract class BaseActivity<P : BasePresenter<MvpView>> : MvpView, AppCompatActivity() {

    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = instantiatePresenter()

    }

    protected abstract fun instantiatePresenter(): P

    override fun getContext(): Context {
        return this
    }

}