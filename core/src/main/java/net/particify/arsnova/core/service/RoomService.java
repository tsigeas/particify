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

package net.particify.arsnova.core.service;

import java.util.List;
import java.util.Optional;

import net.particify.arsnova.core.model.Room;
import net.particify.arsnova.core.model.RoomMembership;

/**
 * The functionality the session service should provide.
 */
public interface RoomService extends EntityService<Room> {
  String getIdByShortId(String shortId);

  List<String> getUserRoomIds(String userId);

  String getPassword(Room room);

  void setPassword(Room room, String password);

  Optional<RoomMembership> requestMembership(String roomId, String password);

  Optional<RoomMembership> requestMembershipByToken(String roomId, String token);

  String generateShortId();
}
