package com.timgortworst.roomy.local

import android.content.SharedPreferences
import com.timgortworst.roomy.utils.Constants.SHARED_PREF_FIRST_LAUNCH
import com.timgortworst.roomy.utils.Constants.SHARED_PREF_HOUSEHOLD_ID
import com.timgortworst.roomy.utils.Constants.SHARED_PREF_USER_ID

/**
 * Created by tim.gortworst on 07/03/2018.
 */
class HuishoudGenootSharedPref
constructor(private val sharedPreferences: SharedPreferences) {

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(SHARED_PREF_FIRST_LAUNCH, true)
    }

    fun setFirstLaunch(setFirstLaunch: Boolean) {
        sharedPreferences.edit().putBoolean(SHARED_PREF_FIRST_LAUNCH, setFirstLaunch).apply()
    }

    fun getHouseholdId(): String {
        return sharedPreferences.getString(SHARED_PREF_HOUSEHOLD_ID, "").orEmpty()
    }

    fun setHouseholdId(householdId: String) {
        sharedPreferences.edit().putString(SHARED_PREF_HOUSEHOLD_ID, householdId).apply()
    }
}
