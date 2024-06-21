package com.example.myapp.Controller

import com.example.myapp.Dto.UserDto
import com.example.myapp.Service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/user/")
class UserController {

    private var userService : UserService? = null;

    @Autowired
    fun userController(userService: UserService){
        this.userService = userService;
    }

    @RequestMapping("info")
    fun getUserInfo(@RequestBody userDto: UserDto?): String {
        val userId : String = userDto?.userName.toString();
        return "아이디 : $userId"
    }


}