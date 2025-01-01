package com.example.myapp.user.userProfile.service

import com.example.myapp.user.userProfile.domain.dto.SocialMediaPlatFormDto
import com.example.myapp.user.userProfile.domain.dto.UserProfileDto
import com.example.myapp.user.userProfile.domain.entity.SocialMediaPlatFormEntity
import com.example.myapp.user.userProfile.domain.entity.UserProfileEntity
import com.example.myapp.user.userProfile.infra.repository.UserProfileRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.modelmapper.ModelMapper

@ExtendWith(MockKExtension::class)
class UserProfileServiceUnitTest {

    @MockK
    private lateinit var userProfileRepository: UserProfileRepository

    @MockK
    private lateinit var modelMapper: ModelMapper

    @MockK
    private lateinit var validator: Validator

    @InjectMockKs
    private lateinit var userProfileService: UserProfileService

    @Test
    fun `성공적인 등록 - 유효한 UserProfileDto는 저장되어야 한다`() {
        // Given
        val userProfileDto = UserProfileDto().apply {
            userSeq = 100
            fileSeq = 200
            introduction = "안녕하세요, 저는 사용자입니다."
        }

        val userProfileEntity = UserProfileEntity().apply {
            userSeq = userProfileDto.userSeq
            fileSeq = userProfileDto.fileSeq
            introduction = userProfileDto.introduction
            userProfileSeq = 1 // Assuming this is set after saving
        }

        // MockK 설정: 유효성 검사가 통과됨
        every { validator.validate(userProfileDto) } returns emptySet()

        // MockK 설정: ModelMapper가 DTO를 Entity로 변환
        every { modelMapper.map(userProfileDto, UserProfileEntity::class.java) } returns userProfileEntity

        // MockK 설정: 리포지토리가 저장을 수행하고 반환값을 제공함
        every { userProfileRepository.userProfitableSetting(userProfileEntity) } returns userProfileEntity

        // MockK 설정: ModelMapper가 Entity를 DTO로 변환
        every { modelMapper.map(userProfileEntity, UserProfileDto::class.java) } returns userProfileDto

        // When
        val savedProfile = userProfileService.userProfileTableSetting(userProfileDto)

        // Then
        assertNotNull(savedProfile)
        savedProfile?.let { assertEquals(100, it.userSeq) }
        savedProfile?.let { assertEquals(200, it.fileSeq) }
        if (savedProfile != null) {
            assertEquals("안녕하세요, 저는 사용자입니다.", savedProfile.introduction)
        }

        // MockK 검증: validator.validate, modelMapper.map, repository.userProfitableSetting, modelMapper.map 호출 확인
        verify(exactly = 1) { validator.validate(userProfileDto) }
        verify(exactly = 1) { modelMapper.map(userProfileDto, UserProfileEntity::class.java) }
        verify(exactly = 1) { userProfileRepository.userProfitableSetting(userProfileEntity) }
        verify(exactly = 1) { modelMapper.map(userProfileEntity, UserProfileDto::class.java) }

        // 모든 설정된 MockK 호출이 검증되었는지 확인
        confirmVerified(validator, modelMapper, userProfileRepository)
    }


    @Test
    fun `성공적인 등록 - introduction이 빈값이여도 등록이 되어야 한다`() {
        // Given
        val userProfileDto = UserProfileDto().apply {
            userSeq = 100
            fileSeq = 200
            introduction = ""
        }

        val userProfileEntity = UserProfileEntity().apply {
            userSeq = userProfileDto.userSeq
            fileSeq = userProfileDto.fileSeq
            introduction = userProfileDto.introduction
        }

        // MockK 설정: 유효성 검사가 통과됨
        every { validator.validate(userProfileDto) } returns emptySet()

        // MockK 설정: ModelMapper가 DTO를 Entity로 변환
        every { modelMapper.map(userProfileDto, UserProfileEntity::class.java) } returns userProfileEntity

        // MockK 설정: 리포지토리가 저장을 수행하고 반환값을 제공함
        every { userProfileRepository.userProfitableSetting(userProfileEntity) } returns userProfileEntity

        // MockK 설정: ModelMapper가 Entity를 DTO로 변환
        every { modelMapper.map(userProfileEntity, UserProfileDto::class.java) } returns userProfileDto

        // When
        val savedProfile = userProfileService.userProfileTableSetting(userProfileDto)

        // Then
        assertNotNull(savedProfile)
        savedProfile?.let { assertEquals(100, it.userSeq) }
        savedProfile?.let { assertEquals(200, it.fileSeq) }
        assertEquals("", savedProfile?.introduction)

        // MockK 검증: validator.validate, modelMapper.map, repository.userProfitableSetting, modelMapper.map 호출 확인
        verify(exactly = 1) { validator.validate(userProfileDto) }
        verify(exactly = 1) { modelMapper.map(userProfileDto, UserProfileEntity::class.java) }
        verify(exactly = 1) { userProfileRepository.userProfitableSetting(userProfileEntity) }
        verify(exactly = 1) { modelMapper.map(userProfileEntity, UserProfileDto::class.java) }

        // 모든 설정된 MockK 호출이 검증되었는지 확인
        confirmVerified(validator, modelMapper, userProfileRepository)
    }


    @Test
    fun `등록 실패 - 유효하지않은 UserProfileDto는 저장되지 않아야 한다(userSeq)`() {
        // Given
        val userProfileDto = UserProfileDto().apply {
            userSeq = 0
            fileSeq = 200
            introduction = "안녕하세요, 저는 사용자입니다."
        }
        val userProfileEntity = UserProfileEntity().apply {
            userSeq = userProfileDto.userSeq
            fileSeq = userProfileDto.fileSeq
            introduction = userProfileDto.introduction
        }

        every { modelMapper.map(userProfileDto, UserProfileEntity::class.java) } returns userProfileEntity

        //Mockk 설정 유효정 실패
        val validation: ConstraintViolation<UserProfileDto> = mockk {
            every { message } returns "유저 시퀸스는 양수여야 합니다."
            every { propertyPath.toString() } returns "userSeq"
        }

        every { validator.validate(userProfileDto) } returns setOf(validation)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            userProfileService.userProfileTableSetting(userProfileDto)
        }

        assertTrue(exception.message!!.contains("유효성 검증 실패: 유저 시퀸스는 양수여야 합니다."))

        verify(exactly = 1) { validator.validate(userProfileDto) }
        verify(exactly = 0) { userProfileRepository.userProfitableSetting(userProfileEntity) }
    }


    @Test
    fun `등록 실패 - 유효하지 않은 UserProfileDto는 저장되지 않아야 한다(userSeq, fileSeq)`() {
        // Given
        val userProfileDto = UserProfileDto().apply {
            userSeq = 0 // 유효하지 않은 값
            fileSeq = -1 // 유효하지 않은 값
            introduction = "안녕하세요, 저는 사용자입니다."
        }

        // ConstraintViolation Mock 설정 for userSeq
        val violationUserSeq: ConstraintViolation<UserProfileDto> = mockk {
            every { message } returns "유저 시퀸스는 양수여야 합니다."
            every { propertyPath.toString() } returns "userSeq"
        }

        // ConstraintViolation Mock 설정 for fileSeq
        val violationFileSeq: ConstraintViolation<UserProfileDto> = mockk {
            every { message } returns "파일 시퀸스는 0 이상이어야 합니다."
            every { propertyPath.toString() } returns "fileSeq"
        }

        // Validator Mock 설정: 유효성 검사 실패 반환
        every { validator.validate(userProfileDto) } returns setOf(violationUserSeq, violationFileSeq)

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            userProfileService.userProfileTableSetting(userProfileDto)
        }

        // 예외 메시지에 두 개의 검증 실패 메시지가 포함되어 있는지 확인
        assertTrue(exception.message!!.contains("유효성 검증 실패: 유저 시퀸스는 양수여야 합니다."))
        assertTrue(exception.message!!.contains("파일 시퀸스는 0 이상이어야 합니다."))

        // MockK 검증: validator.validate 호출되었고, repository.save와 modelMapper.map은 호출되지 않았는지 확인
        verify(exactly = 1) { validator.validate(userProfileDto) }
        verify(exactly = 0) { userProfileRepository.userProfitableSetting(any()) }
        verify(exactly = 0) { modelMapper.map(any(), any<Class<UserProfileEntity>>()) }
        verify(exactly = 0) { modelMapper.map(any<UserProfileEntity>(), any<Class<UserProfileDto>>()) }

        // 모든 설정된 MockK 호출이 검증되었는지 확인
        confirmVerified(validator, modelMapper, userProfileRepository)
    }


    @Test
    fun `등록 성공 - 유효한 SocialMediaPlatForm은 db에 저장되어야 한다`() {
        // Givne
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = 50
            platFormName = "Github"
            platFormUrl = "https://github/test"
        }


        val socialMediaPlatFormEntity = SocialMediaPlatFormEntity().apply {
            userProfileSeq = socialMediaPlatFormDto.userProfileSeq
            platFormName = socialMediaPlatFormDto.platFormName
            platFormUrl = socialMediaPlatFormDto.platFormUrl
        }

        every { validator.validate(socialMediaPlatFormDto) } returns emptySet()

        every {
            modelMapper.map(
                socialMediaPlatFormDto,
                SocialMediaPlatFormEntity::class.java
            )
        } returns socialMediaPlatFormEntity

        every {
            modelMapper.map(
                socialMediaPlatFormEntity,
                SocialMediaPlatFormDto::class.java
            )
        } returns socialMediaPlatFormDto

        every { userProfileRepository.socialMediaPlatFromByUserProfile(socialMediaPlatFormEntity) } returns socialMediaPlatFormEntity

        //Then
        val savedValue = userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)

        //When
        assertEquals(savedValue.userProfileSeq, socialMediaPlatFormDto.userProfileSeq)
        assertEquals(savedValue.platFormName, socialMediaPlatFormDto.platFormName)
        assertEquals(savedValue.platFormUrl, socialMediaPlatFormDto.platFormUrl)
    }


    @Test
    fun `등록 실패 - 유효하지 않은 userProfileSeq는 db에 저장되지 않아야 한다`() {
        // Givne
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = -1
            platFormName = "Github"
            platFormUrl = "https://github/test"
        }

        val validationUserprofileSeq: ConstraintViolation<SocialMediaPlatFormDto> = mockk {
            every { message } returns "유저 프로필의 시퀸스는 0이상이여야 합니다."
            every { propertyPath.toString() } returns "userProfileSeq"
        }

        every { validator.validate(socialMediaPlatFormDto) } returns setOf(validationUserprofileSeq)

        //When
        val exception = assertThrows<IllegalArgumentException> {
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
        }


        //Then
        assertTrue(exception.message!!.contains("유저 프로필의 시퀸스는 0이상이여야 합니다."))
        verify(exactly = 0) { userProfileRepository.socialMediaPlatFromByUserProfile(any()) }
        verify(exactly = 1) { validator.validate(socialMediaPlatFormDto) }

    }


    @Test
    fun `등록 실패 - 유효하지 않은 platFormName은 db에 저장되지 않아야 한다`() {
        //Given
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = 100
            platFormName = ""
            platFormUrl = "https://github/test"
        }

        val validationPlatFormName: ConstraintViolation<SocialMediaPlatFormDto> = mockk {
            every { message } returns "플랫폼 이름은 비어있을수 없습니다."
            every { propertyPath.toString() } returns "platFormName"
        }

        every { validator.validate(socialMediaPlatFormDto) } returns setOf(validationPlatFormName)

        //When
        val exception = assertThrows<IllegalArgumentException> {
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
        }

        //Then
        assertTrue(exception.message!!.contains("플랫폼 이름은 비어있을수 없습니다."))
        verify(exactly = 0) { userProfileRepository.socialMediaPlatFromByUserProfile(any()) }
        verify(exactly = 1) { validator.validate(socialMediaPlatFormDto) }
    }

   @Test
   fun `등록 실패 - 유효하지 않은 platFormUrl은 db에 저장되지 않아야 한다`() {
       //Given
       val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
           userProfileSeq = 100
           platFormName = "Github"
           platFormUrl = ""
       }

      val validationPlatFormUrl : ConstraintViolation<SocialMediaPlatFormDto>  = mockk {
          every { message } returns "url은 비어있을수 없습니다."
          every { propertyPath.toString() } returns "platFormUrl"
      }

      every { validator.validate(socialMediaPlatFormDto) } returns setOf(validationPlatFormUrl)

      //When
      val exception = assertThrows<IllegalArgumentException> {
          userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
      }

       //Then
       assertTrue(exception.message!!.contains("url은 비어있을수 없습니다."))

       verify(exactly = 0){ userProfileRepository.socialMediaPlatFromByUserProfile(any())}
       verify(exactly = 1){ validator.validate(socialMediaPlatFormDto)  }

   }


    @Test
    fun `등록 실패 - 유효하지 않은 platFormUrl(주소 형식)은 db에 저장되지 않아야 한다`() {
        //Given
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = 100
            platFormName = "Github"
            platFormUrl = "test_url"
        }

        val validationPlatFormUrl : ConstraintViolation<SocialMediaPlatFormDto>  = mockk {
            every { message } returns "유효한 url이여야 합니다."
            every { propertyPath.toString() } returns "platFormUrl"
        }

        every { validator.validate(socialMediaPlatFormDto) } returns setOf(validationPlatFormUrl)

        //When
        val exception = assertThrows<IllegalArgumentException> {
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
        }

        //Then
        assertTrue(exception.message!!.contains("유효한 url이여야 합니다."))

        verify(exactly = 0){ userProfileRepository.socialMediaPlatFromByUserProfile(any())}
        verify(exactly = 1){ validator.validate(socialMediaPlatFormDto)  }

    }


}

