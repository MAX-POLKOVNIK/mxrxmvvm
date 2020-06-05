package com.github.polkovnik.max.mvvm.rx

import com.github.polkovnik.max.mvvm.MvvmDialogFragment

abstract class ReactiveMvvmDialogFragment<TViewModel : ReactiveViewModel> : MvvmDialogFragment<TViewModel>() {
    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.destroy()
    }
}