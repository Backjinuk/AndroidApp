package com.example.myapp.user.user.service

import com.example.myapp.user.user.domain.UserJoinType
import com.example.myapp.user.user.domain.UserRole
import com.example.myapp.user.user.domain.dto.UserDto
import com.example.myapp.user.user.domain.dto.UserTokenDto
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserServiceIntegrationTest @Autowired constructor(
    private val userService: UserService
) {

    @Test
    @Order(1)
    @Transactional
    fun `회원가입 테스트 - 정상 케이스`() {
        // given
        val userDto = UserDto().apply {
            email = "valid.email@example.com"
            passwd = "ValidPass123"
            nickName = "ValidNick"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        val joinedUser = userService.registerUser(userDto)

        // then
        assertNotNull(joinedUser.userSeq)  // 정상적으로 저장되어 id가 발급되었는지 확인
        assertEquals(userDto.email, joinedUser.email)
        assertEquals(userDto.passwd, joinedUser.passwd)
        assertEquals(userDto.nickName, joinedUser.nickName)
        assertEquals(userDto.userRole, joinedUser.userRole)
        assertEquals(userDto.joinType, joinedUser.joinType)
    }

    @Test
    @Order(2)
    @Transactional
    fun `이메일로 사용자 존재 여부 조회 테스트 - 존재하는 경우`() {
        // given
        val userDto = UserDto().apply {
            email = "valid.email@example.com"
            passwd = "ValidPass123"
            nickName = "ValidNick"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        // when
        val joinedUser = userService.registerUser(userDto)

        val exists = userService.userIsExistsByEmail(userDto)

        // then
        assertTrue(exists)
    }


    @Test
    @Order(3)
    fun `이메일로 사용자 존재 여부 조회 테스트 - 존재하지 않는 경우`() {
        // given
        val userDto = UserDto().apply {
            email = "valid.not.email@example.com"
            passwd = "ValidPass123"
            nickName = "ValidNick"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        // when
        val exists = userService.userIsExistsByEmail(userDto)

        // then
        assertFalse(exists, "가입되어 있지 않은 이메일이므로 false를 기대함")
    }

    @Test
    @Order(5)
    @Transactional
    fun `회원가입 테스트 - 유효하지 않은 데이터로 인해 실패`() {
        // given
        val invalidUserDto = UserDto().apply {
            email = ""                     // 이메일 미기재
            passwd = "1234"               // 비밀번호가 너무 짧음
            nickName = ""                  // 닉네임 없음
            userRole = UserRole.User
            joinType = UserJoinType.HOMEPAGE
        }

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            userService.registerUser(invalidUserDto)
        }

        // 예외 메시지 검증
        assertTrue(exception.message?.contains("유효성 검증 실패") == true, "유효성 검증 실패 메시지가 포함되어야 합니다.")
        assertTrue(exception.message?.contains("이메일은 필수 항목입니다.") == true, "이메일 필수 메시지가 포함되어야 합니다.")
        assertTrue(exception.message?.contains("비밀번호는 8~20자 사이여야 합니다.") == true, "비밀번호 길이 메시지가 포함되어야 합니다.")
    }

    @Test
    @Order(6)
    @Transactional
    fun `토큰 등록 테스트 - 정상 케이스`() {
        // Given
        val userDto = UserDto().apply {
            email = "token.user@example.com"
            passwd = "ValidPass123"
            nickName = "TokenUser"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        // 사용자 등록
        val joinedUser = userService.registerUser(userDto)

        val userTokenDto = UserTokenDto().apply {
            userSeq = joinedUser.userSeq
            refreshToken = "validRefreshToken"
            expiredDt = LocalDateTime.now().plusDays(1)
        }

        // When
        val savedToken = userService.addUserTokenByUserSeq(userTokenDto)

        // Then
        assertNotNull(savedToken.userTokenSeq, "토큰이 정상적으로 저장되어야 합니다.")
        assertEquals(userTokenDto.userSeq, savedToken.userSeq, "userSeq가 일치해야 합니다.")
        assertEquals(userTokenDto.refreshToken, savedToken.refreshToken, "refreshToken이 일치해야 합니다.")
        assertEquals(userTokenDto.expiredDt, savedToken.expiredDt, "expiredDt가 일치해야 합니다.")
    }

    @Test
    @Order(7)
    @Transactional
    fun `토큰 등록 테스트 - userSeq가 음수이면 실패`() {
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

        assertTrue(exception.message?.contains("유저 시퀸스는 양수여야 합니다.") == true, "userSeq가 음수일 경우 예외 메시지가 포함되어야 합니다.")
    }

    @Test
    @Order(8)
    @Transactional
    fun `토큰 등록 테스트 - refreshToken이 비어있으면 실패`() {
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

        assertTrue(exception.message?.contains("refreshToken은 비어있을수 없습니다.") == true, "refreshToken이 비어있을 경우 예외 메시지가 포함되어야 합니다.")
    }

    @Test
    @Order(10)
    @Transactional
    fun `토큰 등록 테스트 - 모든 필드가 유효하지 않으면 여러 예외가 발생`() {
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

        assertTrue(exception.message!!.contains("유저 시퀸스는 양수여야 합니다."), "userSeq 음수 메시지가 포함되어야 합니다.")
        assertTrue(exception.message!!.contains("refreshToken은 비어있을수 없습니다."), "refreshToken 비어있음 메시지가 포함되어야 합니다.")
    }

}