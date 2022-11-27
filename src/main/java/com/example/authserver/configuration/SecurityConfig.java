package com.example.authserver.configuration;

import com.example.authserver.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/*").permitAll()
                .antMatchers("/test/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    // TODO: WHat is this
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // TODO: WHat is this
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user1 =
//                User.withDefaultPasswordEncoder()
//                        .username("yd")
//                        .password("123")
//                        .roles("ADMIN")
//                        .build();
//
//        UserDetails user2 =
//                User.withDefaultPasswordEncoder()
//                        .username("dd")
//                        .password("123")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

//    @Bean
//    public UserDetailsService users(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }


}
