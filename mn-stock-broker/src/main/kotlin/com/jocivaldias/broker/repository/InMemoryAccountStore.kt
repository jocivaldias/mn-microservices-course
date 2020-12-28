package com.jocivaldias.broker.repository

import com.jocivaldias.broker.dto.WatchListDTO
import java.util.*
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class InMemoryAccountStore {

    private val watchListDTOPerAccount: HashMap<UUID, WatchListDTO> = HashMap()

    fun getWatchList(accountUUID: UUID): WatchListDTO {
        return watchListDTOPerAccount.getOrDefault(accountUUID, WatchListDTO())
    }

    fun updateWatchList(accountUUID: UUID, watchListDTO: WatchListDTO): WatchListDTO {
        watchListDTOPerAccount[accountUUID] = watchListDTO
        return getWatchList(accountUUID)
    }

    fun delete(accountUUID: UUID) {
        watchListDTOPerAccount.remove(accountUUID)
    }

}

