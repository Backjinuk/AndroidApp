package com.example.myapp.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@SequenceGenerator(
    name = "subscribe_seq_generator",
    sequenceName = "subscribe_seq",
    allocationSize = 1
)
open class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscribe_seq_generator")
    @Column(name = "subscribe_seq")
    open var subscribeSeq : Long ?= 0

    /***
     * @author 구독당하는 사함
     */
    @Column(name="subscriber_owner_user_seq")
    open var subscriberOwnerUserSeq : Long ?= 0

    /***
     * @author 구독하는 사함
     */
    @Column(name="subscriber_user_seq")
    open var subscriberUserSeq: Long ?= 0

    @Column(name="subscriber_status")
    open var subscribeStatus:Char ?= null

    @Column(name="reg_dt")
    open var regDt:LocalDateTime ?= LocalDateTime.now()






}