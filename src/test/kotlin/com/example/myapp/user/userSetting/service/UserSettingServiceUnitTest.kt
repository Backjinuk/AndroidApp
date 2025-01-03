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
import io.mockk.mockk
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    private lateinit var modelMapper: ModelMapper

    @InjectMockKs
    private lateinit var userSettingService: UserSettingService

    @Test
    fun `등록 성공 - 유효한 UserSettingDto는 db에 저장되어야 한다`() {

        // Given
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

        // Mocking Validator to return no violations
        every { validator.validate(userSettingDto) } returns emptySet()

        // Mocking ModelMapper to map DTO to Entity
        every { modelMapper.map(userSettingDto, UserSettingEntity::class.java) } returns savedEntity

        // Mocking Repository to save the entity
        every { userSettingRepository.userSettingTableSetting(savedEntity) } returns savedEntity

        // Mocking ModelMapper to map Entity back to DTO if needed
        every { modelMapper.map(savedEntity, UserSettingDto::class.java) } returns userSettingDto

        // When
        val savedValue = userSettingService.userSettingTableSetting(userSettingDto)

        // Then
        assertEquals(userSettingDto.userSeq, savedValue.userSeq, "userSeq가 일치해야 합니다.")
        assertEquals(userSettingDto.notificationEnabled, savedValue.notificationEnabled, "notificationEnabled가 일치해야 합니다.")
        assertEquals(userSettingDto.eventEnabled, savedValue.eventEnabled, "eventEnabled가 일치해야 합니다.")
        assertEquals(userSettingDto.themePreference, savedValue.themePreference, "themePreference가 일치해야 합니다.")
    }


    @Test
    fun `등록 실패 - userSeq가 음수이면 예외가 발생한다`() {
        // Given
        val userSettingDto = UserSettingDto().apply {
            userSeq = -1
            notificationEnabled = UserSettingEnabled.ON
            eventEnabled = UserSettingEnabled.ON
            themePreference = ThemePreference.DARK
        }

        // Mocking Validator to return violations
        val violationMock = mockk<ConstraintViolation<UserSettingDto>>().apply {
            every { propertyPath.toString() } returns "userSeq"
            every { message } returns "유저의 시퀸스는 양수여야 합니다."
        }
        every { validator.validate(userSettingDto) } returns setOf(violationMock)

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            userSettingService.userSettingTableSetting(userSettingDto)
        }

        assertTrue(exception.message!!.contains("유저의 시퀸스는 양수여야 합니다."))
    }}