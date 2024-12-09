package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.api.SharingProvider


class SharingProviderImpl(private val srcActivityContext: Context): SharingProvider {
    override fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, srcActivityContext.getString(R.string.ShareAppMessage))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        srcActivityContext.startActivity(shareIntent)
    }

    override fun contactSupport() {
        val sendIntent: Intent =  Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, srcActivityContext.getString(R.string.SupportEmail))
            putExtra(Intent.EXTRA_TEXT, srcActivityContext.getString(R.string.SupportMessageText))
            putExtra(Intent.EXTRA_SUBJECT, srcActivityContext.getString(R.string.SupportMessageHeader))
        }
        srcActivityContext.startActivity(sendIntent)
    }

    override fun readUserAgreement() {
        val sendIntent: Intent =  Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(srcActivityContext.getString(R.string.LicenseUrl))
        }
        srcActivityContext.startActivity(sendIntent)
    }
}