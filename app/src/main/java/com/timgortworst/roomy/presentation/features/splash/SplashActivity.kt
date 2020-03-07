package com.timgortworst.roomy.presentation.features.splash

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.timgortworst.roomy.R
import com.timgortworst.roomy.domain.usecase.ForceUpdateUseCase
import com.timgortworst.roomy.domain.utils.InviteLinkBuilder.Companion.QUERY_PARAM_HOUSEHOLD
import com.timgortworst.roomy.domain.utils.showSnackbar
import com.timgortworst.roomy.presentation.RoomyApp
import com.timgortworst.roomy.presentation.features.main.MainActivity
import com.timgortworst.roomy.presentation.features.signin.SignInActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SplashActivity : AppCompatActivity(), SplashView, ForceUpdateUseCase.OnUpdateNeededListener {
    private val presenter: SplashPresenter by inject { parametersOf(this) }
    private lateinit var referredHouseholdId: String

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SplashActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MyTheme_NoActionBar_Launcher)
        super.onCreate(savedInstanceState)

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val currentVersion = RoomyApp.getAppVersion()

        ForceUpdateUseCase.with(remoteConfig)
            .onUpdateNeeded(this)
            .check(currentVersion)
    }

    override fun goToSignInActivity() {
        SignInActivity.start(this)
        finish()
    }

    override fun goToMainActivity() {
        MainActivity.start(this)
        finish()
    }

    override fun presentHouseholdOverwriteDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_household_overwrite_title))
            .setMessage(getString(R.string.dialog_household_overwrite_text))
            .setPositiveButton(android.R.string.yes) { _, _ ->
                presenter.changeCurrentUserHousehold(referredHouseholdId)
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                goToMainActivity()
            }
            .show()
    }

    override fun presentAlreadyInHouseholdDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_household_similar_title))
            .setMessage(getString(R.string.dialog_household_similar_text))
            .setNeutralButton(android.R.string.yes) { _, _ ->
                goToMainActivity()
            }
            .show()
    }

    override fun onUpdateNeeded(updateUrl: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.forced_update_dialog_title))
            .setMessage(getString(R.string.forced_update_dialog_text))
            .setPositiveButton(getString(R.string.forced_update_dialog_positive_button)) { _, _ ->
                redirectStore(updateUrl)
                finish()
            }
            .setNegativeButton(getString(R.string.forced_update_dialog_negative_button)) { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onUpdateRecommended(updateUrl: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.recommended_update_dialog_title))
            .setMessage(getString(R.string.recommended_update_dialog_text))
            .setPositiveButton(getString(R.string.recommended_update_dialog_positive_button)) { _, _ ->
                redirectStore(updateUrl)
                finish()
            }
            .setNegativeButton(getString(R.string.recommended_update_dialog_negative_button)) { _, _ ->
                noUpdateNeeded()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun noUpdateNeeded() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener {
            val referredHouseholdId = it?.link?.getQueryParameter(QUERY_PARAM_HOUSEHOLD).orEmpty()
            presenter.handleAppStartup(referredHouseholdId)
        }.addOnFailureListener {
            val content = findViewById<View>(android.R.id.content)
            content.showSnackbar(R.string.error_generic)
            finish()
        }
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
