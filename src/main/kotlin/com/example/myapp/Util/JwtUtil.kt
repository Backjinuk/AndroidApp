package com.example.myapp.Util

import com.example.myapp.Dto.UserDto
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.io.Decoders
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.time.ZonedDateTime
import java.util.Date

@Slf4j
@Component
class JwtUtil(
    @Value("\${jwt.secret}") secretKey: String,
    @Value("\${jwt.expiration_time}") private val accessTokenExpTime: Long) {

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
     * JWT생성
     * @param userDto : UserDto
     * @param expiretime : Long
     * @return JWT String : String
     */
    fun createToken(userDto: UserDto, accessTokenExpTime: Long): String {
        val claims: Claims = Jwts.claims().also {
            it["userSeq"] = userDto.userSeq
            it["email"] = userDto.email
        }

        val now: ZonedDateTime = ZonedDateTime.now()
        val tokenValidity: ZonedDateTime = now.plusSeconds(accessTokenExpTime)

        return try {
            Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact().also {
                    println("Token generated successfully: $it")
                }
        } catch (e: Exception) {
            println("Error creating token: ${e.message}")
            e.printStackTrace()
            ""
        }
    }
    /**
     * Token에서 User ID 추출
     * @param token : String
     * @return UserSeq :Long
     */
    fun getUserSeq(token:String) : Long{
        return parseClaims(token)["userSeq", Long::class.java]
    }

    /**
     * JWT Claims
     * @param token : String
     * @return Claims : Claims
     */
    fun parseClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body;
        } catch (e: ExpiredJwtException) {
            e.claims;
        }
    }

    /**
     * JWT검즌
     * @param token : string
     * @return IsValidate : boolean
     */
    fun validateToken(token : String) : Boolean{
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true;
        } catch (e: SecurityException) {
            log.info("Invalid JWT Token", e)
            false
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT Token", e)
            false
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT Token", e)
            false
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT Token", e)
            false
        } catch (e: IllegalArgumentException) {
            log.info("JWT claims string is empty.", e)
            false
        }
    }


}
