package com.example.mymovieapp.core.ui.event

object MyEventManager {

    private val myEventListeners = mutableListOf<MyEventListener>()

    fun addListener(myEventListener: MyEventListener) {
        this.myEventListeners.add(myEventListener)
    }

    fun removeListener(myEventListener: MyEventListener){
        this.myEventListeners.remove(myEventListener)
    }

    fun sendEvent(myEvent: MyEvent){
        myEventListeners.forEach { myEventListener ->
            myEventListener.receiveEvent(myEvent)
        }
    }
}