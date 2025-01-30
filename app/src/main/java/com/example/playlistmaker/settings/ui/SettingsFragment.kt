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

    private var _ui: FragmentSettingsBinding? = null
    private val ui get() = _ui!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentSettingsBinding.inflate(inflater)

        settingsViewModel.isDarkThemeEnabled().observe(viewLifecycleOwner) { isEnabled ->
            ui.btnToggleDarkTheme.isChecked = isEnabled
        }

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

        val root = ui.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            SettingsFragment()
    }
}