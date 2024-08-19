package com.example.myapp.Util

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Dto.SubscribeDto
import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.Community
import com.example.myapp.Entity.Subscribe
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

        fun subscribeEntityToDto(subscribe: Subscribe):SubscribeDto{
            return modelMapper.map(subscribe, SubscribeDto::class.java)
        }

        fun subscribeDtoToEntity(subscribeDto: SubscribeDto):Subscribe{
            return modelMapper.map(subscribeDto, Subscribe::class.java)
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