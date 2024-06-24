package com.sonal.spaceshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.random.Random


class EnemySpaceShip(context: Context) {

    var enemySpaceShipBitmap: Bitmap
    var ex: Int=0
    var ey: Int=0
    var enemyVelocity: Int=0
    val random: Random = Random

    init {
        enemySpaceShipBitmap=BitmapFactory.decodeResource(context.resources, R.drawable.ufo)
        resetEnemySpaceShip()
    }

    fun getEnemySpaceShip(): Bitmap {
        return enemySpaceShipBitmap
    }

    fun getEnemySpaceShipWidth(): Int {
        return enemySpaceShipBitmap.width ?:0
    }

    fun getEnemySpaceShipHeight(): Int {
        return enemySpaceShipBitmap.height ?:0
    }

    fun resetEnemySpaceShip(){
        ex = 200 + random.nextInt(400)
        ey=0
        enemyVelocity= 14 + random.nextInt(10)
    }
}