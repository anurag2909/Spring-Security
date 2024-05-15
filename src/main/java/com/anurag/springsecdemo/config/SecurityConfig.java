package com.anurag.springsecdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JwtFilter jwtfilter;

    @Bean
    public AuthenticationProvider authProvider(){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // we are not opting password encryption as of now

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        http.csrf(customizer -> customizer.disable()); // Disabling the CSRF
//        http.authorizeHttpRequests(request -> request.requestMatchers("register").permitAll().anyRequest().authenticated()); // For Login Page, apart from login. all other requests will be authenticated
//        // http.formLogin((Customizer.withDefaults())); // for form login
//        http.httpBasic(Customizer.withDefaults()); // Now it should give me the form login
//
//        // Everytime you refresh, it will have the same session ID. To change this,
//
//        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        // You don't need form login, when you are using stateless.

        http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("register", "login")
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();



        //return http.build();


    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails user = User.
//                withDefaultPasswordEncoder().
//                username("Anurag").
//                password("Anurag@2909").
//                roles("USER").
//                build();
//
//        UserDetails admin = User.
//                withDefaultPasswordEncoder().
//                username("Kiran").
//                password("Kiran@1904").
//                roles("ADMIN").
//                build();
//
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
