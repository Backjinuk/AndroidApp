package com.example.myapp.user.user.service

import com.example.myapp.user.user.domain.UserJoinType
import com.example.myapp.user.user.domain.UserRole
import com.example.myapp.user.user.domain.dto.UserDto
import com.example.myapp.user.user.domain.dto.UserTokenDto
import com.example.myapp.user.user.domain.entity.UserEntity
import com.example.myapp.user.user.domain.entity.UserTokenEntity
import com.example.myapp.user.user.infra.repository.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.modelmapper.ModelMapper
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class UserServiceUnitTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var modelMapper: ModelMapper

    @MockK
    private lateinit var validator: Validator

    @InjectMockKs
    private lateinit var userService: UserService

    @Nested
    @DisplayName("registerUser 메서드 테스트")
    inner class RegisterUser(){
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
                userSeq = 1L // assuming userSeq is set upon saving
                email = userDto.email
                passwd = userDto.passwd
                nickName = userDto.nickName
                userRole = userDto.userRole
                joinType = userDto.joinType
            }

            // Mocking Validator to return no violations
            every { validator.validate(userDto) } returns emptySet()

            // Mocking ModelMapper to map DTO to Entity
            every { modelMapper.map(userDto, UserEntity::class.java) } returns userEntity

            // Mocking Repository to save the entity
            every { userRepository.userJoin(userEntity) } returns savedUser

            // Mocking ModelMapper to map Entity back to DTO
            val savedUserDto = UserDto().apply {
                userSeq = savedUser.userSeq!!
                email = savedUser.email
                passwd = savedUser.passwd
                nickName = savedUser.nickName
                userRole = savedUser.userRole
                joinType = savedUser.joinType
                // regDt는 필요에 따라 설정
            }
            every { modelMapper.map(savedUser, UserDto::class.java) } returns savedUserDto

            // when
            val result = userService.registerUser(userDto)

            // then
            assertNotNull(result, "결과는 null이 아니어야 합니다.")
            assertEquals(savedUserDto.email, result.email, "email이 일치해야 합니다.")
            assertEquals(savedUserDto.passwd, result.passwd, "passwd가 일치해야 합니다.")
            assertEquals(savedUserDto.nickName, result.nickName, "nickName이 일치해야 합니다.")
            assertEquals(savedUserDto.userRole, result.userRole, "userRole이 일치해야 합니다.")
            assertEquals(savedUserDto.joinType, result.joinType, "joinType이 일치해야 합니다.")

            verify(exactly = 1) { modelMapper.map(userDto, UserEntity::class.java) }
            verify(exactly = 1) { userRepository.userJoin(userEntity) }
            verify(exactly = 1) { modelMapper.map(savedUser, UserDto::class.java) }
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
            val violation1 = mockk<ConstraintViolation<UserDto>>().apply {
                every { propertyPath.toString() } returns "email"
                every { message } returns "이메일은 필수 항목입니다."
            }

            val violation2 = mockk<ConstraintViolation<UserDto>>().apply {
                every { propertyPath.toString() } returns "passwd"
                every { message } returns "비밀번호는 8~20자 사이여야 합니다."
            }

            val violations = setOf(violation1, violation2)

            // 검증 시 위반사항이 발견되도록 모킹
            every { validator.validate(userDto) } returns violations

            // when & then
            val exception = assertThrows<IllegalArgumentException> {
                userService.registerUser(userDto)
            }

            // 예외 메시지 검증
            assertTrue(exception.message?.contains("유효성 검증 실패") == true)
            assertTrue(exception.message?.contains("이메일은 필수 항목입니다.") == true)
            assertTrue(exception.message?.contains("비밀번호는 8~20자 사이여야 합니다.") == true)

            // 검증 메서드가 실제로 호출되었는지 확인
            verify(exactly = 1) { validator.validate(userDto) }
            // 추가적인 상호작용이 없었는지 확인
            confirmVerified(modelMapper, userRepository, validator)
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
            every { userRepository.userIsExistsByEmail(userDto.email) } returns true

            // when
            val result = userService.userIsExistsByEmail(userDto)

            // then
            assertTrue(result, "이미 존재하는 이메일이라면 true를 반환해야 합니다.")

            // verify
            verify(exactly = 1) { userRepository.userIsExistsByEmail(userDto.email) }
            confirmVerified(userRepository, modelMapper, validator)
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

            // repository가 email로 존재하는지 확인하면 true 반환
            every { userRepository.userIsExistsByEmail(userDto.email) } returns true

            // when & then
            val userJoin = userService.userIsExistsByEmail(userDto)

            verify(exactly = 1) { userRepository.userIsExistsByEmail(userDto.email) }

            assertEquals(userJoin, true)
        }
    }


   @Nested
   @DisplayName("addUserTokenByUserSeq 메서드 테스트")
   inner class AddUserTokenByUserSeq(){
       @org.junit.jupiter.api.Test
       @DisplayName("등록 성공 - 유효한 UserTokenDto는 DB에 저장되고 반환되어야 한다")
       fun `등록 성공 - 유효한 UserTokenDto는 DB에 저장되고 반환되어야 한다`() {
           // Given
           val userTokenDto = UserTokenDto().apply {
               userSeq = 100
               refreshToken = "validRefreshToken"
               expiredDt = LocalDateTime.now().plusDays(1)
           }

           val userTokenEntity = UserTokenEntity().apply {
               userSeq = userTokenDto.userSeq
               refreshToken = userTokenDto.refreshToken
               expiredDt = userTokenDto.expiredDt
           }

           val savedEntity = UserTokenEntity().apply {
               userTokenSeq = 1L // assuming userTokenSeq is set upon saving
               userSeq = userTokenDto.userSeq
               refreshToken = userTokenDto.refreshToken
               expiredDt = userTokenDto.expiredDt
               regDt = LocalDateTime.now() // set regDt if needed
           }

           // Mocking Validator to return no violations
           every { validator.validate(userTokenDto) } returns emptySet()

           // Mocking ModelMapper to map DTO to Entity
           every { modelMapper.map(userTokenDto, UserTokenEntity::class.java) } returns userTokenEntity

           // Mocking Repository to save the entity
           every { userRepository.addUserTokenByUserSeq(userTokenEntity) } returns savedEntity

           // Mocking ModelMapper to map Entity back to DTO
           val savedUserDto = UserTokenDto().apply {
               userTokenSeq = savedEntity.userTokenSeq
               userSeq = savedEntity.userSeq
               refreshToken = savedEntity.refreshToken
               expiredDt = savedEntity.expiredDt
               // regDt는 필요에 따라 설정
           }
           every { modelMapper.map(savedEntity, UserTokenDto::class.java) } returns savedUserDto

           // When
           val result = userService.addUserTokenByUserSeq(userTokenDto)

           // Then
           assertNotNull(result, "결과는 null이 아니어야 합니다.")
           assertEquals(savedEntity.userTokenSeq, result.userTokenSeq, "userTokenSeq가 일치해야 합니다.")
           assertEquals(userTokenDto.userSeq, result.userSeq, "userSeq가 일치해야 합니다.")
           assertEquals(userTokenDto.refreshToken, result.refreshToken, "refreshToken이 일치해야 합니다.")
           assertEquals(userTokenDto.expiredDt, result.expiredDt, "expiredDt가 일치해야 합니다.")

           verify(exactly = 1) { validator.validate(userTokenDto) }
           verify(exactly = 1) { modelMapper.map(userTokenDto, UserTokenEntity::class.java) }
           verify(exactly = 1) { userRepository.addUserTokenByUserSeq(userTokenEntity) }
           verify(exactly = 1) { modelMapper.map(savedEntity, UserTokenDto::class.java) }
           confirmVerified(validator, modelMapper, userRepository)
       }

       @org.junit.jupiter.api.Test
       @DisplayName("등록 실패 - userSeq가 음수이면 예외가 발생한다")
       fun `등록 실패 - userSeq가 음수이면 예외가 발생한다`() {
           // Given
           val userTokenDto = UserTokenDto().apply {
               userSeq = -1
               refreshToken = "validRefreshToken"
               expiredDt = LocalDateTime.now().plusDays(1)
           }

           // Mocking Validator to return violations
           val violation: ConstraintViolation<UserTokenDto> = mockk {
               every { propertyPath.toString() } returns "userSeq"
               every { message } returns "유저 시퀸스는 양수여야 합니다."
           }

           every { validator.validate(userTokenDto) } returns setOf(violation)

           // When & Then
           val exception = assertThrows<IllegalArgumentException> {
               userService.addUserTokenByUserSeq(userTokenDto)
           }

           assertTrue(exception.message!!.contains("유저 시퀸스는 양수여야 합니다."))

           // verify
           verify(exactly = 1) { validator.validate(userTokenDto) }
           // No other interactions should occur
           verify { modelMapper wasNot Called }
           verify { userRepository.addUserTokenByUserSeq(any()) wasNot Called }

           confirmVerified(userRepository, modelMapper, validator)
       }

       @org.junit.jupiter.api.Test
       @DisplayName("등록 실패 - refreshToken이 비어있으면 예외가 발생한다")
       fun `등록 실패 - refreshToken이 비어있으면 예외가 발생한다`() {
           // Given
           val userTokenDto = UserTokenDto().apply {
               userSeq = 100
               refreshToken = ""
               expiredDt = LocalDateTime.now().plusDays(1)
           }

           // Mocking Validator to return violations
           val violation: ConstraintViolation<UserTokenDto> = mockk {
               every { propertyPath.toString() } returns "refreshToken"
               every { message } returns "refreshToken은 비어있을수 없습니다."
           }
           every { validator.validate(userTokenDto) } returns setOf(violation)

           // When & Then
           val exception = assertThrows<IllegalArgumentException> {
               userService.addUserTokenByUserSeq(userTokenDto)
           }

           assertTrue(exception.message!!.contains("refreshToken은 비어있을수 없습니다."))

           // verify
           verify(exactly = 1) { validator.validate(userTokenDto) }
           // No other interactions should occur
           verify { modelMapper wasNot Called }
           verify { userRepository.addUserTokenByUserSeq(any()) wasNot Called }

           confirmVerified(userRepository, modelMapper, validator)
       }

       @org.junit.jupiter.api.Test
       @DisplayName("등록 실패 - expiredDt가 null이면 예외가 발생한다")
       fun `등록 실패 - expiredDt가 null이면 예외가 발생한다`() {
           // Given
           val userTokenDto = UserTokenDto().apply {
               userSeq = 100
               refreshToken = "validRefreshToken"
               expiredDt = null
           }

           // Mocking Validator to return violations
           val violation: ConstraintViolation<UserTokenDto> = mockk {
               every { propertyPath.toString() } returns "expiredDt"
               every { message } returns "만료시간은 비어있을수 없습니다."
           }
           every { validator.validate(userTokenDto) } returns setOf(violation)

           // When & Then
           val exception = assertThrows<IllegalArgumentException> {
               userService.addUserTokenByUserSeq(userTokenDto)
           }

           assertTrue(exception.message!!.contains("만료시간은 비어있을수 없습니다."))

           // verify
           verify(exactly = 1) { validator.validate(userTokenDto) }
           // No other interactions should occur
           verify { modelMapper wasNot Called }
           verify { userRepository.addUserTokenByUserSeq(any()) wasNot Called }

           confirmVerified(userRepository, modelMapper, validator)
       }

       @org.junit.jupiter.api.Test
       @DisplayName("등록 실패 - 모든 필드가 유효하지 않으면 여러 예외가 발생한다")
       fun `등록 실패 - 모든 필드가 유효하지 않으면 여러 예외가 발생한다`() {
           // Given
           val userTokenDto = UserTokenDto().apply {
               userSeq = -10
               refreshToken = ""
               expiredDt = null
           }

           // Mocking Validator to return multiple violations
           val violation1: ConstraintViolation<UserTokenDto> = mockk {
               every { propertyPath.toString() } returns "userSeq"
               every { message } returns "유저 시퀸스는 양수여야 합니다."
           }
           val violation2: ConstraintViolation<UserTokenDto> = mockk {
               every { propertyPath.toString() } returns "refreshToken"
               every { message } returns "refreshToken은 비어있을수 없습니다."
           }
           val violation3: ConstraintViolation<UserTokenDto> = mockk {
               every { propertyPath.toString() } returns "expiredDt"
               every { message } returns "만료시간은 비어있을수 없습니다."
           }
           every { validator.validate(userTokenDto) } returns setOf(violation1, violation2, violation3)

           // When & Then
           val exception = assertThrows<IllegalArgumentException> {
               userService.addUserTokenByUserSeq(userTokenDto)
           }

           assertTrue(exception.message!!.contains("유저 시퀸스는 양수여야 합니다."))
           assertTrue(exception.message!!.contains("refreshToken은 비어있을수 없습니다."))
           assertTrue(exception.message!!.contains("만료시간은 비어있을수 없습니다."))

           // verify
           verify(exactly = 1) { validator.validate(userTokenDto) }
           // No other interactions should occur
           verify { modelMapper wasNot Called }
           verify { userRepository.addUserTokenByUserSeq(any()) wasNot Called }

           confirmVerified(userRepository, modelMapper, validator)
       }
   }

}