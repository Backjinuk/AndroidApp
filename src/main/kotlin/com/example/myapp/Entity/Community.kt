package com.example.myapp.Entity

import jakarta.persistence.*
import lombok.Data
import java.time.LocalDateTime

@Entity
@Data
class Community {

    @Id
    @GeneratedValue
    @Column(name = " commu_seq")
    var commuSeq:Long = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usrSeq") // This defines the foreign key column
    lateinit var commuWrite: User

    @Column(name = "commuTitle")
    var commuTitle:String = "";

    @Column(name = "commu_coment")
    var commuComent:String = "";

    @Column(name="total_user_count")
    var totalUserCount:Int = 0;

    @Column(name="user_count")
    var userCount:Int = 0;

    var latitude:Double = 0.0;

    var longitude: Double = 0.0;

    var address:String = "";


    @Column(name = "commu_meeting_time")
    var commuMeetingTime:LocalDateTime = LocalDateTime.now();

    @Column(name="reg_dt")
    var regDt:LocalDateTime = LocalDateTime.now();

    @Column(name="up_dt")
    var upDt:LocalDateTime = LocalDateTime.now()

}