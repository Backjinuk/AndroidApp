package com.example.myapp.user.useCase

import com.example.myapp.user.user.domain.UserJoinType
import com.example.myapp.user.user.domain.UserRole
import com.example.myapp.user.user.domain.dto.UserDto
import com.example.myapp.user.user.domain.dto.UserTokenDto
import com.example.myapp.user.user.service.UserService
import com.example.myapp.user.userProfile.domain.dto.UserProfileDto
import com.example.myapp.user.userProfile.service.UserProfileService
import com.example.myapp.user.userSetting.domain.dto.UserSettingDto
import com.example.myapp.user.userSetting.service.UserSettingService
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.modelmapper.ModelMapper

@ExtendWith(MockKExtension::class)
class UserUseCaseUnitTest {

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var userProfileService: UserProfileService

    @MockK
    private lateinit var userSettingService: UserSettingService

    @MockK
    private lateinit var modelMapper: ModelMapper

    @InjectMockKs
    private lateinit var userUseCaseInteractor: UserUseCaseInteract

    @Test
    fun `registerUser - 성공적으로 회원 가입을 완료한다`() = runBlocking {
        // given
        val userDto = UserDto().apply {
            email = "valid.email@example.com"
            passwd = "ValidPass123"
            nickName = "ValidNick"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        val registeredUserDto = UserDto().apply {
            userSeq = 1L
            email = userDto.email
            passwd = userDto.passwd
            nickName = userDto.nickName
            userRole = userDto.userRole
            joinType = userDto.joinType
        }

        // Mocking
        every { userService.registerUser(userDto) } returns registeredUserDto
        every { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) } returns UserProfileDto()
        every { userSettingService.createDefaultUserSettings(any<UserSettingDto>()) } returns UserSettingDto()
        every { userService.addUserTokenByUserSeq(any<UserTokenDto>()) } returns UserTokenDto()


        // when
        val result = userUseCaseInteractor.registerUser(userDto)

        // then
        assertNotNull(result.userSeq, "userSeq는 null이 아니어야 합니다.")
        assertEquals(1L, result.userSeq, "userSeq가 일치해야 합니다.")
        assertEquals(userDto.email, result.email, "이메일이 일치해야 합니다.")
        assertEquals(userDto.passwd, result.passwd, "비밀번호가 일치해야 합니다.")
        assertEquals(userDto.nickName, result.nickName, "닉네임이 일치해야 합니다.")
        assertEquals(userDto.userRole, result.userRole, "UserRole이 일치해야 합니다.")
        assertEquals(userDto.joinType, result.joinType, "UserJoinType이 일치해야 합니다.")

        // Verify interactions
        coVerify(exactly = 1) { userService.registerUser(userDto) }
        coVerify(exactly = 1) { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) }
        coVerify(exactly = 1) { userSettingService.createDefaultUserSettings(any<UserSettingDto>()) }
        coVerify(exactly = 1) { userService.addUserTokenByUserSeq(any<UserTokenDto>()) }

        confirmVerified(userService, userProfileService, userSettingService, modelMapper)
    }

    @Test
    fun `registerUser - 이메일 중복 시 회원 가입 실패`() = runBlocking {
        // given
        val userDto = UserDto().apply {
            email = "duplicate@example.com"
            passwd = "ValidPass123"
            nickName = "DuplicateUser"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        // Mocking: 이메일 중복으로 인해 예외 발생
        every { userService.registerUser(userDto) } throws IllegalArgumentException("이미 존재하는 이메일입니다.")

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            userUseCaseInteractor.registerUser(userDto)
        }

        assertEquals("이미 존재하는 이메일입니다.", exception.message, "예외 메시지가 일치해야 합니다.")

        // Verify that subsequent services are not called
        coVerify(exactly = 1) { userService.registerUser(userDto) }
        coVerify(exactly = 0) { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) }
        coVerify(exactly = 0) { userSettingService.createDefaultUserSettings(any<UserSettingDto>()) }
        coVerify(exactly = 0) { userService.addUserTokenByUserSeq(any<UserTokenDto>()) }

        confirmVerified(userService, userProfileService, userSettingService, modelMapper)
    }

    @Test
    fun `registerUser - 프로필 등록 실패 시 전체 트랜잭션 롤백`() = runBlocking {
        // given
        val userDto = UserDto().apply {
            email = "profile.fail@example.com"
            passwd = "ValidPass123"
            nickName = "ProfileFailUser"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        val registeredUserDto = UserDto().apply {
            userSeq = 2L
            email = userDto.email
            passwd = userDto.passwd
            nickName = userDto.nickName
            userRole = userDto.userRole
            joinType = userDto.joinType
        }

        // Mocking
        every { userService.registerUser(userDto) } returns registeredUserDto
        every { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) } throws RuntimeException("프로필 등록 실패")
        // userSettingService.createDefaultUserSettings는 호출되지 않아야 함
        // userService.addUserTokenByUserSeq는 호출되지 않아야 함

        // when & then
        val exception = assertThrows<RuntimeException> {
            userUseCaseInteractor.registerUser(userDto)
        }

        assertEquals("프로필 등록 실패", exception.message, "예외 메시지가 일치해야 합니다.")

        // Verify interactions
        coVerify(exactly = 1) { userService.registerUser(userDto) }
        coVerify(exactly = 1) { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) }
        coVerify(exactly = 0) { userSettingService.createDefaultUserSettings(any<UserSettingDto>()) }
        coVerify(exactly = 0) { userService.addUserTokenByUserSeq(any<UserTokenDto>()) }

        confirmVerified(userService, userProfileService, userSettingService, modelMapper)
    }

    @Test
    fun `registerUser - 세팅 정보 등록 실패 시 전체 트랜잭션 롤백`() = runBlocking {
        // given
        val userDto = UserDto().apply {
            email = "settings.fail@example.com"
            passwd = "ValidPass123"
            nickName = "SettingsFailUser"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        val registeredUserDto = UserDto().apply {
            userSeq = 3L
            email = userDto.email
            passwd = userDto.passwd
            nickName = userDto.nickName
            userRole = userDto.userRole
            joinType = userDto.joinType
        }

        // Mocking
        every { userService.registerUser(userDto) } returns registeredUserDto
        every { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) } returns UserProfileDto()
        every { userSettingService.createDefaultUserSettings(any<UserSettingDto>()) } throws RuntimeException("세팅 정보 등록 실패")
        // userService.addUserTokenByUserSeq는 호출되지 않아야 함

        // when & then
        val exception = assertThrows<RuntimeException> {
            userUseCaseInteractor.registerUser(userDto)
        }

        assertEquals("세팅 정보 등록 실패", exception.message, "예외 메시지가 일치해야 합니다.")

        // Verify interactions
        coVerify(exactly = 1) { userService.registerUser(userDto) }
        coVerify(exactly = 1) { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) }
        coVerify(exactly = 1) { userSettingService.createDefaultUserSettings(any<UserSettingDto>()) }
        coVerify(exactly = 0) { userService.addUserTokenByUserSeq(any<UserTokenDto>()) }

        confirmVerified(userService, userProfileService, userSettingService, modelMapper)
    }

    @Test
    fun `registerUser - 토큰 등록 실패 시 전체 트랜잭션 롤백`() = runBlocking {
        // given
        val userDto = UserDto().apply {
            email = "token.fail@example.com"
            passwd = "ValidPass123"
            nickName = "TokenFailUser"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        val registeredUserDto = UserDto().apply {
            userSeq = 4L
            email = userDto.email
            passwd = userDto.passwd
            nickName = userDto.nickName
            userRole = userDto.userRole
            joinType = userDto.joinType
        }

        // Mocking
        every { userService.registerUser(userDto) } returns registeredUserDto
        every { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) } returns UserProfileDto()
        every { userSettingService.createDefaultUserSettings(any<UserSettingDto>()) } returns UserSettingDto()
        every { userService.addUserTokenByUserSeq(any<UserTokenDto>()) } throws RuntimeException("토큰 등록 실패")

        // when & then
        val exception = assertThrows<RuntimeException> {
            userUseCaseInteractor.registerUser(userDto)
        }

        assertEquals("토큰 등록 실패", exception.message, "예외 메시지가 일치해야 합니다.")

        // Verify interactions
        coVerify(exactly = 1) { userService.registerUser(userDto) }
        coVerify(exactly = 1) { userProfileService.createDefaultUserProfile(any<UserProfileDto>()) }
        coVerify(exactly = 1) { userSettingService.createDefaultUserSettings(any<UserSettingDto>()) }
        coVerify(exactly = 1) { userService.addUserTokenByUserSeq(any<UserTokenDto>()) }

        confirmVerified(userService, userProfileService, userSettingService, modelMapper)
    }
}