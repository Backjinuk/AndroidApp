package com.example.myapp.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@SequenceGenerator(
    name = "community_apply_seq_generator",
    sequenceName = "community_apply_seq",
    allocationSize = 1
)
open class CommunityApply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_apply_seq_generator")
    @Column(name = "community_apply_seq")
    open var communityApplySeq:Long ?= 0;

    @Column(name ="apply_user_seq")
    open var applyUserSeq: Long ?= 0;

    @Column(name = "apply_commu_seq")
    open var applyCommuSeq: Long ?= 0;

    @Column(name = "apply_status")
    open var applyStatus:Char ?= 'N';

    @Column(name="reg_dt")
    open var regDt:LocalDateTime ?= LocalDateTime.now()

    @Column(name="up_dt")
    open var upDt:LocalDateTime ?= null

}