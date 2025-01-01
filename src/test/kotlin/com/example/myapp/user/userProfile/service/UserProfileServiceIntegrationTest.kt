package com.example.myapp.user.userProfile.service

import com.example.myapp.user.userProfile.domain.dto.SocialMediaPlatFormDto
import com.example.myapp.user.userProfile.domain.dto.UserProfileDto
import com.example.myapp.user.userProfile.infra.repository.UserProfileRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@Transactional
class UserProfileServiceIntegrationTest @Autowired constructor(
    private val userProfileService: UserProfileService
) {

    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository

    @Test
    fun `등록 성공 - 유효한 userProfileDto의 값은 db에 등록됨`() {
        //given
        val userProfileDto = UserProfileDto().apply {
            userSeq = 100
            fileSeq = 200
            introduction = "안녕하세요, 저는 사용자입니다."
        } //실제로는 없는 회원

        //when
        val savedUserProfile = userProfileService.userProfileTableSetting(userProfileDto);

        //than
        assertNotNull(savedUserProfile)
        assertEquals(savedUserProfile.userSeq, userProfileDto.userSeq)
        assertEquals(savedUserProfile.fileSeq, userProfileDto.fileSeq)
        assertEquals(savedUserProfile.introduction, userProfileDto.introduction)
    }

    @Test
    fun `성공적인 등록 - introduction이 빈 값이어도 저장되어야 한다`() {
        // Given
        val userProfileDto = UserProfileDto().apply {
            userSeq = 100
            fileSeq = 200
            introduction = "" // 빈 값
        }

        // When
        val savedProfile = userProfileService.userProfileTableSetting(userProfileDto)

        // Then
        assertNotNull(savedProfile)
        assertTrue(savedProfile.userProfileSeq!! > 0)
        assertEquals(userProfileDto.userSeq, savedProfile.userSeq)
        assertEquals(userProfileDto.fileSeq, savedProfile.fileSeq)
        assertEquals(userProfileDto.introduction, savedProfile.introduction)

    }


    @Test
    fun `등록 실패 - 유효하지 않은 UserProfileDto는 저장되지 않아야 한다(userSeq, fileSeq)`() {
        // Given
        val userProfileDto = UserProfileDto().apply {
            userSeq = 0 // 유효하지 않은 값
            fileSeq = -1 // 유효하지 않은 값
            introduction = "안녕하세요, 저는 사용자입니다."
        }

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            userProfileService.userProfileTableSetting(userProfileDto)
        }

        // 예외 메시지 출력 (디버깅 용도)
        println("Exception message: ${exception.message}")

        // 예외 메시지에 두 개의 검증 실패 메시지가 포함되어 있는지 확인
        assertTrue(exception.message!!.contains("유저의 시퀸스는 양수여야 합니다."))
        assertTrue(exception.message!!.contains("파일 시퀸스는 0 이상이여야 합니다."))

    }

    //===========================================================================

    @Test
    fun `등록 성공 - 유효한 socialMediaPlatFormDto는 db에 저장되어야 한다`() {
        // Given
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = 100
            platFormName = "Github"
            platFormUrl = "https://github.com/testGit"
        }

        // When
        val returnValue = userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)

        //Then
        assertEquals(returnValue.userProfileSeq, socialMediaPlatFormDto.userProfileSeq)
        assertEquals(returnValue.platFormName, socialMediaPlatFormDto.platFormName)
        assertEquals(returnValue.platFormUrl, socialMediaPlatFormDto.platFormUrl)

    }


    @Test
    fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(userProfileSeq)는 db에 저장되지 않아야 한다`() {
        // Given
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = -1
            platFormName = "Github"
            platFormUrl = "https://github.com/testGit"
        }

        // When && Then
        val exceptions = assertThrows<IllegalArgumentException> {
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
        }

        assertTrue(exceptions.message!!.contains("유저 프로필의 시퀸스는 0이상이여야 합니다."))
    }


    @Test
    fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormName)는 db에 저장되지 않아야 한다`() {
        // Given
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = 100
            platFormName = ""
            platFormUrl = "https://github.com/testGit"
        }

        // When && Then
        val exceptions = assertThrows<IllegalArgumentException> {
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
        }

        assertTrue(exceptions.message!!.contains("플랫폼 이름은 비어있을수 없습니다."))
    }


    @Test
    fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormUrl)는 db에 저장되지 않아야 한다`() {
        // Given
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = 100
            platFormName = "Github"
            platFormUrl = ""
        }

        // When && Then
        val exceptions = assertThrows<IllegalArgumentException> {
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
        }

        assertTrue(exceptions.message!!.contains("url은 비어있을수 없습니다."))
    }


    @Test
    fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormUrl - 주소 형식 잘못)는 db에 저장되지 않아야 한다`() {
        // Given
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = 100
            platFormName = "Github"
            platFormUrl = "test-url"
        }

        // When && Then
        val exceptions = assertThrows<IllegalArgumentException> {
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
        }

        assertTrue(exceptions.message!!.contains("유효한 url이여야 합니다."))
    }


    @Test
    fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(userProfileSeq, platFormName, platFormUrl)는 db에 저장되지 않아야 한다`() {
        // Given
        val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
            userProfileSeq = -1
            platFormName = ""
            platFormUrl = ""
        }

        // When && Then
        val exceptions = assertThrows<IllegalArgumentException> {
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
        }

        assertTrue(exceptions.message!!.contains("유저 프로필의 시퀸스는 0이상이여야 합니다."))
        assertTrue(exceptions.message!!.contains("플랫폼 이름은 비어있을수 없습니다."))
        assertTrue(exceptions.message!!.contains("url은 비어있을수 없습니다."))
    }


}