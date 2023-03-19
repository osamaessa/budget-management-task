package com.osama.budgetmanagement.presentation.base

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController

abstract class BaseFragment<VB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
) : Fragment() {

    lateinit var binding: VB
    val provider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            this@BaseFragment.onCreateMenu(menu, menuInflater)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == android.R.id.home) {
                findNavController().popBackStack()
                return true
            }
            this@BaseFragment.onMenuItemSelected(menuItem)
            return true
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.apply {
            lifecycleOwner = this@BaseFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (withBack()){
                        findNavController().popBackStack()
                    }else{
                        requireActivity().finishAffinity()
                    }
                }
            }
        )
    }

    private fun setUpToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar())
        if (withBack()) {
            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
                true
            )
        }

        (requireActivity() as MenuHost).addMenuProvider(provider, requireActivity(), Lifecycle.State.CREATED)

    }

    fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MenuHost).removeMenuProvider(provider)
    }

    abstract fun withBack(): Boolean
    abstract fun toolbar(): Toolbar
    abstract fun onCreateMenu(menu: Menu, menuInflater: MenuInflater)
    abstract fun onMenuItemSelected(menuItem: MenuItem)
}