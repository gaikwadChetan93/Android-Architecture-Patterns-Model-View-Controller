package com.example.topratedmoviewitharchitecturepattern.top_rated

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface ManagedCoroutineScope : CoroutineScope {
    abstract fun launch(block: suspend CoroutineScope.() -> Unit) : Job
}