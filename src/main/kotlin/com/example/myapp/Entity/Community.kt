package com.example.myapp.Entity

import jakarta.persistence.*
import lombok.Data
import java.time.LocalDateTime

@Entity
@Data
@SequenceGenerator(
    name = "commu_seq_generator", // 시퀀스 생성기 이름
    sequenceName = "commu_seq", // 데이터베이스에 생성될 시퀀스 이름
    allocationSize = 1 // 시퀀스를 한 번에 1씩 증가시킴
)
open class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commu_seq_generator")
    @Column(name = " commu_seq")
    open var commuSeq:Long = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usrSeq") // This defines the foreign key column
    open lateinit var commuWrite: User

    @Column(name = "commuTitle")
    open var commuTitle:String = "";

    @Column(name = "commu_coment")
    open var commuComent:String = "";

    @Column(name="total_user_count")
    open var totalUserCount:Int = 0;

    @Column(name="user_count")
    open var userCount:Int = 0;

    open var latitude:Double = 0.0;

    open var longitude: Double = 0.0;

    open var address:String = "";

    @Column(name = "commu_meeting_time")
    open var commuMeetingTime:LocalDateTime = LocalDateTime.now();

    @Column(name="reg_dt")
    open var regDt:LocalDateTime = LocalDateTime.now();

    @Column(name="up_dt")
    open var upDt:LocalDateTime = LocalDateTime.now()

}