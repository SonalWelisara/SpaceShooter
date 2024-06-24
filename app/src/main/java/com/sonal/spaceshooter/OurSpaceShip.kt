package com.sonal.spaceshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.random.Random

class OurSpaceShip(context: Context, screenWidth: Int, screenHeight: Int) {

    private var ourSpaceShipBitmap: Bitmap
    var ox: Int=0
    var oy: Int=0
    var isAlive: Boolean? = true
    private var ourVelocity: Int=0
    private val random: Random = Random

    init {
        ourSpaceShipBitmap=BitmapFactory.decodeResource(context.resources, R.drawable.rocket)
        resetOurSpaceShip(screenWidth, screenHeight);
    }

    fun getOurSpaceShip(): Bitmap {
        return ourSpaceShipBitmap
    }

    fun getOurSpaceShipWidth(): Int {
        return ourSpaceShipBitmap.width ?:0
    }

    private fun resetOurSpaceShip(screenWidth: Int, screenHeight: Int){
        ox = 200 + random.nextInt(screenWidth)
        oy=screenHeight - ourSpaceShipBitmap.height
        ourVelocity= 14 + random.nextInt(10)
    }
}