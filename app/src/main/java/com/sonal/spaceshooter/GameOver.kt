package com.sonal.spaceshooter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver: AppCompatActivity() {

    private lateinit var tvPoints: TextView
    private lateinit var tvHighScore: TextView
    private var highScores: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameover)

        highScores = getHighScore()
        var points = intent.extras?.getInt("points")
        tvPoints = findViewById(R.id.tvPoints)
        tvPoints.setText(""+points)
        tvHighScore = findViewById(R.id.tvHighScore)
        tvHighScore.setText(""+highScores)
    }
    fun restart(v:View){
        val intent = Intent(this, StartUp::class.java)
        startActivity(intent)
        finish()
    }

    fun exit(v: View){
        finish()
    }

    private fun getHighScore(): Int {
        val sharedPref = getSharedPreferences("SpaceShooter", Context.MODE_PRIVATE)
        return sharedPref.getInt("highScore", 0)
    }
}