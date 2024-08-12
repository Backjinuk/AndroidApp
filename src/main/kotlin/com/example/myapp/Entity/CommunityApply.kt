package com.example.myapp.Entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class CommunityApply {

    @Id
    @GeneratedValue
    @Column(name = "community_apply_seq")
    var communityApplySeq:Long ?= 0;

    @Column(name ="apply_user_seq")
    var applyUserSeq: Long ?= 0;

    @Column(name = "apply_commu_seq")
    var applyCommuSeq: Long ?= 0;

    @Column(name = "apply_status")
    var applyStatus:Char ?= 'N';

    @Column(name="reg_dt")
    var regDt:LocalDateTime ?= LocalDateTime.now()

    @Column(name="up_dt")
    var upDt:LocalDateTime ?= null

}