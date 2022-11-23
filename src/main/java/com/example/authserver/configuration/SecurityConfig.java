package com.example.authserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/health").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        return http.build();
    }

    /**
     * UserDetailsService: Having access to the user’s password
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 =
                User.withDefaultPasswordEncoder()
                        .username("yd")
                        .password("123")
                        .roles("ADMIN")
                        .build();

        UserDetails user2 =
                User.withDefaultPasswordEncoder()
                        .username("dd")
                        .password("123")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

//    @Bean
//    public UserDetailsService users(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }
}
