package com.example.myapp.user.user.service

import com.example.myapp.Entity.QUser.user
import com.example.myapp.user.user.domain.UserJoinType
import com.example.myapp.user.user.domain.UserRole
import com.example.myapp.user.user.domain.dto.UserDto
import com.example.myapp.user.user.domain.entity.UserEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

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

        val joinedUser = userService.userJoin(userDto)

        // then
        assertNotNull(joinedUser.userSeq)  // 정상적으로 저장되어 id가 발급되었는지 확인
        assertEquals(userDto.email, joinedUser.email)
        assertEquals(userDto.passwd, joinedUser.passwd)
        assertEquals(userDto.nickName, joinedUser.nickName)
    }

    @Test
    @Order(2)
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
}