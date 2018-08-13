package com.github.quarck.gxustaalarmo

// Anko is not building properly for me for some reason, so
// it was easier to just create a few lines of code below rather then
// investigating why it is not working

import android.os.AsyncTask

class AsyncOperation(val fn: () -> Unit)
    : AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg p0: Void?): Void? {
        fn()
        return null
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun background(noinline fn: () -> Unit) {
    AsyncOperation(fn).execute();
}

