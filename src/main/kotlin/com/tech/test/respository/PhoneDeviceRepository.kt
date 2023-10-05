package com.tech.test.respository

import com.tech.test.model.Device
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PhoneDeviceRepository: CrudRepository<Device, Int> {
    fun findByModel(model: String): Device

    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.transactions t WHERE t.returnedDate IS NULL OR d.quantity > 0")
    override fun findAll(): List<Device>
}