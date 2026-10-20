package com.example.springhello

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class Greeting(val message: String)

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(@RequestParam(defaultValue = "world") name: String) =
        Greeting("Hello, $name!")
}