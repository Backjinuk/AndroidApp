package com.example.myapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class MyAppApplication

fun main(args: Array<String>) {
    runApplication<MyAppApplication>(*args)
}
