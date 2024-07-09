package com.example.myapp.Controller

import com.example.myapp.Dto.UserDto
import com.example.myapp.Service.UserService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.userDtoToEntity
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


    @Autowired
    fun userController(userService: UserService, jwtUtil: JwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("userJoin")
    fun userJoin(@RequestBody userDto :UserDto) : Boolean {
        val searchUser: Boolean = userService?.searchUser(userDtoToEntity(userDto)) === 0.toLong()


        if(searchUser){
            userService?.userJoin(userDtoToEntity(userDto))
        }

        return  searchUser
    }

    @PostMapping("getFindUserId")
    fun getFindUserId(@RequestBody userDto : UserDto) : String? {
        return userService?.getFindUserId(userDtoToEntity(userDto))
    }


    @PostMapping("userLogin")
    fun userLogin(@RequestBody userDto: UserDto): String? {
        val loggedInUser: UserDto? = userService?.userLogin(userDtoToEntity(userDto))

        return try {
            if (loggedInUser?.userId != null) {
                jwtUtil?.createAccessToken(userDto)
            } else {
                println("User not found or invalid credentials")
                null
            }
        } catch (e: Exception) {
            println("Error generating access token: ${e.message}")
            null
        }
    }









}