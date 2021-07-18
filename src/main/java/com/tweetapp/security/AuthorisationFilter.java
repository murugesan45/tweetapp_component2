package com.tweetapp.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class AuthorisationFilter extends BasicAuthenticationFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorisationFilter.class);

	Environment env;

	public AuthorisationFilter(AuthenticationManager authenticationManager, Environment env) {
		super(authenticationManager);
		this.env = env;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		String authorizationHeader = req.getHeader(env.getProperty("authorization.token.header.name"));
		if (authorizationHeader == null
				|| !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
			chain.doFilter(req, res);
			return;
		}

		try {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch (ExpiredJwtException e) {
			LOGGER.error("Error!! Jwt token is expired " + e);
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		} catch (SignatureException e) {
			LOGGER.error("Error!! Jwt Signature exception " + e);
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		} catch (MalformedJwtException e) {
			LOGGER.error("Error!!  Jwt Signature exception " + e);
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;

		}

	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String authorizationHeader = req.getHeader(env.getProperty("authorization.token.header.name"));

		String userId = "";
		if (authorizationHeader != null) {
			String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "");
			userId = Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(token).getBody()
					.getSubject();
		}

		return (userId == null || userId == "") ? null
				: new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

	}

}
