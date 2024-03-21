package com.htd.mymvvm.main.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import com.htd.mymvvm.base.ui.BaseViewModel

class MainViewModel @Inject constructor(application: Application) :
    BaseViewModel(application) {


     fun isAttendAllMeetings(meetings : MutableList<List<Int>>) : Boolean {
        //1，先排好序
        val length = meetings.size
        for (i in 0 until length) {
            for (j in 0 until length - i - 1) {
                if (meetings[j][0] > meetings[j + 1][0]) {
                    val temp = meetings[j]
                    meetings[j] = meetings[j + 1]
                    meetings[j + 1] = temp
                }
            }
        }
        //2.进行比较
        val l = length - 1
        meetings.forEachIndexed { index , metting ->
            if (index < l && meetings[index + 1][0] <= metting[1])
                return false
        }
        return true
    }
}
