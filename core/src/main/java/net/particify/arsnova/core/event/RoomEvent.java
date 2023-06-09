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

package net.particify.arsnova.core.event;

import java.io.Serial;

/**
 * Base class for all {@link ArsnovaEvent}s that are related to a room.
 */
public abstract class RoomEvent extends ArsnovaEvent {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String roomId;

  public RoomEvent(final Object source, final String roomId) {
    super(source);
    this.roomId = roomId;
  }

  public String getRoomId() {
    return roomId;
  }
}
