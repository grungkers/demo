package com.tech.test.model

import jakarta.persistence.*
import java.util.*

@Entity
class WatchDevice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?=null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceId")
    val device: Device,

    @ManyToOne
    @JoinColumn
    val watcher: User,
) {
}