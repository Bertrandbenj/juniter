package juniter.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web) {
       // web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // use for at least graphiql
        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and().cors()
                .and().csrf().disable()


        .exceptionHandling()
        //.defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
        //.disable()
        ;
    }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // use for at least graphiql
//        http.authorizeRequests()
//                .anyRequest()
//                .permitAll()
//                .and().cors()
//
//                //.disable()
//
//                .and().exceptionHandling()
//                .and().authorizeRequests()
//                .antMatchers("/js/**", "/lib/**", "/images/**", "/css/**", "/index.html", "/", "/*.css", "/webjars/**", "/*.js").permitAll()
//
//                .antMatchers("/","/ws/**","/ws/block/websocket" , "/websocket", "/graphql", "/**/websocket","/node/summary").permitAll()
//                //.defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
//                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//                .anyRequest().permitAll()
//                .and().csrf().disable()
//        //
//        //
//        ;
//    }


    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/favicon.ico"),
            new AntPathRequestMatcher("/core/**"),
            new AntPathRequestMatcher("/ws/**"),
            new AntPathRequestMatcher("/graphiql/**"),
            new AntPathRequestMatcher("/graphql/websocket/**"),
            new AntPathRequestMatcher("/error")
    );
    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(FORBIDDEN);
    }

}