package com.timgortworst.roomy.domain

interface UseCase<T> {
    fun invoke() : T
//    fun<P, T> executeUseCase(vararg prop: P?, funCreateSpec: P) : T
}