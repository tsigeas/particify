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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.particify.arsnova.core.model.UserProfile;
import net.particify.arsnova.core.service.UserService;

/**
 * Loads UserDetails for a guest user ({@link UserProfile.AuthProvider#ARSNOVA_GUEST}) based on the username (guest
 * token).
 *
 * @author Daniel Gerhardt
 */
@Service
public class GuestUserDetailsService extends AbstractUserDetailsService
    implements UserDetailsService {
  public static final GrantedAuthority ROLE_GUEST_USER = new SimpleGrantedAuthority("ROLE_GUEST_USER");

  private final Collection<GrantedAuthority> defaultGrantedAuthorities = Set.of(
      User.ROLE_USER,
      ROLE_GUEST_USER
  );

  public GuestUserDetailsService(final UserService userService) {
    super(UserProfile.AuthProvider.ARSNOVA_GUEST, userService);
  }

  @Override
  public UserDetails loadUserByUsername(final String loginId) throws UsernameNotFoundException {
    return loadUserByUsername(loginId, false);
  }

  public UserDetails loadUserByUsername(String loginId, final boolean autoCreate) {
    if (autoCreate) {
      loginId = userService.generateGuestId();
      return getOrCreate(loginId, defaultGrantedAuthorities, Collections.emptyMap());
    }

    return get(loginId, defaultGrantedAuthorities);
  }
}
