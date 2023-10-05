package com.tech.test.initializer

import com.tech.test.model.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
    return http
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers(antMatcher("/swagger-ui/**")).hasAnyRole(User.Roles.ADMIN.name, User.Roles.TESTER.name)
                    .requestMatchers(antMatcher("/api/**")).hasAnyRole(User.Roles.ADMIN.name, User.Roles.TESTER.name)
                    .anyRequest().authenticated()
            }
            .csrf{ it.disable() }
            .headers { it.disable() }
            .cors { it.disable() }
            .httpBasic(Customizer.withDefaults())

            .build()
    }
}