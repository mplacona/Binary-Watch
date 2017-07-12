package rocks.androidthings.binaryclock

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import rocks.androidthings.driver.max72xx.MAX72XX
import java.io.IOException
import java.util.*


class MainActivity : Activity() {
    private val TAG = MainActivity::class.java.simpleName
    private val NB_DEVICES = 1
    lateinit var ledControl: MAX72XX
    lateinit var rightNow: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            ledControl = MAX72XX("SPI0.0", NB_DEVICES)
            for (i in 0..ledControl.getDeviceCount() - 1) {
                ledControl.setIntensity(i, 15)
                ledControl.shutdown(i, false)
                ledControl.clearDisplay(i)
            }

        } catch (e: IOException) {
            Log.e(TAG, "Error initializing LED matrix", e)
        }

        val timeHandler = Handler(mainLooper)
        timeHandler.postDelayed(object : Runnable {
            override fun run() {
                setClockFace()
                timeHandler.postDelayed(this, 1000)
            }
        }, 10)

    }

    private fun setClockFace(){
        rightNow = Calendar.getInstance()

        val hour = rightNow.get(Calendar.HOUR_OF_DAY)
        val minute = rightNow.get(Calendar.MINUTE)
        val hourUnit = hour % 10
        val minuteUnit = minute % 10

        Log.d(TAG, "START")
        Log.d(TAG, hour.toString())
        Log.d(TAG, minute.toString())
        Log.d(TAG, "END")

        // HOUR (0)
        when (hour) {
            in 10..19 -> ledControl.setLed(0, 0, 1, true)
            else -> ledControl.setLed(0, 0, 1, false)
        }
        when (hour) {
            in 20..23 -> ledControl.setLed(0, 0, 2, true)
            else -> ledControl.setLed(0, 0, 2, false)
        }

        //HOUR UNITS (1)
        when (hourUnit) {
            1, 3, 5, 7, 9 -> ledControl.setLed(0, 1, 1, true)
            else -> ledControl.setLed(0, 1, 1, false)
        }
        when (hourUnit) {
            2, 3, 6, 7 -> ledControl.setLed(0, 1, 2, true)
            else -> ledControl.setLed(0, 1, 2, false)
        }
        when (hourUnit) {
            4, 5, 6, 7 -> ledControl.setLed(0, 1, 3, true)
            else -> ledControl.setLed(0, 1, 3, false)
        }
        when (hourUnit) {
            8, 9 -> ledControl.setLed(0, 1, 4, true)
            else -> ledControl.setLed(0, 1, 4, false)
        }

        // MINUTE (2)
        when (minute) {
            in 10..19, in 30..39, in 50..59 -> ledControl.setLed(0, 2, 1, true)
            else -> ledControl.setLed(0, 2, 1, false)
        }
        when (minute) {
            in 20..39 -> ledControl.setLed(0, 2, 2, true)
            else -> ledControl.setLed(0, 2, 2, false)
        }
        when (minute) {
            in 40..59 -> ledControl.setLed(0, 2, 3, true)
            else -> ledControl.setLed(0, 2, 3, false)
        }

        // MINUTE UNITS (3)
        when (minuteUnit) {
            1, 3, 5, 7, 9 -> ledControl.setLed(0, 3, 1, true)
            else -> ledControl.setLed(0, 3, 1, false)
        }
        when (minuteUnit) {
            2, 3, 6, 7 -> ledControl.setLed(0, 3, 2, true)
            else -> ledControl.setLed(0, 3, 2, false)
        }
        when (minuteUnit) {
            4, 5, 6, 7 -> ledControl.setLed(0, 3, 3, true)
            else -> ledControl.setLed(0, 3, 3, false)
        }
        when (minuteUnit) {
            8, 9 -> ledControl.setLed(0, 3, 4, true)
            else -> ledControl.setLed(0, 3, 4, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            ledControl.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing LED matrix", e)
        }

    }
}
