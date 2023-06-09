package net.particify.arsnova.comments.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtTokenFilter extends GenericFilterBean {
  private static final Pattern BEARER_TOKEN_PATTERN = Pattern.compile("Bearer (.*)", Pattern.CASE_INSENSITIVE);
  private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Override
  public void doFilter(final ServletRequest servletRequest,
      final ServletResponse servletResponse,
      final FilterChain filterChain)
      throws IOException, ServletException {
    final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

    JwtToken token = null;
    final String jwtHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if (jwtHeader != null) {
      final Matcher tokenMatcher = BEARER_TOKEN_PATTERN.matcher(jwtHeader);
      if (tokenMatcher.matches()) {
        token = new JwtToken(tokenMatcher.group(1));
      } else {
        logger.debug("Unsupported authentication scheme.");
      }
    }

    if (token != null) {
      try {
        final Authentication authenticatedToken = jwtAuthenticationProvider.authenticate(token);
        logger.debug("Storing JWT to SecurityContext: {}", authenticatedToken);
        SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
      } catch (final Exception e) {
        logger.debug("JWT authentication failed", e);
      }
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Autowired
  public void setJwtAuthenticationProvider(final JwtAuthenticationProvider jwtAuthenticationProvider) {
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
  }
}
