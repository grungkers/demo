package com.tech.test.model

import jakarta.persistence.*
import java.util.*

@Entity
class BookingTransaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?=null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceId")
    val device: Device,

    @Column
    var returnedDate: Date? = null,

    @ManyToOne
    @JoinColumn
    val borrower: User,

    @Column
    val fromDate: Date,

    @Column
    val toDate: Date? = null,
)