package com.dicoding.intermediate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.dicoding.intermediate.R
import com.dicoding.intermediate.databinding.FragmentRegisterBinding
import com.dicoding.intermediate.ui.vm.RegisterVM
import com.dicoding.intermediate.utils.helper.gone
import com.dicoding.intermediate.utils.helper.observeSingleEvent
import com.dicoding.intermediate.utils.helper.popClick
import com.dicoding.intermediate.utils.helper.show
import com.dicoding.intermediate.utils.helper.showError
import com.dicoding.intermediate.utils.helper.showSnackBar
import org.koin.android.ext.android.inject

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: RegisterVM by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        binding.apply {
            toolBar.apply {
                setNavigationOnClickListener {
                    it.findNavController().popBackStack()
                }
            }

            btnRegister.popClick {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val fullName = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        when {
            fullName.isEmpty() -> {
                binding.edtName.showError(getString(R.string.validation_must_not_empty))
            }
            email.isEmpty() -> {
                binding.edtEmail.showError(getString(R.string.validation_must_not_empty))
            }
            password.isEmpty() -> {
                binding.edtPassword.showError(getString(R.string.validation_must_not_empty))
            }
            password.length < 8 -> {
                binding.edtPassword.showError(getString(R.string.validation_password))
            }
            else -> {
                viewModel.registerUser(fullName, email, password)
            }
        }
    }

    override fun initProcess() {}

    override fun initObservers() {
        viewModel.registerResult.observeSingleEvent(
            viewLifecycleOwner,
            loading = {
                binding.loadingLayout.root.show()
            },
            success = { response ->
                binding.loadingLayout.root.gone()
                binding.root.showSnackBar(response.data)
                requireView().findNavController().popBackStack()
            },
            error = { response ->
                binding.loadingLayout.root.gone()
                binding.root.showSnackBar(response.errorMessage)
            },
        )
    }
}