package com.example.myapp.Config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import kotlin.jvm.Throws


@Configuration
@EnableWebSecurity
class SecurityConfig  {

    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity) : SecurityFilterChain{
        httpSecurity.csrf{it.disable()}
            .httpBasic{it.disable()}
            .formLogin { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("/user/userJoin", "/" ,"/user/userLogin").permitAll()
                    .anyRequest().authenticated()
            }
            .logout{logout ->
                logout.logoutSuccessUrl("/userLogin")
                    .invalidateHttpSession(true)
            }
            .sessionManagement{session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
        return httpSecurity.build();
    }


    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder();
    }


}