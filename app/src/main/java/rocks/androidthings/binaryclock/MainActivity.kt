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

            //ledControl.setLed(0, 0, 1, true)

        } catch (e: IOException) {
            Log.e(TAG, "Error initializing LED matrix", e)
        }

        val someHandler = Handler(mainLooper)
        someHandler.postDelayed(object : Runnable {
            override fun run() {

                rightNow = Calendar.getInstance()

                val hour = rightNow.get(Calendar.HOUR_OF_DAY)
                val minute = rightNow.get(Calendar.MINUTE)
                val hourUnit = hour % 10
                val minuteUnit = minute % 10

                // HOUR (0)
                if(hour in 10..19) {
                    ledControl.setLed(0, 0, 1, true)
                } else {
                    ledControl.setLed(0, 0, 1, false)
                }
                if(hour in 20..23)  {
                    ledControl.setLed(0, 0, 2, true)
                } else {
                    ledControl.setLed(0, 0, 2, false)
                }

                //HOUR UNITS (1)
                if(hourUnit == 1 || hourUnit == 3 || hourUnit == 5 || hourUnit == 7 || hourUnit == 9) {
                    ledControl.setLed(0, 1, 1, true)
                } else {
                    ledControl.setLed(0, 1, 1, false)
                }
                if(hourUnit == 2 || hourUnit == 3 || hourUnit == 6 || hourUnit == 7) {
                    ledControl.setLed(0, 1, 2, true)
                } else {
                    ledControl.setLed(0, 1, 2, false)
                }
                if(hourUnit == 4 || hourUnit == 5 || hourUnit == 6 || hourUnit == 7) {
                    ledControl.setLed(0, 1, 3, true)
                } else {
                    ledControl.setLed(0, 1, 3, false)
                }
                if(hourUnit == 8 || hourUnit == 9) {
                    ledControl.setLed(0, 1, 4, true)
                } else {
                    ledControl.setLed(0, 1, 4, false)
                }

                // MINUTE (2)
                if((minute in 10..19) || (minute in 30..39) || (minute in 50..59))  {
                    ledControl.setLed(0, 2, 1, true)
                } else {
                    ledControl.setLed(0, 2, 1, false)
                }

                if(minute in 20..39)  {
                    ledControl.setLed(0, 2, 2, true)
                } else {
                    ledControl.setLed(0, 2, 2, false)
                }
                if(minute in 40..59) {
                    ledControl.setLed(0, 2, 3, true)
                } else {
                    ledControl.setLed(0, 2, 3, false)
                }

                // MINUTE UNITS (3)
                if(minuteUnit == 1 || minuteUnit == 3 || minuteUnit == 5 || minuteUnit == 7 || minuteUnit == 9) {
                    ledControl.setLed(0, 3, 1, true)
                } else {
                    ledControl.setLed(0, 3, 1, false)
                }
                if(minuteUnit == 2 || minuteUnit == 3 || minuteUnit == 6 || minuteUnit == 7) {
                    ledControl.setLed(0, 3, 2, true)
                } else {
                    ledControl.setLed(0, 3, 2, false)
                }
                if(minuteUnit == 4 || minuteUnit == 5 || minuteUnit == 6 || minuteUnit == 7) {
                    ledControl.setLed(0, 3, 3, true)
                } else {
                    ledControl.setLed(0, 3, 3, false)
                }
                if(minuteUnit == 8 || minuteUnit == 9) {
                    ledControl.setLed(0, 3, 4, true)
                } else {
                    ledControl.setLed(0, 3, 4, false)
                }

                someHandler.postDelayed(this, 1000)
            }
        }, 10)

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
