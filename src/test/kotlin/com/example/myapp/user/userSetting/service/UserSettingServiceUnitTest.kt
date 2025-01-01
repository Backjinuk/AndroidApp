package com.example.myapp.user.userSetting.service

import com.example.myapp.user.userSetting.domain.ThemePreference
import com.example.myapp.user.userSetting.domain.UserSettingEnabled
import com.example.myapp.user.userSetting.domain.dto.UserSettingDto
import com.example.myapp.user.userSetting.domain.entity.UserSettingEntity
import com.example.myapp.user.userSetting.infra.repository.UserSettingRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.modelmapper.ModelMapper
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class UserSettingServiceUnitTest {

    @MockK
    private lateinit var userSettingRepository: UserSettingRepository

    @MockK
    private lateinit var validator: Validator

    @MockK
    private lateinit var modalMapper: ModelMapper

    @InjectMockKs
    private lateinit var userSettingService: UserSettingService


    @Test
    fun `등록 성공 - 유효한 UserSettingDto는 db에 저장되어야 한다`() {

        //Given
        val userSettingDto = UserSettingDto().apply {
            userSeq = 100
            notificationEnabled = UserSettingEnabled.ON
            eventEnabled = UserSettingEnabled.ON
            themePreference = ThemePreference.DARK
        }


        val savedEntity = UserSettingEntity().apply {
            userSeq = userSettingDto.userSeq
            notificationEnabled = userSettingDto.notificationEnabled
            eventEnabled = userSettingDto.eventEnabled
            themePreference = userSettingDto.themePreference
        }

        every { validator.validate(userSettingDto) } returns emptySet()

        every { modalMapper.map(userSettingDto, UserSettingEntity::class.java) } returns savedEntity

        every { modalMapper.map(savedEntity, UserSettingDto::class.java) } returns userSettingDto

        every { userSettingRepository.userSettingTableSetting(savedEntity) } returns savedEntity

        //When
        val savedValue = userSettingService.userSettingTableSetting(userSettingDto)

        //Then
        assertEquals(savedValue.userSeq, userSettingDto.userSeq)
        assertEquals(savedValue.notificationEnabled, userSettingDto.notificationEnabled)
        assertEquals(savedValue.eventEnabled, userSettingDto.eventEnabled)
        assertEquals(savedValue.themePreference, userSettingDto.themePreference)

    }


}