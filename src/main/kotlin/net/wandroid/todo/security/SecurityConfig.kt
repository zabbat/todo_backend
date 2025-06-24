package net.wandroid.todo.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.web.filter.ForwardedHeaderFilter
import org.springframework.core.Ordered

@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val firebaseFilter: FirebaseAuthenticationFilter,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .csrf { it.disable() } // cross site ref
                .authorizeHttpRequests { customizer ->
                    customizer
                            .requestMatchers(AntPathRequestMatcher("/api/todo/**"))
                            .authenticated()
                    customizer.anyRequest().permitAll()
                }
                // .httpBasic {}
                .formLogin { it.disable() }
                .addFilterBefore(firebaseFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun forwardedHeaderFilter(): FilterRegistrationBean<ForwardedHeaderFilter> {
        val filterRegBean = FilterRegistrationBean(ForwardedHeaderFilter())
        filterRegBean.order = Ordered.HIGHEST_PRECEDENCE
        return filterRegBean
    }
}
