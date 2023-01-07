package com.devsuperior.bds04.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer //FAZ COM QUE ESSA CLASSE IMPLEMENTE O ResourceServer DO OAuth2
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Autowired
	private Environment env;
	
	@Autowired
	private TokenStore tokenStore;

	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };

	private static final String[] PUBLIC_GET = { "/cities/**","/events/**" };
	
	private static final String[] CLIENT = { "/events/**" };


	@Override //PERMITE QUE O RESOURCE SERVER FAÇA AS DEVIDAS VERIFICAÇÕES DO TOKEN GERADO PELO AUTHORIZATION SERVER
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		//UNLOCK ACCESS H2 CONSOLE
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()
		.antMatchers(HttpMethod.GET,PUBLIC_GET).permitAll()
		.antMatchers(CLIENT).hasAnyRole("ADMIN","CLIENT")
		.anyRequest().hasRole("ADMIN");
	}



}