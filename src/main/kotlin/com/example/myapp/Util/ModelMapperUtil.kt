package com.example.myapp.Util

import com.example.myapp.Dto.CommunityDto
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

        fun commuDtoToEntity(communityDTO: CommunityDto): Community {
            return modelMapper.map(communityDTO, Community::class.java)
        }

        fun commuEntityToDto(community: Community):CommunityDto{
            return modelMapper.map(community, CommunityDto::class.java)
        }

        fun commuEntityToDto(community: Community, status: Char?): CommunityDto {
            // Community 엔티티를 CommunityDto로 매핑
            val communityDto = modelMapper.map(community, CommunityDto::class.java)

            // applyStatus를 DTO에 설정
            communityDto.applyStatus = status

            return communityDto
        }

    }
}