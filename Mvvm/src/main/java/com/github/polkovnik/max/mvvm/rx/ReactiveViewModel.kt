package com.github.polkovnik.max.mvvm.rx

import android.app.Application
import androidx.annotation.StringRes
import com.github.polkovnik.max.mvvm.ViewModel
import com.github.polkovnik.max.mvvm.state.Idle
import com.github.polkovnik.max.mvvm.state.loading.UiLockLoadingState
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class ReactiveViewModel(application: Application) : ViewModel(application) {
    private val subscriptions: MutableList<Disposable> = mutableListOf()
    private val subscriptionsWhileVisible: MutableList<Disposable> = mutableListOf()

    open fun destroy() {
        subscriptions.forEach { it.dispose() }
        subscriptions.clear()
    }

    protected fun <T> Single<T>.subscribeOnIoObserveOnMain() =
        this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    protected fun Completable.subscribeOnIoObserveOnMain() =
        this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    protected fun <T> PublishRelay<T>.subscribeOnIoObserveOnMain() =
        this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    protected fun <T> Single<T>.applyUiLockOnSubscribeFinally(@StringRes message: Int) =
        applyUiLockOnSubscribeFinally(getString(message))

    protected fun <T> Single<T>.applyUiLockOnSubscribeFinally(message: String) =
        this.doOnSubscribe { state.mutableValue = UiLockLoadingState(message) }
            .doFinally { state.mutableValue = Idle }

    protected fun Completable.applyUiLockOnSubscribeFinally(@StringRes message: Int) =
        applyUiLockOnSubscribeFinally(getString(message))

    protected fun Completable.applyUiLockOnSubscribeFinally(message: String) =
        this.doOnSubscribe { state.mutableValue = UiLockLoadingState(message) }
            .doFinally { state.mutableValue = Idle }

    protected fun Disposable.addToSubscriptions() { subscriptions.add(this) }
    protected fun Disposable.addToSubscriptionsWhileVisible() { subscriptionsWhileVisible.add(this) }
}