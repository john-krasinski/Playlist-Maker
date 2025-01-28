package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModel<SettingsViewModel> {
        parametersOf((requireActivity().application as App))
    }

    private lateinit var ui: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            //
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = FragmentSettingsBinding.inflate(inflater)
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsViewModel.isDarkThemeEnabled().observe(viewLifecycleOwner) { isEnabled ->
            ui.btnToggleDarkTheme.isChecked = isEnabled
        }

//        ui.btnBackFromSettings.setOnClickListener {
//            finish()
//        }

        ui.btnToggleDarkTheme.setOnCheckedChangeListener { btn, isChecked ->
            settingsViewModel.toggleDarkTheme(isChecked)
        }

        ui.btnShare.setOnClickListener {
            settingsViewModel.shareApp()
        }

        ui.btnSupport.setOnClickListener {
            settingsViewModel.contactSupport()
        }

        ui.btnUserAgreement.setOnClickListener {
            settingsViewModel.readUserAgreement()
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            SettingsFragment()
    }
}