package com.example.loginapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    	BCryptPasswordEncoder encoder = passwordEncoder();
        auth.inMemoryAuthentication().passwordEncoder(encoder).
        withUser("manager").password(encoder.encode("password")).authorities("ROLE_MANAGER").and().
        withUser("user1").password(encoder.encode("password")).authorities("ROLE_USER");
        

        auth.authenticationProvider(authenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/").permitAll()
        	.antMatchers("/users", "/newUser").hasAnyRole("MANAGER").anyRequest().authenticated().and()
            .formLogin().defaultSuccessUrl("/").permitAll().and()
			.logout().permitAll().and()
			.exceptionHandling().accessDeniedPage("/403");

        http.csrf().disable();
    }

}