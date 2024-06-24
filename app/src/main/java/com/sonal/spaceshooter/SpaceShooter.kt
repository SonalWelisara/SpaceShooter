package com.sonal.spaceshooter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.view.View
import kotlinx.coroutines.Runnable
import kotlin.random.Random
import android.view.MotionEvent

class SpaceShooter(context: Context): View(context) {
    private var background: Bitmap
    private var lifeImage: Bitmap
    private var UPDATE_MILLIS: Long=30
    private var screenWidth: Int
    private var screenHeight: Int
    private var points: Int=0
    private var life: Int=3
    private var  scorePaint: Paint
    private var TEXT_SIZE: Float=80f
    private var paused: Boolean=false
    private var ourSpaceShip: OurSpaceShip
    private var enemySpaceShip: EnemySpaceShip
    private val random: Random = Random
    private var enemyShots: ArrayList<Shot> = ArrayList()
    private var ourShots: ArrayList<Shot> = ArrayList()
    private var enemyExplosion: Boolean= false
    private var explosions: ArrayList<Explosion> = ArrayList()
    private var enemyShotAction: Boolean=false
    private val runnable = Runnable{
        invalidate()
    }
    private val explosionsToRemove = ArrayList<Explosion>()

    init {
        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y
        ourSpaceShip = OurSpaceShip(context, screenWidth, screenHeight)
        enemySpaceShip = EnemySpaceShip(context)
        background = BitmapFactory.decodeResource(context.resources, R.drawable.background)
        lifeImage = BitmapFactory.decodeResource(context.resources, R.drawable.life)
        scorePaint = Paint().apply {
            color = Color.RED
            textSize = TEXT_SIZE
            textAlign = Paint.Align.LEFT
        }
    }

    override fun onDraw(canvas: Canvas){
        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawText("Pt: "+points, 0f, TEXT_SIZE, scorePaint)

        for (i in life downTo 1){
            val left = (screenWidth - lifeImage.width * i).toFloat()
            canvas.drawBitmap(lifeImage, left, 0f, null )
        }

        if (life == 0){
            paused = true
            handler.removeCallbacksAndMessages(null)
            saveHighScore(points)
            val intent = Intent(context, GameOver::class.java)
            intent.putExtra("points", points)
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }

        enemySpaceShip.ex += enemySpaceShip.enemyVelocity
        if(enemySpaceShip.ex + enemySpaceShip.getEnemySpaceShipWidth() >= screenWidth){
            enemySpaceShip.enemyVelocity *= -1
        }

        if(enemySpaceShip.ex <=0 ){
            enemySpaceShip.enemyVelocity *= -1
        }

        if(!enemyShotAction && (enemySpaceShip.ex >=200+random.nextInt(400))){
            var enemyShot = Shot(context, enemySpaceShip.ex + enemySpaceShip.getEnemySpaceShipWidth()/2, enemySpaceShip.ey)
            enemyShots.add(enemyShot)
            enemyShotAction = true
        }

        if(!enemyExplosion){
            canvas.drawBitmap(enemySpaceShip.getEnemySpaceShip(), enemySpaceShip.ex.toFloat(), enemySpaceShip.ey.toFloat(), null)
        }

        if(ourSpaceShip.isAlive == true){
            if(ourSpaceShip.ox>screenWidth-ourSpaceShip.getOurSpaceShipWidth()){
                ourSpaceShip.ox = screenWidth - ourSpaceShip.getOurSpaceShipWidth()
            }else if(ourSpaceShip.ox<0){
                ourSpaceShip.ox=0
            }
            canvas.drawBitmap(ourSpaceShip.getOurSpaceShip(), ourSpaceShip.ox.toFloat(), ourSpaceShip.oy.toFloat(), null)
        }

        for (i in enemyShots.size - 1 downTo 0) {
            enemyShots[i].shy += 15
            canvas.drawBitmap(enemyShots[i].getShot(), enemyShots[i].shx.toFloat(), enemyShots[i].shy.toFloat(), null)
            if ((enemyShots[i].shx >= ourSpaceShip.ox)
                && (enemyShots[i].shx <= ourSpaceShip.ox + ourSpaceShip.getOurSpaceShipWidth())
                && (enemyShots[i].shy >= ourSpaceShip.oy)
                && (enemyShots[i].shy <= screenHeight))
            {
                life--
                enemyShots.removeAt(i)
                var explosion = Explosion(context, ourSpaceShip.ox, ourSpaceShip.oy)
                explosions.add(explosion)
            } else if (enemyShots[i].shy >= screenHeight) {
                enemyShots.removeAt(i)
                break
            }
        }

        if (enemyShots.isEmpty()) {
            enemyShotAction = false
        }

        for(i in ourShots.size - 1 downTo 0){
            ourShots.get(i).shy -=15
            canvas.drawBitmap(ourShots.get(i).getShot(), ourShots.get(i).shx.toFloat(), ourShots.get(i).shy.toFloat(), null)
            if((ourShots.get(i).shx >= enemySpaceShip.ex)
                && (ourShots.get(i).shx <= enemySpaceShip.ex + enemySpaceShip.getEnemySpaceShipWidth())
                && (ourShots.get(i).shy <= enemySpaceShip.getEnemySpaceShipHeight())
                && ourShots.get(i).shy >= enemySpaceShip.ey)
            {
                points++
                ourShots.removeAt(i)
                val explosion = Explosion(context, enemySpaceShip.ex, enemySpaceShip.ey)
                explosions.add(explosion)
            }else if(ourShots.get(i).shy <= 0){
                ourShots.removeAt(i)
            }
        }

        for(i in explosions.indices){
            canvas.drawBitmap(explosions.get(i).getExplosion (explosions.get(i).explosionFrame), explosions.get(i).eX.toFloat(), explosions.get(i).eY.toFloat(), null)
            explosions.get(i).explosionFrame++

            if (explosions[i].explosionFrame > 8) {
                explosionsToRemove.add(explosions[i])
            }
        }
        explosions.removeAll(explosionsToRemove)

        if(!paused){
            handler.postDelayed(runnable, UPDATE_MILLIS)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val touchX = it.x.toInt()
            when (it.action) {
                MotionEvent.ACTION_UP -> {
                    if (ourShots.size < 3) {
                        val ourShot = Shot(context, ourSpaceShip.ox + ourSpaceShip.getOurSpaceShipWidth() / 2, ourSpaceShip.oy)
                        ourShots.add(ourShot)
                    }
                }
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    ourSpaceShip.ox = touchX
                }
            }
        }
        return true
    }

    private fun saveHighScore(points: Int) {
        val sharedPref = context.getSharedPreferences("SpaceShooter", Context.MODE_PRIVATE)
        val highScore = sharedPref.getInt("highScore", 0)
        if (points > highScore) {
            with(sharedPref.edit()) {
                putInt("highScore", points)
                apply()
            }
        }
    }
}