/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.JUNITO.android.QRCordMaler2

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream


/**
 * Activity for entering a word.
 */

class NewWordActivity : AppCompatActivity() {

  val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"

    private val viewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        // WEBViewに広告を貼る
        val webView: WebView = findViewById(R.id.web_add2)
         webView.loadUrl("http://t5c.xyz/app/QRCordmaker.html")


        val editWordView = findViewById<EditText>(R.id.edit_word)
        //DB修正
        val btn_update=findViewById<Button>(R.id.btn_update)
        //DB保存
        val button = findViewById<Button>(R.id.button_save)
        //QRコード作成
        val create_qr_btn=findViewById<Button>(R.id.create_qr_btn)
        val qr_code=findViewById<ImageView>(R.id.qr_code)
        //val qr_code=findViewById<ImageView>(R.id.qr_cord)
        //dB削除
        val btn_delete=findViewById<Button>(R.id.btn_delete2)

//画像を保存する。
        val btn_download=findViewById<Button>(R.id.btn_download)


//画面遷移　リストを選択した場合の処理 選択されたテキストを表示させる。
        val insenttxt =intent.getStringExtra("TEXT_KEY")
        if(insenttxt==null){
            //新規の場合 修正ボタンを無効にする
            btn_update.isEnabled=false
        }else{
            //修正の場合　新規ボタンを無効にする。
            editWordView.setText(insenttxt)
            button.isEnabled=false
        }

        //DB保存

        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editWordView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = editWordView.text.toString()
               // const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
        //DB削除
        btn_delete.setOnClickListener {
             val txtwrod=editWordView.text.toString()
            // val txtTest="TEST2"
            val word = Word(txtwrod  )
            // editWordView.setText("Crea:  "+word.toString())
            //viewModel.insert(word)
             viewModel.delete(word)
            finish()
        }
//DB修正
        btn_update.setOnClickListener{
            val txtword=editWordView.text.toString()
            val word=Word(txtword)
            viewModel.insert(word)
            finish()
        }

        //DB修正
       //QRコード作成
        create_qr_btn.setOnClickListener {
            val multiFormatWriter = MultiFormatWriter()
            try {
                val bitMatrix =
                        multiFormatWriter.encode(editWordView.text.toString(), BarcodeFormat.QR_CODE, 200, 200)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                qr_code.setImageBitmap(bitmap)
            } catch (e: Exception) {
            }
        }
        //画像を保存する。
        btn_download.setOnClickListener {
           //imageViewに画像があるか判定する。
            if (qr_code.drawable==null){
                //画像がない場合の処理
                Toast.makeText(applicationContext,"画像がありません",Toast.LENGTH_SHORT).show()

            }else{
//画像がある場合の処理
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1)
                } else {
                    saveFile(createFile())

                }
            }
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // 絶対に許可される前提で記述
        saveFile(createFile())
    }

    private fun createFile(): File {
       //ファイル名を設定する。
        val mk=makeFileName()
        val savefilename=mk.fimename

        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

        return File(dir, savefilename+".jpeg")
    }

    private fun saveFile(f: File) {
       val imv_qr=findViewById<ImageView>(R.id.qr_code)
       // val bit = BitmapFactory.decodeResource(resources, R.drawable.pic)
       //イメージビューの画像を取得


        val bit = (imv_qr.drawable as BitmapDrawable).bitmap

     //   val bit = BitmapFactory.decodeResource(resources,bmp_qr)
        val ops = FileOutputStream(f)
        bit.compress(Bitmap.CompressFormat.PNG, 100, ops)

        ops.close()

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put("_data", f.absolutePath)
        }

        contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        Toast.makeText(applicationContext,"ダウンロードされました",Toast.LENGTH_LONG).show()
            }
}






 //  companion object {
////       const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
//    }

