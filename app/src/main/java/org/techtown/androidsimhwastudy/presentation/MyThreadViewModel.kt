package org.techtown.androidsimhwastudy.presentation

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.techtown.androidsimhwastudy.data.ThreadState

class MyThreadViewModel : ViewModel() {
    private var _threadState = MutableLiveData<ThreadState>(ThreadState.SLEEPING)
    val threadState: LiveData<ThreadState> get() = _threadState
    private var _bitmap = MutableLiveData<Bitmap>()
    val image: LiveData<Bitmap> get() = _bitmap
    private var _count = MutableLiveData<Int>(0)
    val count: LiveData<Int> get() = _count

    fun setBitmap(image: Bitmap) {
        _bitmap.value = image
    }

    fun increaseCount() {
        _count.postValue(_count.value!!.plus(1))
    }

    fun setThreadState(isRunning: Boolean) {
        _threadState.value = if (isRunning) ThreadState.RUNNING else ThreadState.SLEEPING
    }
}
