package com.example.myapp.user.useCase

import com.example.myapp.Util.JwtUtil
import com.example.myapp.user.user.domain.dto.UserDto
import com.example.myapp.user.user.domain.dto.UserTokenDto
import com.example.myapp.user.user.service.UserService
import com.example.myapp.user.userProfile.domain.dto.UserProfileDto
import com.example.myapp.user.userProfile.service.UserProfileService
import com.example.myapp.user.userSetting.domain.dto.UserSettingDto
import com.example.myapp.user.userSetting.service.UserSettingService
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserUseCaseInteract(
    private var userService: UserService,
    private var userSettingService: UserSettingService,
    private var userProfileService: UserProfileService,
    private var jwtUtil: JwtUtil,
    private var modelMapper: ModelMapper
) : UserUseCase {

    override fun registerUser(userDto: UserDto): UserDto {
        // 1. 회원 정보 등록
        val registeredUser = userService.registerUser(userDto)

        userProfileService.createDefaultUserProfile(UserProfileDto().apply { userSeq = registeredUser.userSeq })

        userSettingService.createDefaultUserSettings(UserSettingDto().apply { userSeq = registeredUser.userSeq })

        userService.addUserTokenByUserSeq(
            //임시로 refreshToken, exprieDt 지정
            UserTokenDto().apply {
                userSeq = registeredUser.userSeq
                refreshToken = "testToken12345"
                expiredDt = LocalDateTime.now()
            }
        )

        return registeredUser
    }

    override fun updateUserInfoByUser(userDto: UserDto): UserDto {
        return userService.updateUserInfoByUser(userDto)
    }

    override fun login(userDto: UserDto): UserDto {
        val userInfo = userService.getFindUserInfoByEmailAndPassword(userDto.email, userDto.passwd)

        // jwt 발급후 db애 저장
        val token = jwtUtil.createRefreshToken(userInfo)

        userService.addUserTokenByUserSeq(
            UserTokenDto().apply {
                userSeq = userInfo.userSeq
                refreshToken = token
                expiredDt = jwtUtil.getExpireDt(token)
            }
        )



        return userInfo;

    }
}