package com.JUNITO.android.QRCordMaler2

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//ファイル名を作成するクラス
class makeFileName {
   val nowtime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
   val fimename=nowtime.toString()
}