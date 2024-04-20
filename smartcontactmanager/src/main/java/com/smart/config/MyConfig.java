package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MyConfig {

    
	@Bean
    //authentication
      UserDetailsService getUserDetailsService()
    {
        return new UserDetailsServiceImpl();
    }

    @Bean
      BCryptPasswordEncoder passwordEncoder()
    {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
   DaoAuthenticationProvider authenticationProvider()
    {
    	DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
    	daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
    	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    	
    	return daoAuthenticationProvider;
    }

   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	 auth.authenticationProvider(authenticationProvider());
}
   @Bean
 protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/**").permitAll()
				
				//.anyRequest().authenticated()
			)
			
			.formLogin((form) -> form
			.loginPage("/signin")
			.loginProcessingUrl("/dologin")
			.defaultSuccessUrl("/user/index")
	//		.failureUrl("/login_fail") 
          //   .permitAll()
			);
		http.logout(logout ->
        logout.logoutUrl("/logout") 
      //  .logoutSuccessUrl("/logout")
        .logoutSuccessUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );
			
			/*
			.logout((logout)  -> 
			logout.logoutUrl("/logout"));
*/
	
		return http.build();
	}

}

//.logout((logout) -> logout.permitAll());

   



	 
	 



