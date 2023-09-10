package com.example.playlistmaker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounceSearch(delayMillis: Long,
                       coroutineScope: CoroutineScope,
                       useLastParam: Boolean,
                       action: (T, Boolean) -> Unit): (T,Boolean) -> Unit {
    var debounceJob: Job? = null
    return { param, bool ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param, bool)
            }
        }
    }
}

fun <T> debounce(delayMillis: Long,
                       coroutineScope: CoroutineScope,
                       useLastParam: Boolean,
                       action: (T) -> Unit): (T) -> Unit {
    var debounceJob: Job? = null
    return { param ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}

