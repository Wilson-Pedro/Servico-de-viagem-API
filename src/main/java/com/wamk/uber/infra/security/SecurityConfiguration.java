package com.wamk.uber.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	final SecurityFilter securityFilter;
	
	public SecurityConfiguration(SecurityFilter securityFilter) {
		this.securityFilter = securityFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		return httpSecurity
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers(new AntPathRequestMatcher("/auth/login", HttpMethod.POST.toString())).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/auth/register", HttpMethod.POST.toString())).permitAll()
					//CARRO
					.requestMatchers(new AntPathRequestMatcher("/carros", HttpMethod.GET.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/carros/pages", HttpMethod.GET.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/carros/{id}", HttpMethod.GET.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/carros/", HttpMethod.POST.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/carros/{id}", HttpMethod.PUT.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/carros/{id}", HttpMethod.DELETE.toString())).hasAnyRole("ADMIN")
					//USUARIO
					.requestMatchers(new AntPathRequestMatcher("/usuarios", HttpMethod.GET.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/usuarios/pages", HttpMethod.GET.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/usuarios/{id}", HttpMethod.GET.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/usuarios/{id}/viagens", HttpMethod.GET.toString())).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/usuarios/", HttpMethod.POST.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/usuarios/solicitacarViagem", HttpMethod.POST.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/usuarios/{id}", HttpMethod.PUT.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/usuarios/{usuarioId}/cancelarViagem", HttpMethod.DELETE.toString())).hasAnyRole("ADMIN","USER")
					.requestMatchers(new AntPathRequestMatcher("/usuarios/{id}/ativar", HttpMethod.PATCH.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/usuarios/{id}/desativa", HttpMethod.PATCH.toString())).hasAnyRole("ADMIN", "USER")
					//VIAGENS
					.requestMatchers(new AntPathRequestMatcher("/viagens", HttpMethod.GET.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/viagens/{id}", HttpMethod.GET.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/viagens/pages", HttpMethod.GET.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/viagens/", HttpMethod.POST.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/viagens/{id}", HttpMethod.PUT.toString())).hasAnyRole("ADMIN", "USER")
					.requestMatchers(new AntPathRequestMatcher("/viagens/{id}", HttpMethod.DELETE.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/viagens/{id}/cancelar", HttpMethod.DELETE.toString())).hasRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/viagens/{id}/finalizar", HttpMethod.PATCH.toString())).hasAnyRole("ADMIN", "USER")
					.anyRequest().authenticated()
			)
			.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
