package com.moong_bee.blog.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.moong_bee.blog.config.jwt.TokenProvider;
import com.moong_bee.blog.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.moong_bee.blog.config.oauth.OAuth2SuccessHandler;
import com.moong_bee.blog.repository.RefreshTokenRepository;
import com.moong_bee.blog.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

	private final OAuth2UserCustomService oAuth2UserCustomService;
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserService userService;

	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring()
				.requestMatchers(toH2Console())
				.requestMatchers(new AntPathRequestMatcher("/img/**"), new AntPathRequestMatcher("/css/**"),
						new AntPathRequestMatcher("/js/**"));
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.logout(AbstractHttpConfigurer::disable);

		http.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		http.authorizeHttpRequests(request -> request
				.requestMatchers(new AntPathRequestMatcher("/api/token")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
				.anyRequest().permitAll());

		http.oauth2Login(oauth2Login -> oauth2Login
				.loginPage("/login")
				.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
						.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
				.successHandler(oAuth2SuccessHandler())
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
						.userService(oAuth2UserCustomService)));

		http.logout(logout -> logout
				.logoutSuccessUrl("/login"));

		http.exceptionHandling(exceptionHandling -> exceptionHandling
				.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
						new AntPathRequestMatcher("/api/**")));

		return http.build();
	}

	@Bean
	public OAuth2SuccessHandler oAuth2SuccessHandler() {
		return new OAuth2SuccessHandler(tokenProvider,
				refreshTokenRepository,
				oAuth2AuthorizationRequestBasedOnCookieRepository(),
				userService);
	}

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter(tokenProvider);
	}

	@Bean
	public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
		return new OAuth2AuthorizationRequestBasedOnCookieRepository();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}