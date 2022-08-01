package org.techtown.androidsimhwastudy

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import org.techtown.androidsimhwastudy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val myHandler = MyHandler()
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
            CountThread().start()
        }
    }

    private fun setOnClickMinhoButton() {
        binding.btnMinHo.setOnClickListener {
            binding.ivProfile.visibility = View.INVISIBLE
            binding.pgbSearch.visibility = View.VISIBLE
            MinhoThread().start()
        }
    }

    private fun setOnClickJkButton() {
        binding.btnJoonKyung.setOnClickListener {
            binding.ivProfile.visibility = View.INVISIBLE
            binding.pgbSearch.visibility = View.VISIBLE
            JkThread().start()
        }
    }

    inner class MyHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_CNT -> {
                    binding.tvCnt.text = msg.arg1.toString()
                }
                MSG_IMAGE -> {
                    binding.ivProfile.load(msg.obj)
                    binding.ivProfile.visibility = View.VISIBLE
                    binding.pgbSearch.visibility = View.INVISIBLE
                }
                else -> Log.e(TAG, "handleMessage: ${msg.what}")
            }
        }
    }

    inner class CountThread : Thread() {
        override fun run() {
            repeat(50) {
                val message = Message().apply {
                    what = MSG_CNT
                    arg1 = it + 1
                }
                myHandler.sendMessage(message)
                sleep(1000L)
            }
        }
    }

    inner class MinhoThread : Thread() {
        override fun run() {
            val message = Message().apply {
                what = MSG_IMAGE
                obj = "https://avatars.githubusercontent.com/u/90037701?v=4"
            }
            sleep(3000L)
            myHandler.sendMessage(message)
        }
    }

    inner class JkThread : Thread() {
        override fun run() {
            val message = Message().apply {
                what = MSG_IMAGE
                obj = "https://avatars.githubusercontent.com/u/90037701?v=4"
            }
            sleep(3000L)
            myHandler.sendMessage(message)
        }
    }

    companion object {
        const val MSG_CNT = 1
        const val MSG_IMAGE = 2
        const val TAG = "로그"
    }
}
/**
 * 3개의 서브 스레드들이 각자 할 일을 한 후 Massage 객체(massage)에 작업 내용을 담고
 * massage를 핸들러가 메인쓰레드의 Masseage 큐에 넣어주거나 처리한다.
 * 메세지 큐에 담긴 메세지는 추후에 Looper가 꺼내서 핸들러한테 넘겨서 처리시킴 */
