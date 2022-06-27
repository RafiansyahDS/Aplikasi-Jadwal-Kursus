package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val listThemePref = findPreference<Preference>(getString(R.string.pref_key_dark)) as ListPreference
        listThemePref.setOnPreferenceChangeListener { _, newValue ->
            val themeValues = resources.getStringArray(R.array.dark_mode_value)
            when(newValue.toString()){
                themeValues[0] -> updateTheme(3)
                themeValues[1] -> updateTheme(2)
                themeValues[2] -> updateTheme(1)
            }
            true
        }

        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify)) as SwitchPreference
        val dailyReminder = DailyReminder()
        prefNotification.setOnPreferenceChangeListener { _, newValue ->
            if(newValue.equals(true)){
                dailyReminder.setDailyReminder(layoutInflater.context)
            }else{
                dailyReminder.cancelAlarm(layoutInflater.context)
            }
            true
        }

    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}