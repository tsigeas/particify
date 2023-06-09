/*
 * This file is part of ARSnova Backend.
 * Copyright (C) 2012-2019 The ARSnova Team and Contributors
 *
 * ARSnova Backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ARSnova Backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.particify.arsnova.core.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


/**
 * This class gets called when a user failed to login.
 */
public class LoginAuthenticationFailureHandler extends
    SimpleUrlAuthenticationFailureHandler {
  public static final String URL_ATTRIBUTE = "ars-login-failure-url";

  private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
  private String failureUrl;

  @Override
  public void onAuthenticationFailure(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException exception
  ) throws IOException, ServletException {
    final HttpSession session = request.getSession();
    if (session != null && session.getAttribute(URL_ATTRIBUTE) != null) {
      failureUrl = (String) session.getAttribute(URL_ATTRIBUTE);
    }

    redirectStrategy.sendRedirect(request, response, failureUrl);
  }

  @Override
  public void setDefaultFailureUrl(final String url) {
    failureUrl = url;
  }

}
