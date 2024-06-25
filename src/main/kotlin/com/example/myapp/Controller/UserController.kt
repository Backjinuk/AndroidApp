package com.example.myapp.Controller

import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.User
import com.example.myapp.Service.UserService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/user/")
class UserController {

    private var userService : UserService? = null;
    private var modelMapper = ModelMapper();

    @Autowired
    fun userController(userService: UserService){
        this.userService = userService;
    }


    @RequestMapping("userJoin")
    fun userJoin(@RequestBody userDto :UserDto){
        userService?.userJoin(userDtoToEntity(userDto))
    }

    @RequestMapping("getfindUserId")
    fun getfindUserId(@RequestBody userDto : UserDto) : String? {
        return userService?.getFindUserId(userDtoToEntity(userDto))
    }

    fun userDtoToEntity(userDto: UserDto) : User{
        return modelMapper.map(userDto, User::class.java);
    }
}