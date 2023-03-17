package cinema.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers(HttpMethod.POST,"/movies/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/movies/**").hasAnyAuthority("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/cinema-halls/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/cinema-halls/**").hasAnyAuthority("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/movie-sessions/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/movie-sessions/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/movie-sessions/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/movie-sessions/available")
                .hasAnyAuthority("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/users/by-email").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/shopping-carts/**").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/shopping-carts/**").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/orders/**").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/orders/**").hasAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
