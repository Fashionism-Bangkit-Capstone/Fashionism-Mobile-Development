package com.alcorp.fashionism_umkm

import com.alcorp.fashionism_umkm.di.networkModule
import com.alcorp.fashionism_umkm.di.repositoryModule
import com.alcorp.fashionism_umkm.di.viewModelModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify

class KoinModulesTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun allDefinitionsResolvable() {
        module { includes(networkModule, repositoryModule, viewModelModule) }.verify()
    }
}
