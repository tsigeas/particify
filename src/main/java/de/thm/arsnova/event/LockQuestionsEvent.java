/*
 * This file is part of ARSnova Backend.
 * Copyright (C) 2012-2018 The ARSnova Team and Contributors
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
package de.thm.arsnova.event;

import de.thm.arsnova.model.Content;

import java.util.List;

/**
 * Fires whenever a set of contents are disabled, i.e., they are hidden from students.
 */
public class LockQuestionsEvent extends RoomEvent {

	private static final long serialVersionUID = 1L;

	private List<Content> contents;

	public LockQuestionsEvent(Object source, String roomId, List<Content> contents) {
		super(source, roomId);
		this.contents = contents;
	}

	public List<Content> getQuestions() {
		return this.contents;
	}

}