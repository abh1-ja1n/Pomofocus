package com.example.pomofocus

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    enum class TimerState{
        Stopped, Pause, Running
    }

    enum class TimerMode{
        Work, Play
    }

    private lateinit var timer: CountDownTimer
    private var workTimerLengthSeconds = 0L
    private var playTimerLengthSeconds = 0L
    private var secondsRemaining = 0L
    private var timerState = TimerState.Stopped
    private var timerMode = TimerMode.Work

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = " Pomofocus"
        start_pause.setOnClickListener{ v ->
            handleStartPauseButton()
        }

        reset.setOnClickListener{ v ->
            timer.cancel()
            onReset()
            //onTimerFinished()
        }
    }

    private fun handleStartPauseButton() {
        if(timerState == TimerState.Stopped || timerState == TimerState.Pause){
            startTimer()
            timerState = TimerState.Running
            timerMode = PrefUtil.getTimerMode(this)
            reset.isEnabled = true
            start_pause.setText(getString(R.string.pause))
        }
        else{
            timer.cancel()
            timerState = TimerState.Pause
            timerMode = PrefUtil.getTimerMode(this)
            start_pause.setText(getString(R.string.start))
        }
    }

    override fun onResume(){
        super.onResume()

        initTimer()
    }

    override fun onPause(){
        super.onPause()

        if(timerState == TimerState.Running){
            timer.cancel()
        }
        else if(timerState == TimerState.Pause){
            //
        }
        PrefUtil.setPreviousPlayTimerLengthSeconds(playTimerLengthSeconds, this)
        PrefUtil.setPreviousWorkTimerLengthSeconds(workTimerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerMode(timerMode, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(this)
        var m1: Int
        var m2: Int
        if(timerState == TimerState.Stopped){
            m1 = setNewWorkTimerLength()
            m2 = setNewPlayTimerLength()
        }
        else{
            m1 = setPreviousWorkTimerLength()
            m2 = setPreviousPlayTimerLength()
        }
        timerMode = PrefUtil.getTimerMode(this)
        if(timerMode == TimerMode.Work){
            progress_countdown.max = m1
        }
        else{
            progress_countdown.max = m2
        }

        if(timerMode == TimerMode.Work) {
            secondsRemaining =
                if (timerState == TimerState.Running || timerState == TimerState.Pause)
                    PrefUtil.getSecondsRemaining(this)
                else
                    workTimerLengthSeconds
        }
        else{
            secondsRemaining =
                if (timerState == TimerState.Running || timerState == TimerState.Pause)
                    PrefUtil.getSecondsRemaining(this)
                else
                    playTimerLengthSeconds
        }

        if(timerState == TimerState.Running){
            startTimer()
        }
        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        startanotherTimer()
    }

    private fun onReset(){

        timerState = TimerState.Stopped
        timerMode = TimerMode.Work
        var a = setNewPlayTimerLength()
        progress_countdown.max = setNewWorkTimerLength()
        text_msg.setText("Time to Work!!")

        progress_countdown.progress = 0

        PrefUtil.setSecondsRemaining(workTimerLengthSeconds, this)
        secondsRemaining = workTimerLengthSeconds
        updateButtons()
        updateCountdownUI()
    }

    private fun startanotherTimer(){

        if(timerMode == TimerMode.Work){
            timerMode = TimerMode.Play
            text_msg.setText("Time to Play!!")

            progress_countdown.max = setPreviousPlayTimerLength()
            PrefUtil.setSecondsRemaining(playTimerLengthSeconds, this)
            secondsRemaining = playTimerLengthSeconds
            startTimer()
        }
        else{
            timerMode = TimerMode.Work
            text_msg.setText("Time to Work!!")
            progress_countdown.max = setPreviousWorkTimerLength()
            PrefUtil.setSecondsRemaining(workTimerLengthSeconds, this)
            secondsRemaining = workTimerLengthSeconds
            startTimer()
        }
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timer = object: CountDownTimer(secondsRemaining * 1000, 1000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished/1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewWorkTimerLength() : Int{
        val lengthinMinutes = PrefUtil.getWorkTimerLength(this)
        workTimerLengthSeconds = lengthinMinutes * 60L
        return workTimerLengthSeconds.toInt()
    }

    private fun setNewPlayTimerLength() : Int{
        val lengthinMinutes = PrefUtil.getPlayTimerLength(this)
        playTimerLengthSeconds = lengthinMinutes * 60L
        return playTimerLengthSeconds.toInt()
    }

    private fun setPreviousWorkTimerLength() : Int{
        workTimerLengthSeconds = PrefUtil.getPreviousWorkTimerLengthSeconds(this)
        return workTimerLengthSeconds.toInt()
    }

    private fun setPreviousPlayTimerLength() : Int{
        playTimerLengthSeconds = PrefUtil.getPreviousPlayTimerLengthSeconds(this)
        return playTimerLengthSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val TimerLengthSeconds = if(timerMode == TimerMode.Work) workTimerLengthSeconds else
            playTimerLengthSeconds
        val minutesUntilFinished = secondsRemaining/60
        val secondsinMinuteUntilFinished = secondsRemaining - (minutesUntilFinished * 60)
        val secondStr = secondsinMinuteUntilFinished.toString()
        textview_countdown.text = "$minutesUntilFinished:${if (secondStr.length == 2) secondStr
        else "0" + secondStr}"
        progress_countdown.progress = (TimerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons(){
        when(timerState){
            TimerState.Running -> {
                start_pause.setText(getString(R.string.pause))
                reset.isEnabled = true
            }
            TimerState.Stopped -> {
                start_pause.setText(getString(R.string.start))
                reset.isEnabled = false
            }
            TimerState.Pause -> {
                start_pause.setText(getString(R.string.start))
                reset.isEnabled = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
