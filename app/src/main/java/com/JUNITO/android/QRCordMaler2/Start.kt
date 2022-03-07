package com.JUNITO.android.QRCordMaler2

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class Start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val new_btn=findViewById<Button>(R.id.new_btn)
        val rireki_btn=findViewById<Button>(R.id.rireki_btn)

        // WEBViewに広告を貼る
        val webView: WebView = findViewById(R.id.web_add)
        //webView.loadUrl("http://worldclock.t5c.xyz/")

        webView.loadUrl("http://t5c.xyz/app/QRCordmaker.html")


        new_btn.setOnClickListener {
            val intent = Intent(this, NewWordActivity::class.java) //インテントの作成
            startActivity(intent)
        }

        rireki_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) //インテントの作成
            startActivity(intent)
        }



    }
}