package com.example.mymovieapp.core.ui

import androidx.lifecycle.ViewModel
import com.example.mymovieapp.core.ui.event.MyEvent
import com.example.mymovieapp.core.ui.event.MyEventListener
import com.example.mymovieapp.core.ui.event.MyEventManager

abstract class EventListenerViewModel : ViewModel(){

    private val myEventListener = object : MyEventListener {
        override fun receiveEvent(event: MyEvent) {
            onEventReceiver(event)
        }
    }

    init {
        MyEventManager.addListener(myEventListener)
    }

    override fun onCleared() {
        MyEventManager.removeListener(myEventListener)
        super.onCleared()
    }

    open fun onEventReceiver(myEvent: MyEvent){}
}