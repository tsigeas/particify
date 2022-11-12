package net.particify.arsnova.comments.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.particify.arsnova.comments.exception.ForbiddenException;
import net.particify.arsnova.comments.model.Settings;
import net.particify.arsnova.comments.model.command.CreateSettings;
import net.particify.arsnova.comments.model.command.CreateSettingsPayload;
import net.particify.arsnova.comments.model.command.UpdateSettings;
import net.particify.arsnova.comments.model.command.UpdateSettingsPayload;
import net.particify.arsnova.comments.security.PermissionEvaluator;
import net.particify.arsnova.comments.service.SettingsService;

@Service
public class SettingsCommandHandler {
  private static final Logger logger = LoggerFactory.getLogger(SettingsCommandHandler.class);

  private final SettingsService service;
  private final PermissionEvaluator permissionEvaluator;

  @Autowired
  public SettingsCommandHandler(
      SettingsService service,
      PermissionEvaluator permissionEvaluator
  ) {
    this.service = service;
    this.permissionEvaluator = permissionEvaluator;
  }

  public Settings handle(
      final String roomId,
      CreateSettings command
  ) {
    logger.debug("Got new command: {}", command);

    CreateSettingsPayload payload = command.getPayload();

    if (!permissionEvaluator.isOwnerOrAnyTypeOfModeratorForRoom(payload.getRoomId())) {
      throw new ForbiddenException();
    }

    Settings newSettings = new Settings();
    newSettings.setRoomId(roomId);
    newSettings.setDirectSend(payload.getDirectSend());

    Settings saved = service.create(newSettings);

    return saved;
  }

  public Settings handle(
      final String roomId,
      UpdateSettings command
  ) {
    logger.debug("Got new command: {}", command);

    UpdateSettingsPayload payload = command.getPayload();

    if (!permissionEvaluator.isOwnerOrAnyTypeOfModeratorForRoom(payload.getRoomId())) {
      throw new ForbiddenException();
    }

    Settings settings = service.get(roomId);
    settings.setRoomId(roomId);
    settings.setDirectSend(payload.getDirectSend());
    settings.setFileUploadEnabled(payload.isFileUploadEnabled());

    Settings updated = service.update(settings);

    return updated;

  }
}