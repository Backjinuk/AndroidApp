package com.example.myapp.Controller

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Service.Community.CommunityService
import com.example.myapp.Service.User.UserService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.commuDtoToEntity
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
class CommunityController  @Autowired constructor(
    private var communityService:CommunityService,
    private var userService: UserService,
    private var jwtUtil: JwtUtil
){

    private val log = LoggerFactory.getLogger(JwtUtil::class.java)

    @RequestMapping("addCommunity")
    fun addCommunity(@RequestBody communityDTO: CommunityDto, request: HttpServletRequest): ResponseEntity<Boolean> {

        // Parse claims from the access token
        var userSeq = jwtUtil.requestToUserSeq(request)

        // Find the User entity by userSeq
        val userDto = userService.getUserInfo(userSeq) ?: return ResponseEntity(false, HttpStatus.BAD_REQUEST)

        println("user = ${userDto.userSeq}")

        // Convert CommunityDTO to Community entity
        val communityEntity = commuDtoToEntity(communityDTO).apply {
            commuWrite = userDtoToEntity(userDto) // Set existing User entity
        }

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
    fun getLocationBaseInquery(@RequestBody map: MutableMap<String, Any>, request: HttpServletRequest) : List<CommunityDto>{
        val myPosition = map["myPosition"] as? Map<String, Double>

        // 'myposition' Map에서 'latitude', 'longitude', 'radius' 값을 추출합니다.
        val latitude  : Double = myPosition?.get("latitude") ?: throw IllegalArgumentException("Latitude is missing")
        val longitude : Double = myPosition["longitude"] ?: throw IllegalArgumentException("Longitude is missing")
        val radius    : Double = myPosition["radius"] ?: throw IllegalArgumentException("Radius is missing")

        val userSeq = jwtUtil.JwtTokenGetUserSeq(mapOf("AccessToken" to request.getHeader("AccessToken")))


        return communityService.getLocationBaseInquey(latitude, longitude, radius, userSeq)
    }

    @RequestMapping("getCommunityInfo")
    fun getCommunityInfo(@RequestBody communityDTO: CommunityDto, request: HttpServletRequest): MutableMap<String, Any?> {

        communityDTO.commuWrite.userSeq = jwtUtil.JwtTokenGetUserSeq(mapOf("AccessToken" to request.getHeader("AccessToken")))

        val map : MutableMap<String, Any> = communityService.getCommunityInfo(communityDTO);

        println("map[\"community\"] = ${map["community"]}")
        println("map[\"applyStatus\"] = ${map["applyStatus"]}")


        return mutableMapOf(
                "community" to map["community"],
                "applyStatus" to map["applyStatus"]
            )


    }







}
