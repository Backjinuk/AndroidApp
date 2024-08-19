package com.example.myapp.Util

import com.example.myapp.Dto.UserDto
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.time.ZonedDateTime
import java.util.*

@Slf4j
@Component
class JwtUtil(
    @Value("\${jwt.secret}") secretKey: String,
    @Value("\${jwt.expiration_time}") private val accessTokenExpTime: Long,
    @Value("\${jwt.refresh_time}") private val refreshTokenExpTime: Long) {

    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    private val log = LoggerFactory.getLogger(JwtUtil::class.java)

    /**
     * Access Token 생성
     * @param userDto : UserDto
     * @return Access Token String
     */
    fun createAccessToken(userDto: UserDto): String {
        return createToken(userDto, accessTokenExpTime)
    }

    /**
     * Refresh Token 생성
     * @param userDto : UserDto
     * @return Refresh Token String
     */
    fun createRefreshToken(userDto: UserDto): String {
        return createToken(userDto, refreshTokenExpTime)
    }

    /**
     * JWT생성
     * @param userDto : UserDto
     * @param expiretime : Long
     * @return JWT String : String
     */
    fun createToken(userDto: UserDto, accessTokenExpTime: Long): String {
        val claims: Claims = Jwts.claims().also {
            it["userSeq"] = userDto.userSeq
            it["email"] = userDto.email
            it["userId"] = userDto.userId
            it["usertype"] = userDto.userType
        }

        val now: ZonedDateTime = ZonedDateTime.now()
        val tokenValidity: ZonedDateTime = now.plusSeconds(accessTokenExpTime)

        return try {
            val jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
                .replace("\\s+".toRegex(), "")

            // Bearer 토큰 형식으로 반환
            "Bearer $jwt".also {
                println("Token generated successfully: $it")
            }

        } catch (e: Exception) {
            println("Error creating token: ${e.message}")
            e.printStackTrace()
            ""
        }
    }

    /**
     * JWT Claims
     * @param token : String
     * @return Claims : Claims
     */
    fun parseClaims(token: Map<String, Any>): Claims? {
        try {
            // JwtToken에서 JWT를 추출하고 처리
            val accessToken = token["AccessToken"]?.toString()?.replace("Bearer ", "")?.replace(" ", "")
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .body
            // Access Token이 유효하면 그대로 반환
            return claims
        } catch (e: ExpiredJwtException) {
            println("Expired JWT: ${e.message}")
            return null // 만료된 경우 null 반환
        } catch (e: MalformedJwtException) {
            println("Malformed JWT: ${e.message}")
            e.printStackTrace()
            throw e
        } catch (e: Exception) {
            println("Error parsing JWT: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Token에서 User ID 추출
     * @param token : String
     * @return UserSeq :Long
     */
    fun JwtTokenGetUserSeq(token: Map<String, Any>): Long {
        val userSeqInt = parseClaims(token)?.get("userSeq", Integer::class.java)
        return userSeqInt?.toLong() ?: 0L
    }

    /**
     * 토큰 기한 만료시 ReFreshToken 기준으로 Token재발급
     * @param Map<String, Any>
     * @return Token : String
    */
    fun refreshAccessToken(token: Map<String, Any>) : Map<String,String> {
        val refreshToken = token["RefreshToken"].toString().replace("Bearer ", "").replace(" ", "")
        return try {
            log.info("TOKEN ReFresh 실행중...")
            val claims : Claims  = Jwts.parserBuilder()
                .setSigningKey(key )
                .build()
                .parseClaimsJws(refreshToken)
                .body

            // UserDto에 값을 할당
            val userDto = UserDto().apply {
                userSeq = claims["userSeq"]?.toString()?.toLong()
                email = claims["email"] as? String
            }

            log.info("TOKEN ReFresh 완료...")
            val newAccessToken =  createAccessToken(userDto)
            val newRefreshToken =  createRefreshToken(userDto)

            mapOf(
                "accessToken" to newAccessToken,
                "refreshToken" to newRefreshToken
            )


        }catch (e:Exception){
            println("Invalid refresh token: ${e.message}")
            e.printStackTrace()

            mapOf(
                "accessToken" to "",
                "refreshToken" to ""
            )
        }
    }


    fun  requestToUserSeq(request: HttpServletRequest) : Long?{
        val mutableMap: MutableMap<String, String> = mutableMapOf(
            "AccessToken" to  (request.getHeader("AccessToken")?.toString() ?: ""),
            "RefreshToken" to (request.getHeader("RefreshToken")?.toString() ?: "")
        )

        return parseClaims(mutableMap)?.get("userSeq")?.toString()?.toLong()

    }



}
