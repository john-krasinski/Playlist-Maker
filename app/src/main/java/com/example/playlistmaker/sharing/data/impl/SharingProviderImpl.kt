package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.api.SharingProvider


class SharingProviderImpl(private val context: Context): SharingProvider {
    override fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.ShareAppMessage))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun contactSupport() {
        val sendIntent: Intent =  Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, context.getString(R.string.SupportEmail))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.SupportMessageText))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.SupportMessageHeader))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(sendIntent)
    }

    override fun readUserAgreement() {
        val sendIntent: Intent =  Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(context.getString(R.string.LicenseUrl))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(sendIntent)
    }
}