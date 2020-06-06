package com.example.pomofocus

import android.content.Context
import androidx.preference.PreferenceManager

class PrefUtil {
    companion object{

        private const val WORK_TIMER_LENGTH_ID = "com.example.pomofocus.work_timer_length"
        private const val PLAY_TIMER_LENGTH_ID = "com.example.pomofocus.play_timer_length"

        fun getWorkTimerLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(WORK_TIMER_LENGTH_ID, 10)
        }

        fun getPlayTimerLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(PLAY_TIMER_LENGTH_ID, 10)
        }

        private const val PREVIOUS_PLAY_TIMER_LENGTH_SECONDS_ID = "com.example.pomofocus,orevious_play_timer"
        private const val PREVIOUS_WORK_TIMER_LENGTH_SECONDS_ID = "com.example.pomofocus,orevious_work_timer"

        fun getPreviousWorkTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_WORK_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun getPreviousPlayTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_PLAY_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousPlayTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_PLAY_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        fun setPreviousWorkTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_WORK_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.example.pomofocus.timer_id"

        fun getTimerState(context: Context): MainActivity.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return MainActivity.TimerState.values()[ordinal]
        }

        fun setTimerState(state: MainActivity.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private const val TIMER_MODE_ID = "com.example.pomofocus.timer_mode"

        fun getTimerMode(context: Context): MainActivity.TimerMode{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_MODE_ID, 0)
            return MainActivity.TimerMode.values()[ordinal]
        }

        fun setTimerMode(state: MainActivity.TimerMode, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_MODE_ID, ordinal)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "com.example.pomofocus.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()

        }



    }

}