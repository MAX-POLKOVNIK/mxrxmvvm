package com.github.polkovnik.max.mvvm

abstract class ReactiveMvvmDialogFragment<TViewModel : ReactiveViewModel> : MvvmDialogFragment<TViewModel>() {
    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.destroy()
    }
}