package com.shareDiscount.config;


import com.shareDiscount.security.JwtAuthenticationSuccessHandler;
import com.shareDiscount.security.RestAuthenticationEntryPoint;
import com.shareDiscount.security.jwt.JwtAuthenticationProvider;
import com.shareDiscount.security.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@ComponentScan("com.shareDiscount.security")
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {

        return new ProviderManager(Arrays.asList(authenticationProvider));
    }


    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManager());
        authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        return authenticationTokenFilter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/api/login")
                .antMatchers("/api/signup");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable();

        httpSecurity
                .authorizeRequests()
                .antMatchers("/documentation/**",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-ui.html",
                        "/swagger-resources").permitAll()

                // All urls must be authenticated (filter for token always fires (/api/**)
                .anyRequest().authenticated()
                .and()
                // Call our errorHandler if authentication fails
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), AbstractPreAuthenticatedProcessingFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}
