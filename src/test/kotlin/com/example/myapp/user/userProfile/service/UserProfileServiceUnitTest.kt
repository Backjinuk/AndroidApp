package com.example.myapp.user.userProfile.service

import com.example.myapp.Util.ValidatorUtil
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
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
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
    private lateinit var validatorUtil: ValidatorUtil

    @InjectMockKs
    private lateinit var userProfileService: UserProfileService

    @Nested
    @DisplayName("createDefaultUserProfile 메서드 테스트")
    inner class CreateDefaultUserProfileTests {

        @Test
        @DisplayName("성공적인 등록 - 유효한 UserProfileDto는 저장되어야 한다")
        fun `should save valid UserProfileDto successfully`() {
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
            justRun { validatorUtil.validator(userProfileDto) }

            // MockK 설정: ModelMapper가 DTO를 Entity로 변환
            every { modelMapper.map(userProfileDto, UserProfileEntity::class.java) } returns userProfileEntity

            // MockK 설정: 리포지토리가 저장을 수행하고 반환값을 제공함
            every { userProfileRepository.userProfitableSetting(userProfileEntity) } returns userProfileEntity

            // MockK 설정: ModelMapper가 Entity를 DTO로 변환
            every { modelMapper.map(userProfileEntity, UserProfileDto::class.java) } returns userProfileDto

            // When
            val savedProfile = userProfileService.createDefaultUserProfile(userProfileDto)

            // Then
            assertNotNull(savedProfile)
            assertEquals(100, savedProfile!!.userSeq, "userSeq가 일치해야 합니다.")
            assertEquals(200, savedProfile.fileSeq, "fileSeq가 일치해야 합니다.")
            assertEquals("안녕하세요, 저는 사용자입니다.", savedProfile.introduction, "introduction이 일치해야 합니다.")

            // MockK 검증: validator.validator(), modelMapper.map, repository.userProfitableSetting, modelMapper.map 호출 확인
            verify(exactly = 1) { validatorUtil.validator(userProfileDto) }
            verify(exactly = 1) { modelMapper.map(userProfileDto, UserProfileEntity::class.java) }
            verify(exactly = 1) { userProfileRepository.userProfitableSetting(userProfileEntity) }
            verify(exactly = 1) { modelMapper.map(userProfileEntity, UserProfileDto::class.java) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }

        @Test
        @DisplayName("성공적인 등록 - introduction이 빈값이여도 등록이 되어야 한다")
        fun `should save UserProfileDto with empty introduction successfully`() {
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
            justRun { validatorUtil.validator(userProfileDto) }

            // MockK 설정: ModelMapper가 DTO를 Entity로 변환
            every { modelMapper.map(userProfileDto, UserProfileEntity::class.java) } returns userProfileEntity

            // MockK 설정: 리포지토리가 저장을 수행하고 반환값을 제공함
            every { userProfileRepository.userProfitableSetting(userProfileEntity) } returns userProfileEntity

            // MockK 설정: ModelMapper가 Entity를 DTO로 변환
            every { modelMapper.map(userProfileEntity, UserProfileDto::class.java) } returns userProfileDto

            // When
            val savedProfile = userProfileService.createDefaultUserProfile(userProfileDto)

            // Then
            assertNotNull(savedProfile)
            assertEquals(100, savedProfile!!.userSeq, "userSeq가 일치해야 합니다.")
            assertEquals(200, savedProfile.fileSeq, "fileSeq가 일치해야 합니다.")
            assertEquals("", savedProfile.introduction, "introduction이 빈값이어야 합니다.")

            // MockK 검증: validator.validator(), modelMapper.map, repository.userProfitableSetting, modelMapper.map 호출 확인
            verify(exactly = 1) { validatorUtil.validator(userProfileDto) }
            verify(exactly = 1) { modelMapper.map(userProfileDto, UserProfileEntity::class.java) }
            verify(exactly = 1) { userProfileRepository.userProfitableSetting(userProfileEntity) }
            verify(exactly = 1) { modelMapper.map(userProfileEntity, UserProfileDto::class.java) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }

        @Test
        @DisplayName("등록 실패 - 유효하지않은 UserProfileDto는 저장되지 않아야 한다(userSeq)")
        fun `should not save invalid UserProfileDto with invalid userSeq`() {
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

            // Mocking ModelMapper to map DTO to Entity
            every { modelMapper.map(userProfileDto, UserProfileEntity::class.java) } returns userProfileEntity

            // ValidatorUtil의 예외 메시지를 기대하는 형식으로 수정
            every { validatorUtil.validator(userProfileDto) } throws IllegalArgumentException("유효성 검증 실패: 유저 시퀸스는 양수여야 합니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userProfileService.createDefaultUserProfile(userProfileDto)
            }

            // 예외 메시지에 검증 실패 메시지가 포함되어 있는지 확인
            assertTrue(
                exception.message!!.contains("유효성 검증 실패: 유저 시퀸스는 양수여야 합니다."),
                "예외 메시지에 '유효성 검증 실패: 유저 시퀸스는 양수여야 합니다.'가 포함되어야 합니다."
            )

            // MockK 검증: validator.validator() 호출되었고, repository.save와 modelMapper.map은 호출되지 않았는지 확인
            verify(exactly = 1) { validatorUtil.validator(userProfileDto) }
            verify(exactly = 0) { userProfileRepository.userProfitableSetting(userProfileEntity) }
            verify(exactly = 0) { modelMapper.map(any(), any<Class<UserProfileEntity>>()) }
            verify(exactly = 0) { modelMapper.map(any<UserProfileEntity>(), any<Class<UserProfileDto>>()) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }

        @Test
        @DisplayName("등록 실패 - 유효하지 않은 UserProfileDto는 저장되지 않아야 한다(userSeq, fileSeq)")
        fun `should not save invalid UserProfileDto with invalid userSeq and fileSeq`() {
            // Given
            val userProfileDto = UserProfileDto().apply {
                userSeq = 0 // 유효하지 않은 값
                fileSeq = -1 // 유효하지 않은 값
                introduction = "안녕하세요, 저는 사용자입니다."
            }

            // Validator Mock 설정: 유효성 검사 실패 시 예외 던지기
            every { validatorUtil.validator(userProfileDto) } throws IllegalArgumentException("유저 시퀸스는 양수여야 합니다., 파일 시퀸스는 0 이상이어야 합니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userProfileService.createDefaultUserProfile(userProfileDto)
            }

            // 예외 메시지에 두 개의 검증 실패 메시지가 포함되어 있는지 확인
            assertTrue(
                exception.message!!.contains("유저 시퀸스는 양수여야 합니다."),
                "예외 메시지에 '유저 시퀸스는 양수여야 합니다.'가 포함되어야 합니다."
            )
            assertTrue(
                exception.message!!.contains("파일 시퀸스는 0 이상이어야 합니다."),
                "예외 메시지에 '파일 시퀸스는 0 이상이어야 합니다.'가 포함되어야 합니다."
            )

            // MockK 검증: validator.validator() 호출되었고, repository.save와 modelMapper.map은 호출되지 않았는지 확인
            verify(exactly = 1) { validatorUtil.validator(userProfileDto) }
            verify(exactly = 0) { userProfileRepository.userProfitableSetting(any()) }
            verify(exactly = 0) { modelMapper.map(any(), any<Class<UserProfileEntity>>()) }
            verify(exactly = 0) { modelMapper.map(any<UserProfileEntity>(), any<Class<UserProfileDto>>()) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }
    }

    @Nested
    @DisplayName("socialMediaPlatFromByUserProfile 메서드 테스트")
    inner class SocialMediaPlatFromByUserProfileTests {

        @Test
        @DisplayName("등록 성공 - 유효한 SocialMediaPlatForm은 db에 저장되어야 한다")
        fun `should save valid SocialMediaPlatForm successfully`() {
            // Given
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

            // MockK 설정: 유효성 검사가 통과됨
            justRun { validatorUtil.validator(socialMediaPlatFormDto) }

            // MockK 설정: ModelMapper가 DTO를 Entity로 변환
            every {
                modelMapper.map(
                    socialMediaPlatFormDto,
                    SocialMediaPlatFormEntity::class.java
                )
            } returns socialMediaPlatFormEntity

            // MockK 설정: 리포지토리가 저장을 수행하고 반환값을 제공함
            every { userProfileRepository.socialMediaPlatFromByUserProfile(socialMediaPlatFormEntity) } returns socialMediaPlatFormEntity

            // MockK 설정: ModelMapper가 Entity를 DTO로 변환
            every {
                modelMapper.map(
                    socialMediaPlatFormEntity,
                    SocialMediaPlatFormDto::class.java
                )
            } returns socialMediaPlatFormDto

            // When
            val savedValue = userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)

            // Then
            assertNotNull(savedValue)
            assertEquals(50, savedValue.userProfileSeq, "userProfileSeq가 일치해야 합니다.")
            assertEquals("Github", savedValue.platFormName, "platFormName이 일치해야 합니다.")
            assertEquals("https://github/test", savedValue.platFormUrl, "platFormUrl이 일치해야 합니다.")

            // MockK 검증: validator.validator(), modelMapper.map, repository.socialMediaPlatFromByUserProfile, modelMapper.map 호출 확인
            verify(exactly = 1) { validatorUtil.validator(socialMediaPlatFormDto) }
            verify(exactly = 1) { modelMapper.map(socialMediaPlatFormDto, SocialMediaPlatFormEntity::class.java) }
            verify(exactly = 1) { userProfileRepository.socialMediaPlatFromByUserProfile(socialMediaPlatFormEntity) }
            verify(exactly = 1) { modelMapper.map(socialMediaPlatFormEntity, SocialMediaPlatFormDto::class.java) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }

        @Test
        @DisplayName("등록 실패 - 유효하지 않은 userProfileSeq는 db에 저장되지 않아야 한다")
        fun `should not save SocialMediaPlatForm with invalid userProfileSeq`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = -1
                platFormName = "Github"
                platFormUrl = "https://github/test"
            }

            // Mocking ValidatorUtil to throw exception
            every { validatorUtil.validator(socialMediaPlatFormDto) } throws IllegalArgumentException("유저 프로필의 시퀸스는 0이상이여야 합니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            // Then
            assertTrue(
                exception.message!!.contains("유저 프로필의 시퀸스는 0이상이여야 합니다."),
                "예외 메시지에 '유저 프로필의 시퀸스는 0이상이여야 합니다.'가 포함되어야 합니다."
            )
            verify(exactly = 0) { userProfileRepository.socialMediaPlatFromByUserProfile(any()) }
            verify(exactly = 1) { validatorUtil.validator(socialMediaPlatFormDto) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }

        @Test
        @DisplayName("등록 실패 - 유효하지 않은 platFormName은 db에 저장되지 않아야 한다")
        fun `should not save SocialMediaPlatForm with empty platFormName`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = ""
                platFormUrl = "https://github/test"
            }

            // Mocking ValidatorUtil to throw exception
            every { validatorUtil.validator(socialMediaPlatFormDto) } throws IllegalArgumentException("플랫폼 이름은 비어있을수 없습니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            // Then
            assertTrue(
                exception.message!!.contains("플랫폼 이름은 비어있을수 없습니다."),
                "예외 메시지에 '플랫폼 이름은 비어있을수 없습니다.'가 포함되어야 합니다."
            )
            verify(exactly = 0) { userProfileRepository.socialMediaPlatFromByUserProfile(any()) }
            verify(exactly = 1) { validatorUtil.validator(socialMediaPlatFormDto) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }

        @Test
        @DisplayName("등록 실패 - 유효하지 않은 platFormUrl은 db에 저장되지 않아야 한다")
        fun `should not save SocialMediaPlatForm with empty platFormUrl`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = ""
            }

            // Mocking ValidatorUtil to throw exception
            every { validatorUtil.validator(socialMediaPlatFormDto) } throws IllegalArgumentException("url은 비어있을수 없습니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            // Then
            assertTrue(
                exception.message!!.contains("url은 비어있을수 없습니다."),
                "예외 메시지에 'url은 비어있을수 없습니다.'가 포함되어야 합니다."
            )
            verify(exactly = 0) { userProfileRepository.socialMediaPlatFromByUserProfile(any()) }
            verify(exactly = 1) { validatorUtil.validator(socialMediaPlatFormDto) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }

        @Test
        @DisplayName("등록 실패 - 유효하지 않은 platFormUrl(주소 형식)은 db에 저장되지 않아야 한다")
        fun `should not save SocialMediaPlatForm with invalid platFormUrl format`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = "test_url"
            }

            // Mocking ValidatorUtil to throw exception
            every { validatorUtil.validator(socialMediaPlatFormDto) } throws IllegalArgumentException("유효한 url이여야 합니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            // Then
            assertTrue(
                exception.message!!.contains("유효한 url이여야 합니다."),
                "예외 메시지에 '유효한 url이여야 합니다.'가 포함되어야 합니다."
            )
            verify(exactly = 0) { userProfileRepository.socialMediaPlatFromByUserProfile(any()) }
            verify(exactly = 1) { validatorUtil.validator(socialMediaPlatFormDto) }

            // 모든 설정된 MockK 호출이 검증되었는지 확인
            confirmVerified(validatorUtil, modelMapper, userProfileRepository)
        }
    }
}
