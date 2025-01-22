package com.example.myapp.user.userSetting.service

import com.example.myapp.user.userSetting.domain.ThemePreference
import com.example.myapp.user.userSetting.domain.UserSettingEnabled
import com.example.myapp.user.userSetting.domain.dto.UserSettingDto
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertTrue

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserSettingServiceIntegrationTest @Autowired constructor(
    private val userSettingService: UserSettingService
) {

    @Nested
    @DisplayName("createDefaultUserSetting 메서드 테스트")
    inner class createDefaultUserSettingTests{

        @Test
        fun `등록 성공 - 유효한 userSettingDto는 db에 저장된다`(){
            //Given
            val userSettingDto = UserSettingDto().apply {
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            //When
            val result = userSettingService.createDefaultUserSettings(userSettingDto)

            //Then
            assertTrue(result.userSettingSeq != 0L)
            assertEquals(result.userSeq, userSettingDto.userSeq)
            assertEquals(result.notificationEnabled, userSettingDto.notificationEnabled)
            assertEquals(result.eventEnabled, userSettingDto.eventEnabled)
            assertEquals(result.themePreference, userSettingDto.themePreference)
        }

        @Test
        fun `등록 실패 - userSeq가 0인 경우 예외가 발생한다`(){
            //Given
            val userSettingDto = UserSettingDto().apply {
                userSeq = -1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            //When
            val exception = assertThrows<IllegalArgumentException> {
                userSettingService.createDefaultUserSettings(userSettingDto)
            }

            //Then
            assertTrue { exception.message!!.contains("유저의 시퀸스는 양수여야 합니다.") }
        }

    }

    
}