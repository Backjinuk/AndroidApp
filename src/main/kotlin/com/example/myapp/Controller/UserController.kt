package com.example.myapp.Controller

import com.example.myapp.Dto.UserDto
import com.example.myapp.Service.UserService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.userDtoToEntity
import io.jsonwebtoken.ExpiredJwtException
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

        userService?.insertRefreshToken(refreshToken, loggedInUser?.userSeq);

        return  mutableMap;
    }

    @PostMapping("userLogin")
    fun userLogin(@RequestBody userDto: UserDto, request:HttpServletRequest): MutableMap<String, String>? {

        val mutableMap: MutableMap<String, String> = mutableMapOf()
        var accessToken = request.getHeader("AccessToken")?.toString() ?: ""
        var refreshToken = request.getHeader("RefreshToken")?.toString() ?: ""


        var loggedInUser:UserDto = UserDto();

        // JWT 없으면 새로 발급
        if(accessToken.equals("") and accessToken.equals("")){
            loggedInUser = userService?.userLogin(userDtoToEntity(userDto))!!;
            accessToken  = loggedInUser.let { jwtUtil?.createAccessToken(it).toString() }.toString();
            refreshToken = loggedInUser.let { jwtUtil?.createRefreshToken(it).toString() }.toString();
        }

        mutableMap["AccessToken"] = accessToken
        mutableMap["RefreshToken"] = refreshToken

        //JWT가 있으면 JWT검증
        if (!accessToken.equals("") and !accessToken.equals("")){
            var accessTokenParse = jwtUtil?.parseClaims(mutableMap);

            val tokenMap: Map<String, String>? = jwtUtil?.refreshAccessToken(mutableMap);
            mutableMap["RefreshToken"] = tokenMap?.get("refreshToken").toString()

            //accessTokenParse 만료시 refreshToken으로 재발급, refreshToken도 재발급
            if(accessTokenParse == null){
                mutableMap["AccessToken"] = tokenMap?.get("accessToken").toString()
            }
        }

        //JWT가 없으면 insert 있으면 Update
        userService?.insertRefreshToken(refreshToken, loggedInUser?.userSeq);

        return try {
            if (loggedInUser?.userId != null) {
                mutableMap;
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