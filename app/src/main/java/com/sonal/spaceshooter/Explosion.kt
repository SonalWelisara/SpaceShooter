package com.sonal.spaceshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.random.Random

class Explosion(context: Context, eX:Int, eY:Int) {

    var explosion = arrayOf<Bitmap>()
    var eX: Int
    var eY: Int
    var explosionFrame: Int=0

    init {
        explosion = arrayOf(
            BitmapFactory.decodeResource(context.resources, R.drawable.ex0),
            BitmapFactory.decodeResource(context.resources, R.drawable.ex1),
            BitmapFactory.decodeResource(context.resources, R.drawable.ex2),
            BitmapFactory.decodeResource(context.resources, R.drawable.ex3),
            BitmapFactory.decodeResource(context.resources, R.drawable.ex4),
            BitmapFactory.decodeResource(context.resources, R.drawable.ex5),
            BitmapFactory.decodeResource(context.resources, R.drawable.ex6)
        )
        this.eX=eX
        this.eY=eY
    }

    fun getExplosion(explosionFrame: Int): Bitmap {
        if (explosionFrame >= 0 && explosionFrame < explosion.size) {
            return explosion[explosionFrame]
        }
        return explosion.last()
    }

}