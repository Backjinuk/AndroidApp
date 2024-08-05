package com.example.myapp.Controller

import com.example.myapp.Dto.UserDto
import com.example.myapp.Service.User.UserService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.userDtoToEntity
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/user/")
class UserController {

    private var userService : UserService? = null;
    private var jwtUtil : JwtUtil ?= null;
    private val log = LoggerFactory.getLogger(JwtUtil::class.java)

    @Autowired
    fun userController(userService: UserService, jwtUtil: JwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("userJoin")
    fun userJoin(@RequestBody userDto :UserDto) : MutableMap<String, String>{

        val mutableMap: MutableMap<String, String> = mutableMapOf()
        var loggedInUser:UserDto = UserDto();
        var accessToken:String =  "";
        var refreshToken:String = "";

        val searchUser: Boolean = userService?.searchUser(userDtoToEntity(userDto)) === 0.toLong()

        if(searchUser){
            userService?.userJoin(userDtoToEntity(userDto))
        }

        loggedInUser = userService?.userLogin(userDtoToEntity(userDto))!!;
        accessToken  = loggedInUser.let { jwtUtil?.createAccessToken(it).toString() }.toString();
        refreshToken = loggedInUser.let { jwtUtil?.createRefreshToken(it).toString() }.toString();

        mutableMap["AccessToken"] = accessToken
        mutableMap["RefreshToken"] = refreshToken
        mutableMap["searchUser"] = searchUser.toString()

        userService?.insertRefreshToken(refreshToken, mutableMap["NewRefreshToken"].toString(), loggedInUser?.userSeq);

        return  mutableMap;
    }

    @PostMapping("userLogin")
    fun userLogin(@RequestBody userDto: UserDto, request: HttpServletRequest): MutableMap<String, String>? {
        val mutableMap: MutableMap<String, String> = mutableMapOf(
            "AccessToken" to  (request.getHeader("AccessToken")?.toString() ?: ""),
            "RefreshToken" to (request.getHeader("RefreshToken")?.toString() ?: ""),
            "NewRefreshToken" to (request.getHeader("RefreshToken")?.toString() ?: "")
        )

        var loggedInUser: UserDto = UserDto()

        // JWT 없으면 새로 발급
        if (mutableMap["AccessToken"].isNullOrEmpty() && mutableMap["RefreshToken"].isNullOrEmpty()) {
            loggedInUser = userService?.userLogin(userDtoToEntity(userDto))!!
            mutableMap["AccessToken"] = loggedInUser.let { jwtUtil?.createAccessToken(it).toString() }.toString()
            mutableMap["RefreshToken"] = loggedInUser.let { jwtUtil?.createRefreshToken(it).toString() }.toString()
        }

        // JWT가 있으면 JWT 검증
        if (!mutableMap["AccessToken"].isNullOrEmpty() && !mutableMap["RefreshToken"].isNullOrEmpty()) {
            val accessTokenParse = jwtUtil?.parseClaims(mutableMap)

            if (accessTokenParse == null) {
                val tokenMap: Map<String, String>? = jwtUtil?.refreshAccessToken(mutableMap)
                mutableMap["AccessToken"] = tokenMap?.get("accessToken").toString()
                mutableMap["NewRefreshToken"] = tokenMap?.get("refreshToken").toString() // Update NewRefreshToken too
            }
        }

        // JWT가 없으면 insert, 있으면 Update
        userService?.insertRefreshToken(mutableMap["RefreshToken"].toString(), mutableMap["NewRefreshToken"].toString(),loggedInUser.userSeq);

        return try {
            if (loggedInUser.userId != null) {
                mutableMap
            } else {
                println("User not found or invalid credentials")
                null
            }
        } catch (e: Exception) {
            println("Error generating access token: ${e.message}")
            null
        }
    }



    @PostMapping("getFindUserId")
    fun getFindUserId(@RequestBody userDto : UserDto) : String? {
        return userService?.getFindUserId(userDtoToEntity(userDto))
    }

    @PostMapping("JwtTokenGetUserSeq")
    fun JwtTokenGetUserSeq(@RequestBody map: Map<String, Any>) : MutableMap<String, Any?>?{

        var newToken : String ?= "";
        var userSeq = jwtUtil?.JwtTokenGetUserSeq(map)
        var userDto :UserDto ?= UserDto();
        var mutableMap : MutableMap<String,Any?> = mutableMapOf() // map 초기화

        // userSeq = 0 이면 토큰 만료로 판단
        if(userSeq!! <= 0L){
            log.info("JWT TOKEN이 만료되었습니다.")
            newToken = jwtUtil!!.refreshAccessToken(map).get("accessToken").toString()
            userSeq = jwtUtil?.JwtTokenGetUserSeq(mapOf("AccessToken" to newToken))
            mutableMap["AccessToken"] = newToken // map에 userDto 추가
        }

        // 재발급 받은 토큰으로 userInfo 조회
        if(userSeq!! > 0L){
            userDto = userService?.getUserInfo(userSeq)
            mutableMap["userDto"] = userDto // map에 userDto 추가
        }

        return try {
            mutableMap
        } catch (e:Exception){
            println("Invalid refresh token: ${e.message}")
            e.printStackTrace()
            null
        }

    }

}