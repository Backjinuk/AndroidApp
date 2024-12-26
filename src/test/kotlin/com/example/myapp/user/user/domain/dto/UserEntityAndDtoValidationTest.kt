package com.example.myapp.user.user.domain.dto

import com.example.myapp.user.user.domain.UserRole
import com.example.myapp.user.user.domain.entity.UserEntity
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ConstraintViolation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class UserEntityAndDtoValidationTest {

 private lateinit var validator: Validator

 @BeforeEach
 fun setUp() {
  // Validator 초기화
  validator = Validation.buildDefaultValidatorFactory().validator
 }

 @Test
 fun `이메일이 없으면 벨리데이션 실패`() {
  // given
  val user = UserEntity().apply {
   email = "" // 이메일 없음
   passwd = "validPassword123"
   nickName = "UserNick"
   userRole = UserRole.User
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertEquals(1, violations.size) // 하나의 벨리데이션 오류 예상
  assertTrue(violations.any { it.message == "이메일은 필수 항목입니다." })
 }

 @Test
 fun `비밀번호가 짧으면 벨리데이션 실패`() {
  // given
  val user = UserEntity().apply {
   email = "test@example.com"
   passwd = "short" // 너무 짧은 비밀번호
   nickName = "UserNick"
   userRole = UserRole.User
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertEquals(1, violations.size) // 하나의 벨리데이션 오류 예상
  assertTrue(violations.any { it.message == "비밀번호는 8~20자 사이여야 합니다." })
 }
}