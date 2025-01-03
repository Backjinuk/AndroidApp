package com.example.myapp.user.userSetting.service

import com.example.myapp.user.userSetting.domain.ThemePreference
import com.example.myapp.user.userSetting.domain.UserSettingEnabled
import com.example.myapp.user.userSetting.domain.dto.UserSettingDto
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class UserSettingServiceIntegrationTest @Autowired constructor(
    private val userSettingService: UserSettingService
) {


    @Test
    fun `등록 성공 - 유효한 userSettingDto는 DB에 등록되어야 한다`() {
        //Given
        val userSettingDto = UserSettingDto().apply {
            userSeq = 100
            notificationEnabled = UserSettingEnabled.ON
            eventEnabled = UserSettingEnabled.ON
            themePreference = ThemePreference.DARK
        }

        //When
        val savedDto = userSettingService.userSettingTableSetting(userSettingDto)

        //Then
        assertEquals(savedDto.userSeq, savedDto.userSeq)
        assertEquals(savedDto.notificationEnabled, savedDto.notificationEnabled)
        assertEquals(savedDto.eventEnabled, savedDto.eventEnabled)
        assertEquals(savedDto.themePreference, savedDto.themePreference)

    }


    @Test
    fun `등록 실패 - userSeq가 음수일때 등록 실패`() {
        //Given
        val userSettingDto = UserSettingDto().apply {
            userSeq = -1
            notificationEnabled = UserSettingEnabled.ON
            eventEnabled = UserSettingEnabled.ON
            themePreference = ThemePreference.DARK
        }

        //When
        val exception = assertThrows<IllegalArgumentException>{
            userSettingService.userSettingTableSetting(userSettingDto)
        }

        //Then
        assertTrue(exception.message!!.contains("유저의 시퀸스는 양수여야 합니다."))

    }

}