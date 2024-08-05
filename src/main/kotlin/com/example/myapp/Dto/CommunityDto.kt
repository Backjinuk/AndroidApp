package com.example.myapp.Dto

import java.time.LocalDateTime

class CommunityDTO {

    var commuSeq: Long = 0;

    var commuWrite: UserDto = UserDto(); // Assuming User ID is sufficient to represent User in DTO

    var commuTitle: String = "";

    var commuComent: String = "";

    var totalUserCount: Int = 0;

    var userCount:Int = 0;

    var latitude: Double = 0.0;

    var longitude: Double = 0.0;

    var address: String = "";

    var commuMeetingTime: LocalDateTime = LocalDateTime.now();

    var regDt: LocalDateTime = LocalDateTime.now();

    var upDt: LocalDateTime = LocalDateTime.now();
}