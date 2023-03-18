package com.project.zaiika.config;

import com.project.zaiika.auth.JwtAuthFilter;
import com.project.zaiika.models.userModels.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**")
                    .permitAll()
                .requestMatchers("/api/manage-place**", "/api/manage-users**", "/api/manage-role**")
                    .hasAuthority(UserRole.DUNGEON_MASTER.name())
                .requestMatchers("/api/manage/**")
                    .hasAnyAuthority(
                            UserRole.PLACE_OWNER.name(),
                            UserRole.DUNGEON_MASTER.name(),
                            UserRole.ADMIN.name()
                    )
                .requestMatchers("/api/site/*", "/api/menu/*")
                    .hasAnyAuthority(
                            UserRole.DUNGEON_MASTER.name(),
                            UserRole.ADMIN.name(),
                            UserRole.PLACE_OWNER.name(),
                            UserRole.WORKER.name()
                    )
                .requestMatchers("/api/order")
                    .hasAnyAuthority(
                            UserRole.PLACE_OWNER.name(),
                            UserRole.ADMIN.name(),
                            UserRole.WORKER.name()
                    )
                .anyRequest()//TODO удалить на релизе
                    .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                .and()
                .build();
    }
}
