package com.example.myapp.Util

import com.example.myapp.Dto.CommunityDTO
import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.Community
import com.example.myapp.Entity.User
import org.modelmapper.ModelMapper

class ModelMapperUtil {

    companion object {
        private val modelMapper = ModelMapper()

        /**
         * userDto to Entity convert
         * @param userDto
         * @return User.class
         */

        fun userDtoToEntity(userDto: UserDto): User {
            return modelMapper.map(userDto, User::class.java)
        }

        fun commuDtoToEntity(communityDTO: CommunityDTO): Community {
            return modelMapper.map(communityDTO, Community::class.java)
        }

    }
}