package com.timgortworst.roomy.presentation.features.splash

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.timgortworst.roomy.R
import com.timgortworst.roomy.domain.model.response.Response
import com.timgortworst.roomy.domain.usecase.user.InviteLinkBuilderUseCaseImpl.Companion.QUERY_PARAM_HOUSEHOLD
import com.timgortworst.roomy.presentation.RoomyApp
import com.timgortworst.roomy.presentation.base.model.EventObserver
import com.timgortworst.roomy.presentation.base.model.StartUpAction
import com.timgortworst.roomy.presentation.base.model.UpdateAction
import com.timgortworst.roomy.presentation.base.snackbar
import com.timgortworst.roomy.presentation.base.view.BaseActivity
import com.timgortworst.roomy.presentation.features.main.MainActivity
import com.timgortworst.roomy.presentation.features.signin.SignInActivity
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity() {
    private val viewModel: SplashViewModel by inject()

    companion object {
        fun intentBuilder(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MyTheme_NoActionBar_Launcher)
        super.onCreate(savedInstanceState)

        val currentVersion = RoomyApp.getAppVersion()

        viewModel.checkForUpdates(currentVersion)

        viewModel.startupAction.observe(this, EventObserver {
            when (it) {
                Response.Loading -> showProgressDialog()
                is Response.Error -> {
                    val rootView = findViewById<View>(android.R.id.content) ?: return@EventObserver
                    rootView.snackbar(message = getString(R.string.error_generic))
                }
                is Response.Success -> {
                    when (it.data) {
                        StartUpAction.TriggerSignInFlow -> goToSignInActivity()
                        StartUpAction.TriggerMainFlow -> goToMainActivity()
                        StartUpAction.DialogSameId -> presentAlreadyInHouseholdDialog()
                        is StartUpAction.DialogOverrideId -> presentHouseholdOverwriteDialog(it.data.id)
                    }
                }
            }
        })

        viewModel.updateAction.observe(this, EventObserver { response ->
            when(response) {
                Response.Loading -> showProgressDialog()
                is Response.Error -> {
                    val rootView = findViewById<View>(android.R.id.content) ?: return@EventObserver
                    rootView.snackbar(message = getString(R.string.error_generic))
                }
                is Response.Success -> {
                    when (response.data) {
                        UpdateAction.none -> noUpdate()
                        is UpdateAction.recommended -> updateRecommended(response.data.url)
                        is UpdateAction.required -> updateNeeded(response.data.url)
                    }
                }
            }
        })
    }

    private fun goToSignInActivity() {
        startActivity(SignInActivity.intentBuilder(this))
        finish()
    }

    private fun goToMainActivity() {
        startActivity(MainActivity.intentBuilder(this))
        finish()
    }

    private fun presentHouseholdOverwriteDialog(referredHouseholdId: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_household_overwrite_title))
            .setMessage(getString(R.string.dialog_household_overwrite_text))
            .setPositiveButton(android.R.string.yes) { _, _ ->
                viewModel.switchHousehold(referredHouseholdId)
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                goToMainActivity()
            }
            .show()
    }

    private fun presentAlreadyInHouseholdDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_household_similar_title))
            .setMessage(getString(R.string.dialog_household_similar_text))
            .setNeutralButton(android.R.string.yes) { _, _ ->
                goToMainActivity()
            }
            .show()
    }

    private fun updateNeeded(updateUrl: String) {
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

    private fun updateRecommended(updateUrl: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.recommended_update_dialog_title))
            .setMessage(getString(R.string.recommended_update_dialog_text))
            .setPositiveButton(getString(R.string.recommended_update_dialog_positive_button)) { _, _ ->
                redirectStore(updateUrl)
                finish()
            }
            .setNegativeButton(getString(R.string.recommended_update_dialog_negative_button)) { _, _ ->
                noUpdate()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun noUpdate() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener {
            val referredHouseholdId = it?.link?.getQueryParameter(QUERY_PARAM_HOUSEHOLD).orEmpty()
            viewModel.handleAppStartup(referredHouseholdId)
        }.addOnFailureListener {
            val content = findViewById<View>(android.R.id.content)
            content.snackbar(getString(R.string.error_generic))
            finish()
        }
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
