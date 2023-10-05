package com.tech.test.model

import jakarta.persistence.*

@Entity
class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,

    val model: String? = null,

    @Column
    var quantity: Int = 1,

    @OneToMany(mappedBy="device", fetch = FetchType.LAZY)
    val transactions: Set<BookingTransaction>? = null,
) {
    constructor(id: Int) : this(null, null, 0) {
        this.id = id
    }

    fun isAvailable() = this.quantity > 0
}