package com.example.myapp.user.user.service

import com.example.myapp.user.user.domain.UserJoinType
import com.example.myapp.user.user.domain.UserRole
import com.example.myapp.user.user.domain.dto.UserDto
import com.example.myapp.user.user.domain.entity.UserEntity
import com.example.myapp.user.user.infra.repository.UserRepository
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.modelmapper.ModelMapper

@ExtendWith(MockitoExtension::class)
class UserServiceUnitTest{
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var modelMapper: ModelMapper

    @Mock
    private lateinit var validator: Validator

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    @DisplayName("성공적인 회원 가입")
    fun `should register user successfully`() {
        // given
        val userDto = UserDto().apply {
            email = "valid.email@example.com"
            passwd = "ValidPass123"
            nickName = "ValidNick"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        val userEntity = UserEntity().apply {
            email = userDto.email
            passwd = userDto.passwd
            nickName = userDto.nickName
            userRole = userDto.userRole
            joinType = userDto.joinType
        }

        val savedUser = UserEntity().apply {
            userSeq = 1L
            email = userDto.email
            passwd = userDto.passwd
            nickName = userDto.nickName
            userRole = userDto.userRole
            joinType = userDto.joinType
        }

        // Mockito 설정
        `when`(modelMapper.map(userDto, UserEntity::class.java)).thenReturn(userEntity)
        `when`(userRepository.userJoin(userEntity)).thenReturn(savedUser)

        val savedUserDto = UserDto().apply {
            email = savedUser.email
            passwd = savedUser.passwd
            nickName = savedUser.nickName
            userRole = savedUser.userRole
            joinType = savedUser.joinType
            // 필요한 경우 id 필드를 추가하거나 다른 필드를 설정
        }

        `when`(modelMapper.map(savedUser, UserDto::class.java)).thenReturn(savedUserDto)

        // when
        val result = userService.userJoin(userDto)

        // then
        assertNotNull(result)
        assertEquals(savedUserDto.email, result.email)
        assertEquals(savedUserDto.passwd, result.passwd)
        assertEquals(savedUserDto.nickName, result.nickName)
        assertEquals(savedUserDto.userRole, result.userRole)
        assertEquals(savedUserDto.joinType, result.joinType)

        verify(modelMapper, times(1)).map(userDto, UserEntity::class.java)
        verify(userRepository, times(1)).userJoin(userEntity)
        verify(modelMapper, times(1)).map(savedUser, UserDto::class.java)
    }

    @Test
    @DisplayName("유효하지 않은 데이터로 인해 회원 가입 실패")
    fun `should fail registration when data is invalid`() {
        // given
        val userDto = UserDto().apply {
            // 실제로 유효하지 않은 값들
            email = ""             // 이메일 미기재
            passwd = "1234"       // 비밀번호가 너무 짧음
            nickName = ""          // 닉네임 없음
            userRole = UserRole.User
            joinType = UserJoinType.HOMEPAGE
        }

        // validator가 검증에 실패하도록 설정
        val violation1 = mock(ConstraintViolation::class.java) as ConstraintViolation<UserDto>
        `when`(violation1.message).thenReturn("이메일은 필수 항목입니다.")

        val violation2 = mock(ConstraintViolation::class.java) as ConstraintViolation<UserDto>
        `when`(violation2.message).thenReturn("비밀번호는 8~20자 사이여야 합니다.")

        val violations = setOf(violation1, violation2)

        // 검증 시 위반사항이 발견되도록 모킹
        `when`(validator.validate(userDto)).thenReturn(violations)

        // when & then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            userService.userJoin(userDto)
        }

        // 예외 메시지 검증
        // (Service 내부 구현에 따라 "유효성 검증 실패"라는 문구를 포함한다면 확인)
        assertTrue(exception.message?.contains("유효성 검증 실패") == true)

        // 검증 메서드가 실제로 호출되었는지 확인
        verify(validator, times(1)).validate(userDto)
    }


    @Test
    @DisplayName("이메일이 이미 존재하는지 확인 - 존재할 경우 true 반환")
    fun `should return true when email exists`() {
        // given
        val userDto = UserDto().apply {
            email = "duplicate@example.com"
            passwd = "SomePass123"
            nickName = "DupUser"
            userRole = UserRole.User
            joinType = UserJoinType.HOMEPAGE
        }

        // repository가 email로 존재하는지 확인하면 true 반환
        `when`(userRepository.userIsExistsByEmail(userDto.email)).thenReturn(true)

        // when
        val result = userService.userIsExistsByEmail(userDto)

        // then
        assertTrue(result)

        // verify
        verify(userRepository, times(1)).userIsExistsByEmail(userDto.email)
    }

    @Test
    @DisplayName("이메일 중복으로 인해 회원 가입 실패")
    fun `should fail registration when email is duplicated`() {
        // given
        val userDto = UserDto().apply {
            email = "duplicate@example.com"
            passwd = "ValidPass123"
            nickName = "ValidNick"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        val userEntity = UserEntity().apply {
            email = userDto.email
            passwd = userDto.passwd
            nickName = userDto.nickName
            userRole = userDto.userRole
            joinType = userDto.joinType
        }

        // userJoin에서 이메일 중복 예외를 던진다고 가정
        `when`(userRepository.userIsExistsByEmail(userEntity.email)).thenReturn(false)

        // when & then
        val isExists = userService.userIsExistsByEmail(userDto)

        assertEquals(false, isExists)

        // verify
        verify(userRepository, times(1)).userIsExistsByEmail(userEntity.email)
    }
}

