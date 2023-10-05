package com.tech.test.respository

import com.tech.test.model.User
import com.tech.test.model.WatchDevice
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
internal interface WatchDeviceRepository : CrudRepository<WatchDevice, Int> {
    fun findByWatcher(watcher: User):List<WatchDevice>
}
