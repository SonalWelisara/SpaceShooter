package com.sonal.spaceshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Shot(context: Context, shx:Int, shy:Int) {

    private var shotBitmap: Bitmap
    var shx:Int=0
    var shy:Int=0

    init {
        shotBitmap=BitmapFactory.decodeResource(context.resources, R.drawable.shot)
        this.shx=shx
        this.shy=shy
    }

    fun getShot(): Bitmap{
        return shotBitmap
    }

}