package com.manimarank.spell4wiki.data.prefs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.manimarank.spell4wiki.ui.login.LoginActivity
import com.manimarank.spell4wiki.data.auth.AccountUtils
import com.manimarank.spell4wiki.utils.WikiLicense

/**
 * Preference utility class for App level settings and login related preference
 */
class PrefManager(private val mContext: Context?) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor

    /**
     * Set user logged in flag
     * @param name String?
     */
    fun setUserSession(name: String?) {
        editor.putString(USERNAME, name)
        editor.putBoolean(IS_LOGGED_IN_USER, true)
        editor.putBoolean(IS_ANONYMOUS_USER, false)
        editor.apply()
    }

    val name: String?
        get() = pref.getString(USERNAME, null)
    val isLoggedIn: Boolean
        get() = !pref.getBoolean(IS_ANONYMOUS_USER, false) && pref.getBoolean(IS_LOGGED_IN_USER, false) && AccountUtils.isLoggedIn

    // First time launch for App Intro
    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.apply()
        }
    var csrfToken: String?
        get() = pref.getString(CSRF_TOKEN, null)
        set(editToken) {
            editor.putString(CSRF_TOKEN, editToken)
            editor.apply()
        }
    var isAnonymous: Boolean?
        get() = pref.getBoolean(IS_ANONYMOUS_USER, false)
        set(isAnonymous) {
            editor.putBoolean(IS_ANONYMOUS_USER, isAnonymous!!)
            editor.apply()
        }

    /**
     * Clear session details when click logout
     */
    fun logoutUser() {
        clearLoginData()
        if (mContext != null && mContext is Activity) {
            val activity = mContext
            // After logout redirect user to Login Activity
            activity.finishAffinity()
            activity.startActivity(Intent(mContext, LoginActivity::class.java))
        }
    }

    /**
     * Clear login information once logged out
     */
    fun clearLoginData() {
        val isFirstTime = isFirstTimeLaunch
        editor.clear()
        isFirstTimeLaunch = isFirstTime

        // Remove sync - authenticator account
        AccountUtils.removeAccount()
    }

    private val cookies: Set<String>?
        get() = pref.getStringSet(COOKIE, null)

    /**
     * Set cookie information
     * @param cookieList MutableSet<String?>
     */
    fun setCookies(cookieList: MutableSet<String?>) {
        val existingCookie = cookies
        if (existingCookie != null && existingCookie.size > 0) cookieList.addAll(existingCookie)
        editor.putStringSet(COOKIE, cookieList)
        editor.apply()
    }

    var languageCodeSpell4Wiki: String?
        get() = pref.getString(LANGUAGE_CODE_SPELL_4_WIKI, "ta")
        set(languageCode) {
            editor.putString(LANGUAGE_CODE_SPELL_4_WIKI, languageCode)
            editor.apply()
        }
    var languageCodeSpell4WordList: String?
        get() = pref.getString(LANGUAGE_CODE_SPELL_4_WORD_LIST, "ta")
        set(languageCode) {
            editor.putString(LANGUAGE_CODE_SPELL_4_WORD_LIST, languageCode)
            editor.apply()
        }
    var languageCodeSpell4Word: String?
        get() = pref.getString(LANGUAGE_CODE_SPELL_4_WORD, "ta")
        set(languageCode) {
            editor.putString(LANGUAGE_CODE_SPELL_4_WORD, languageCode)
            editor.apply()
        }
    var languageCodeWiktionary: String?
        get() = pref.getString(LANGUAGE_CODE_WIKTIONARY, "ta")
        set(languageCode) {
            editor.putString(LANGUAGE_CODE_WIKTIONARY, languageCode)
            editor.apply()
        }
    var uploadAudioLicense: String?
        get() = pref.getString(UPLOAD_AUDIO_LICENSE, WikiLicense.LicensePrefs.CC_0)
        set(uploadAudioLicense) {
            editor.putString(UPLOAD_AUDIO_LICENSE, uploadAudioLicense)
            editor.apply()
        }
    var abortAlertStatus: Boolean?
        get() = pref.getBoolean(ABORT_ALERT_STATUS, false)
        set(show) {
            editor.putBoolean(ABORT_ALERT_STATUS, show ?: false)
            editor.apply()
        }

    companion object {
        // shared pref
        private const val PREF_NAME = "spell4wiki_pref"
        private const val PRIVATE_MODE = 0

        // Key values
        private const val USERNAME = "username"
        private const val IS_LOGGED_IN_USER = "is_logged_in_user"
        private const val IS_ANONYMOUS_USER = "is_anonymous_user" // Only wiktionary use
        private const val IS_FIRST_TIME_LAUNCH = "is_first_time_launch"
        private const val LANGUAGE_CODE_SPELL_4_WIKI = "language_code_spell_4_wiki"
        private const val LANGUAGE_CODE_SPELL_4_WORD_LIST = "language_code_spell_4_word_list"
        private const val LANGUAGE_CODE_SPELL_4_WORD = "language_code_spell_4_word"
        private const val LANGUAGE_CODE_WIKTIONARY = "language_code_wiktionary"
        private const val UPLOAD_AUDIO_LICENSE = "upload_audio_license"
        private const val ABORT_ALERT_STATUS = "abort_alert_status"
        private const val CSRF_TOKEN = "csrf_token"
        private const val COOKIE = "cookie"
    }

    init {
        pref = mContext!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
        editor.apply()
    }
}