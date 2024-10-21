package com.dicoding.intermediate.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.dicoding.intermediate.R
import android.provider.Settings
import androidx.navigation.fragment.findNavController
import com.dicoding.intermediate.databinding.FragmentSettingBinding
import com.dicoding.intermediate.utils.helper.popClick
import com.dicoding.intermediate.utils.PreferManager
import com.dicoding.intermediate.utils.helper.showYesNoDialog
import org.koin.android.ext.android.inject

class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    private val prefer: PreferManager by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        binding.toolBar.apply {
            title = getString(R.string.title_settings)
            setNavigationOnClickListener {
                it.findNavController().popBackStack()
            }
        }
    }

    override fun initActions() {
        binding.apply {
            btnChangeLanguange.popClick {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            btnLogout.popClick {
                showYesNoDialog(
                    title = getString(R.string.title_logout),
                    message = getString(R.string.message_logout),
                    onYes = {
                        prefer.clearAllPreferences()
                        findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
                    }
                )
            }
        }
    }

    override fun initProcess() {
    }

    override fun initObservers() {
    }


}