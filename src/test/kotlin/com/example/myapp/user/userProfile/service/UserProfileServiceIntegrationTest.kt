package com.example.myapp.user.userProfile.service

import com.example.myapp.user.userProfile.domain.dto.UserProfileDto
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

    @Test
    fun `등록 성공 - 유효한 userProfileDto의 값은 db에 등록됨`() {
        //given
        val userProfileDto = UserProfileDto().apply {
            userSeq = 100
            fileSeq = 200
            introduction = "안녕하세요, 저는 사용자입니다."
        } //실제로는 없는 회원

        //when
        val savedUserProfile = userProfileService.userProfitableSetting(userProfileDto);

        //than
        assertNotNull(savedUserProfile)
        assertEquals(savedUserProfile.userSeq, userProfileDto.userSeq)
        assertEquals(savedUserProfile.fileSeq, userProfileDto.fileSeq)
        assertEquals(savedUserProfile.introduction, userProfileDto.introduction)
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
            userProfileService.userProfitableSetting(userProfileDto)
        }

        // 예외 메시지 출력 (디버깅 용도)
        println("Exception message: ${exception.message}")

        // 예외 메시지에 두 개의 검증 실패 메시지가 포함되어 있는지 확인
        assertTrue(exception.message!!.contains("유저의 시퀸스는 양수여야 합니다."))
        assertTrue(exception.message!!.contains("파일 시퀸스는 0 이상이여야 합니다."))

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
        val savedProfile = userProfileService.userProfitableSetting(userProfileDto)

        // Then
        assertNotNull(savedProfile)
        assertTrue(savedProfile.userProfileSeq!! > 0)
        assertEquals(userProfileDto.userSeq, savedProfile.userSeq)
        assertEquals(userProfileDto.fileSeq, savedProfile.fileSeq)
        assertEquals(userProfileDto.introduction, savedProfile.introduction)

    }

}