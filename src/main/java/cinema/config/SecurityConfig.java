package cinema.config;

import static cinema.model.Role.RoleName.ADMIN;
import static cinema.model.Role.RoleName.USER;

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
                .antMatchers(HttpMethod.POST,"/movies/**").hasAuthority(ADMIN.name())
                .antMatchers(HttpMethod.GET,"/movies/**")
                .hasAnyAuthority(ADMIN.name(), USER.name())
                .antMatchers(HttpMethod.POST, "/cinema-halls/**").hasAuthority(ADMIN.name())
                .antMatchers(HttpMethod.GET, "/cinema-halls/**")
                .hasAnyAuthority(ADMIN.name(), USER.name())
                .antMatchers(HttpMethod.POST, "/movie-sessions/**").hasAuthority(ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/movie-sessions/**").hasAuthority(ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/movie-sessions/**").hasAuthority(ADMIN.name())
                .antMatchers(HttpMethod.GET, "/movie-sessions/available")
                .hasAnyAuthority(ADMIN.name(), USER.name())
                .antMatchers(HttpMethod.GET, "/users/by-email").hasAuthority(ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/shopping-carts/**").hasAuthority(USER.name())
                .antMatchers(HttpMethod.GET, "/shopping-carts/**").hasAuthority(USER.name())
                .antMatchers(HttpMethod.POST, "/orders/**").hasAuthority(USER.name())
                .antMatchers(HttpMethod.GET, "/orders/**").hasAuthority(USER.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
