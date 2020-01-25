package com.timgortworst.roomy.data

import androidx.preference.PreferenceManager
import com.timgortworst.roomy.data.utils.Constants.SHARED_PREF_ADS
import com.timgortworst.roomy.data.utils.Constants.SHARED_PREF_DARK_MODE
import com.timgortworst.roomy.data.utils.Constants.SHARED_PREF_FIRST_LAUNCH
import com.timgortworst.roomy.presentation.base.RoomyApp
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by tim.gortworst on 07/03/2018.
 */
@Singleton
class SharedPrefs
@Inject constructor() {
    private val sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(RoomyApp.applicationContext())

    fun isFirstLaunch(): Boolean {
        return getBoolValue(SHARED_PREF_FIRST_LAUNCH, true)
    }

    fun setFirstLaunch(setFirstLaunch: Boolean) {
        setBoolValue(SHARED_PREF_FIRST_LAUNCH, setFirstLaunch)
    }

    fun isAdsEnabled(): Boolean {
        return getBoolValue(SHARED_PREF_ADS, true)
    }

    fun setAdsEnabled(setAdsEnabled: Boolean) {
        setBoolValue(SHARED_PREF_ADS, setAdsEnabled)
    }

    fun setDarkModeSetting(darkMode : Int) {
        setIntValue(SHARED_PREF_DARK_MODE, darkMode)
    }

    fun getDarkModeSetting(): Int {
        return getIntValue(SHARED_PREF_DARK_MODE)
    }

    private fun getIntValue(key: String, default: Int = 0): Int {
        return sharedPreferences.getInt(key, default)
    }

    private fun setIntValue(key: String, value: Int) {
        return sharedPreferences.edit().putInt(key, value).apply()
    }

    private fun getBoolValue(key: String, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    private fun setBoolValue(key: String, value: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(key, value)
            .apply()
    }
}
