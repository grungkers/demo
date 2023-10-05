package com.tech.test.respository

import com.tech.test.model.BookingTransaction
import com.tech.test.model.Device
import com.tech.test.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BookingTxnRepository: CrudRepository<BookingTransaction, Int> {
    @Query("SELECT t FROM BookingTransaction t " +
            "WHERE t.borrower = :user " +
            "AND t.device = :device AND t.returnedDate IS NULL")
    fun findUnreturnedTxnsByBorrowerAndDevice(@Param("user") user: User, @Param("device") device: Device): BookingTransaction
}