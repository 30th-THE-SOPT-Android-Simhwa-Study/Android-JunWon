package org.techtown.androidsimhwastudy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.techtown.androidsimhwastudy.databinding.ActivityMainBinding
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * 3개의 서브 스레드들이 각자 할 일을 한 후 Massage 객체(massage)에 작업 내용을 담고
 * massage를 핸들러가 메인쓰레드의 Masseage 큐에 넣어주거나 처리한다.
 * 메세지 큐에 담긴 메세지는 추후에 Looper가 꺼내서 핸들러한테 넘겨서 처리시킴 */

class MainActivity : AppCompatActivity() {

    private val bitmapConverter by lazy {
        BitmapConverter()
    }
    private val myHandler: MyHandler by lazy {
        MyHandler(binding, bitmapConverter)
    }
    private var buttonState = ThreadState(true)
    private var profileState = ThreadState(true)
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickProfileImage()
        setOnClickMinhoButton()
        setOnClickJkButton()
    }

    private fun setOnClickProfileImage() {
        binding.ivProfile.setOnClickListener {
            if (profileState.isRunning) {
                profileState.isRunning = false
                CountThread(myHandler, profileState).start()
            }
        }
    }

    private fun setOnClickMinhoButton() {
        binding.btnMinHo.setOnClickListener {
            if (buttonState.isRunning) {
                buttonState.isRunning = false
                binding.ivProfile.visibility = View.INVISIBLE
                binding.pgbSearch.visibility = View.VISIBLE
                MinhoThread(myHandler, buttonState, ::getBitmapFromURL, bitmapConverter).start()
            }
        }
    }

    private fun setOnClickJkButton() {
        binding.btnJoonKyung.setOnClickListener {
            if (buttonState.isRunning) {
                buttonState.isRunning = false
                binding.ivProfile.visibility = View.INVISIBLE
                binding.pgbSearch.visibility = View.VISIBLE
                JkThread(myHandler, buttonState, ::getBitmapFromURL, bitmapConverter).start()
            }
        }
    }

    private fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true // 읽기모드임
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    class MyHandler(
        private val binding: ActivityMainBinding,
        private val converter: BitmapConverter
    ) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_CNT -> {
                    Log.d(TAG, "MyHandler - MSG_CNT ")
                    binding.tvCnt.text = msg.arg1.toString()
                }
                MSG_IMAGE -> {
                    val bundle = msg.data
                    val bitmapInfo = bundle.getString("bitmapKey")
                    val bitmap = converter.stringToBitmap(bitmapInfo)
                    bitmap?.let {
                        binding.ivProfile.setImageBitmap(it)
                    }
                    binding.ivProfile.visibility = View.VISIBLE
                    binding.pgbSearch.visibility = View.INVISIBLE
                }
                else -> Log.e(TAG, "handleMessage: ${msg.what}")
            }
        }
    }

    class CountThread(
        private val myHandler: MyHandler,
        private val profileState: ThreadState
    ) : Thread() {
        override fun run() {
            repeat(50) {
                val message = Message().apply {
                    what = MSG_CNT
                    arg1 = it + 1
                }
                sleep(1000L)
                myHandler.sendMessage(message)
            }
            profileState.isRunning = true
        }
    }

    class MinhoThread(
        private val myHandler: MainActivity.MyHandler,
        private var buttonState: ThreadState,
        private val getBitmapFromURL: (String) -> Bitmap?,
        private val converter: BitmapConverter
    ) : Thread() {
        override fun run() {
            Log.d(TAG, "MinhoThread - run() called")
            // url을 bitmap로 변환 후 String으로 변환
            val bitmapInfo: String? =
                getBitmapFromURL("https://avatars.githubusercontent.com/u/15981307?v=4")?.let {
                    converter.bitmapToString(it)
                }
            // bundle에 bitmapInfo담기
            val bundle: Bundle = Bundle().apply { putString("bitmapKey", bitmapInfo) }
            // message data에 bundle 담기
            val message = Message().apply {
                what = MSG_IMAGE
                data = bundle
            }
            sleep(3000L)
            buttonState.isRunning = true
            myHandler.sendMessage(message)
        }
    }

    class JkThread(
        private val myHandler: MyHandler,
        private var buttonState: ThreadState,
        private val getBitmapFromURL: (String) -> Bitmap?,
        private val converter: BitmapConverter
    ) : Thread() {
        override fun run() {
            Log.d(TAG, "JkThread - run() called")
            val bitmapInfo: String? =
                getBitmapFromURL("https://avatars.githubusercontent.com/u/90037701?v=4")?.let {
                    converter.bitmapToString(it)
                }
            val bundle: Bundle = Bundle().apply { putString("bitmapKey", bitmapInfo) }
            val message = Message().apply {
                what = MSG_IMAGE
                data = bundle
            }
            sleep(3000L)
            buttonState.isRunning = true
            myHandler.sendMessage(message)
        }
    }

    data class ThreadState(var isRunning: Boolean)

    companion object {
        const val MSG_CNT = 1
        const val MSG_IMAGE = 2
        const val TAG = "로그"
    }
}
