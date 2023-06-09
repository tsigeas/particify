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

package net.particify.arsnova.core.model;

import jakarta.validation.ValidationException;
import org.springframework.validation.Errors;

public class EntityValidationException extends ValidationException {
  private Errors errors;
  private Entity entity;

  public EntityValidationException(final Errors errors, final Entity entity) {
    super(errors.getAllErrors().toString());
    this.errors = errors;
    this.entity = entity;
  }

  public Errors getErrors() {
    return errors;
  }

  public Entity getEntity() {
    return entity;
  }
}
