package fi.vm.sade.vtj.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.lang.Exception
import kotlin.jvm.Throws

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/actuator/health").permitAll()
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(userDetailsService())

    }

    override fun userDetailsService(): UserDetailsService {
        return object: UserDetailsService {
            override fun loadUserByUsername(username: String?): UserDetails {
                if (VTJ_SERVICE_USERNAME == username) {
                    return User(
                            username,
                            "", // serttiautentikointi; ei salasanaa
                            AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"))
                }
                throw UsernameNotFoundException("No such user: $username")
            }
        }
    }
}

const val VTJ_SERVICE_USERNAME = "vtj-service"
