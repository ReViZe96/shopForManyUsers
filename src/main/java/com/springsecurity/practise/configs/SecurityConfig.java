package com.springsecurity.practise.configs;

import com.springsecurity.practise.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService; //см. соответствующий класс

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // защита на уровне запросов
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated() //защита на уровне запросов для всех остальных URL-ов (нету)
                .and()
                .formLogin() //позволяет иметь форму для авторизации
                .loginPage("/login.html") //URL формы авторизации
                .loginProcessingUrl("/login") //URL для отправки авторизационных данных формы
                .defaultSuccessUrl("/mainPage", true)// URL на который переходит приложение после удачной авторизации
                .failureUrl("/login?error") //URL на который переходит приложение после неудачной авторизации
                .permitAll()
                .and()
                .logout() //позволяет настроить правила выхода из учётной записи
                .logoutUrl("/logout") //URL выхода из системы
                .logoutSuccessUrl("/mainPage") //позволяет указать URL на который перейдёт приложение после выхода из учётки
                .deleteCookies("JSESSIONID") //очищает куки
                .permitAll(); // доступ для всех
    }

    //хэширует пароли для хранения
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }
}