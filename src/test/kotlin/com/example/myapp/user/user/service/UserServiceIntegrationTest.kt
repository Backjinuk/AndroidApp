package com.example.myapp.user.user.service

import com.example.myapp.user.user.domain.UserJoinType
import com.example.myapp.user.user.domain.UserRole
import com.example.myapp.user.user.domain.dto.UserDto
import com.example.myapp.user.user.domain.dto.UserTokenDto
import com.example.myapp.user.userSetting.infra.repository.UserSettingRepository
import jakarta.validation.Validator
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest @Autowired constructor(
    private val userService: UserService,
    private val userSettingRepository: UserSettingRepository,
    private val validator: Validator
) {

    @Nested
    @DisplayName("registerUser 메서드 테스트")
    inner class RegisterUserTests {

        @Test
        @Order(1)
        @DisplayName("회원가입 테스트 - 정상 케이스")
        fun `should register user successfully with valid data`() {
            // Given
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // When
            val registeredUser = userService.registerUser(userDto)

            // Then
            assertNotNull(registeredUser.userSeq, "UserSeq should not be null after registration.")
            assertEquals(userDto.email, registeredUser.email, "Emails should match.")
            assertEquals(userDto.passwd, registeredUser.passwd, "Passwords should match.")
            assertEquals(userDto.nickName, registeredUser.nickName, "Nicknames should match.")
            assertEquals(userDto.userRole, registeredUser.userRole, "UserRoles should match.")
            assertEquals(userDto.joinType, registeredUser.joinType, "JoinTypes should match.")
        }

        @Test
        @Order(2)
        @DisplayName("이메일로 사용자 존재 여부 조회 테스트 - 존재하는 경우")
        fun `should return true when user exists by email`() {
            // Given
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            userService.registerUser(userDto)

            // When
            val exists = userService.userIsExistsByEmail(userDto)

            // Then
            assertTrue(exists, "User should exist for the given email.")
        }

        @Test
        @Order(3)
        @DisplayName("이메일로 사용자 존재 여부 조회 테스트 - 존재하지 않는 경우")
        fun `should return false when user does not exist by email`() {
            // Given
            val userDto = UserDto().apply {
                email = "nonexistent.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            // When
            val exists = userService.userIsExistsByEmail(userDto)

            // Then
            assertFalse(exists, "User should not exist for the given email.")
        }

        @Test
        @Order(4)
        @DisplayName("회원가입 테스트 - 유효하지 않은 데이터로 인해 실패")
        fun `should throw exception when registering with invalid data`() {
            // Given
            val invalidUserDto = UserDto().apply {
                email = ""                     // 이메일 미기재
                passwd = "1234"               // 비밀번호가 너무 짧음
                nickName = ""                  // 닉네임 없음
                userRole = UserRole.User
                joinType = UserJoinType.HOMEPAGE
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.registerUser(invalidUserDto)
            }

            // 예외 메시지 검증
            assertTrue(
                exception.message?.contains("유효성 검증 실패") == true,
                "Exception message should contain '유효성 검증 실패'."
            )
            assertTrue(
                exception.message?.contains("이메일은 필수 항목입니다.") == true,
                "Exception message should contain '이메일은 필수 항목입니다.'."
            )
            assertTrue(
                exception.message?.contains("비밀번호는 8~20자 사이여야 합니다.") == true,
                "Exception message should contain '비밀번호는 8~20자 사이여야 합니다.'."
            )
            assertTrue(
                exception.message?.contains("닉네임은 필수 항목입니다.") == true,
                "Exception message should contain '닉네임은 필수 항목입니다.'."
            )
        }
    }

    @Nested
    @DisplayName("addUserTokenByUserSeq 메서드 테스트")
    inner class AddUserTokenByUserSeqTests {

        @Test
        @Order(5)
        @DisplayName("토큰 등록 테스트 - 정상 케이스")
        fun `should add user token successfully with valid data`() {
            // Given
            val userDto = UserDto().apply {
                email = "token.user@example.com"
                passwd = "ValidPass123"
                nickName = "TokenUser"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // 사용자 등록
            val registeredUser = userService.registerUser(userDto)

            val userTokenDto = UserTokenDto().apply {
                userSeq = registeredUser.userSeq
                refreshToken = "validRefreshToken"
                expiredDt = LocalDateTime.now().plusDays(1)
            }

            // When
            val savedToken = userService.addUserTokenByUserSeq(userTokenDto)

            // Then
            assertNotNull(savedToken.userTokenSeq, "TokenSeq should not be null after saving.")
            assertEquals(userTokenDto.userSeq, savedToken.userSeq, "userSeq should match.")
            assertEquals(userTokenDto.refreshToken, savedToken.refreshToken, "refreshToken should match.")
            assertEquals(userTokenDto.expiredDt, savedToken.expiredDt, "expiredDt should match.")
        }

        @Test
        @Order(6)
        @DisplayName("토큰 등록 테스트 - userSeq가 음수이면 실패")
        fun `should throw exception when userSeq is negative`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = -5
                refreshToken = "invalidRefreshToken"
                expiredDt = LocalDateTime.now().plusDays(1)
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(
                exception.message?.contains("유저 시퀸스는 양수여야 합니다.") == true,
                "Exception message should contain '유저 시퀸스는 양수여야 합니다.'."
            )
        }

        @Test
        @Order(7)
        @DisplayName("토큰 등록 테스트 - refreshToken이 비어있으면 실패")
        fun `should throw exception when refreshToken is empty`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = 100
                refreshToken = ""
                expiredDt = LocalDateTime.now().plusDays(1)
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(
                exception.message?.contains("refreshToken은 비어있을수 없습니다.") == true,
                "Exception message should contain 'refreshToken은 비어있을수 없습니다.'."
            )
        }

        @Test
        @Order(8)
        @DisplayName("토큰 등록 테스트 - 모든 필드가 유효하지 않으면 여러 예외가 발생")
        fun `should throw multiple exceptions when all fields are invalid`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = -10
                refreshToken = ""
                expiredDt = null
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(
                exception.message?.contains("유저 시퀸스는 양수여야 합니다.") == true,
                "Exception message should contain '유저 시퀸스는 양수여야 합니다.'."
            )
            assertTrue(
                exception.message?.contains("refreshToken은 비어있을수 없습니다.") == true,
                "Exception message should contain 'refreshToken은 비어있을수 없습니다.'."
            )
            assertTrue(
                exception.message?.contains("만료일시는 null일 수 없습니다.") == true,
                "Exception message should contain '만료일시는 null일 수 없습니다.'."
            )
        }
    }
}
