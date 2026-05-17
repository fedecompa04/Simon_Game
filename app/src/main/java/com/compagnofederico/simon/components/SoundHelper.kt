package com.compagnofederico.simon.components

import android.content.Context
import android.media.SoundPool
import com.compagnofederico.simon.R

class SoundHelper(context: Context){
    private val soundPool: SoundPool = SoundPool.Builder().build()
    private val soundMap = mutableMapOf<String, Int>()

    init{
        soundMap["R"] = soundPool.load(context, R.raw.red, 1)
        soundMap["G"] = soundPool.load(context, R.raw.green, 1)
        soundMap["B"] = soundPool.load(context, R.raw.blue, 1)
        soundMap["M"] = soundPool.load(context, R.raw.magenta, 1)
        soundMap["Y"] = soundPool.load(context, R.raw.yellow, 1)
        soundMap["C"] = soundPool.load(context, R.raw.cyan, 1)
    }

    fun playSound(colorLetter: String){
        val soundID = soundMap[colorLetter] ?: return
        soundPool.play(soundID, 1f, 1f, 1, 0, 1f)
    }
}