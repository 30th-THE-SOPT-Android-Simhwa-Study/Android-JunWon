package org.techtown.androidsimhwastudy.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class BitmapConverter {
    fun bitmapToString(bitmap: Bitmap): String {
        val baos =
            ByteArrayOutputStream() // 바이트 배열을 차례대로 읽어 들이기위한 ByteArrayOutputStream클래스 선언, 차례대로 받지 않으면 어떤 일이 일어날까요?
        bitmap.compress(
            Bitmap.CompressFormat.PNG,
            70,
            baos
        ) // bitmap을 압축 (숫자 70은 70%로 압축한다는 뜻, 몇으로 해야 좋은지는 잘 모르겠음)
        val bytes = baos.toByteArray() // 해당 bitmap을 byte배열로 바꿔준다.
        return Base64.encodeToString(bytes, Base64.DEFAULT) // Base 64 방식으로byte 배열을 String으로 변환
    }

    fun stringToBitmap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(
                encodedString,
                Base64.DEFAULT
            ) // String 화 된 이미지를  base64방식으로 인코딩하여 byte배열을 만듬
            BitmapFactory.decodeByteArray(
                encodeByte,
                0,
                encodeByte.size
            ) //  byte to Bitmap
            // 만들어진 bitmap을 return
        } catch (e: Exception) {
            e.message
            null // 안되면 어쩔 수 없이 null 보내야죵.. res에서 placeholder같은거 하나 보내도 좋을듯
        }
    }
}
