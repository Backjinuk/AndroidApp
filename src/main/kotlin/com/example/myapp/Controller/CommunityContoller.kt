package com.example.myapp.Controller

import com.example.myapp.Dto.CommunityDTO
import com.example.myapp.Entity.Community
import com.example.myapp.Service.Community.CommunityService
import com.example.myapp.Service.User.UserService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.commuDtoToEntity
import com.example.myapp.Util.ModelMapperUtil.Companion.commuEntityToDto
import com.example.myapp.Util.ModelMapperUtil.Companion.userDtoToEntity
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/commu/")
class CommunityContoller  @Autowired constructor(
    private var communityService:CommunityService,
    private var userService: UserService,
    private var jwtUtil: JwtUtil
){

    private val log = LoggerFactory.getLogger(JwtUtil::class.java)

    @RequestMapping("addCommunity")
    fun addCommunity(@RequestBody communityDTO: CommunityDTO, request: HttpServletRequest): ResponseEntity<Boolean> {
        // Extract tokens from request headers
        val mutableMap: MutableMap<String, String> = mutableMapOf(
            "AccessToken" to  (request.getHeader("AccessToken")?.toString() ?: ""),
            "RefreshToken" to (request.getHeader("RefreshToken")?.toString() ?: "")
        )

        // Parse claims from the access token
        var userSeq = jwtUtil?.parseClaims(mutableMap)?.get("userSeq")?.toString()?.toLong()

        // Find the User entity by userSeq
        val userDto = userService.getUserInfo(userSeq) ?: return ResponseEntity(false, HttpStatus.BAD_REQUEST)

        println("user = ${userDto.userSeq}")


        // Convert CommunityDTO to Community entity
        val communityEntity = commuDtoToEntity(communityDTO).apply {
            commuWrite = userDtoToEntity(userDto) // Set existing User entity
        }

        println("communityEntity.commuWrite.userSeq = ${communityEntity.commuWrite.userSeq}")
        
        return try {
            // Add community and return appropriate response
            val savedCommunity = communityService.addCommunity(communityEntity)
            ResponseEntity(savedCommunity != null, HttpStatus.OK)
        } catch (e: Exception) {
            log.error("Error adding community", e)
            ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping("getLocationBaseInquery")
    fun getLocationBaseInquery(@RequestBody map: MutableMap<String, Any>) : List<CommunityDTO>{
        println("map.get(\"myPosition\") = ${map.get("myPosition")}")

        val myPosition = map["myPosition"] as? Map<String, Double>

        // 'myposition' Map에서 'latitude', 'longitude', 'radius' 값을 추출합니다.
        val latitude  : Double = myPosition?.get("latitude") ?: throw IllegalArgumentException("Latitude is missing")
        val longitude : Double = myPosition?.get("longitude") ?: throw IllegalArgumentException("Longitude is missing")
        val radius    : Double = myPosition?.get("radius") ?: throw IllegalArgumentException("Radius is missing")

        return communityService.getLocationBaseInquey(latitude, longitude, radius).map { item ->  commuEntityToDto(item) }
    }

}
