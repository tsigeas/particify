package net.particify.arsnova.comments.model.event;

import java.util.Objects;
import java.util.UUID;

import net.particify.arsnova.comments.model.Comment;
import net.particify.arsnova.comments.model.WebSocketPayload;

public class CommentHighlightedPayload implements WebSocketPayload {
  private UUID id;
  private boolean lights;

  public CommentHighlightedPayload() {
  }

  public CommentHighlightedPayload(Comment c, boolean lights) {
    if (c != null) {
      id = c.getId();
      this.lights = lights;
    }
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public boolean getLights() {
    return lights;
  }

  public void setLights(boolean lights) {
    this.lights = lights;
  }

  @Override
  public String toString() {
    return "CommentHighlightedPayload{" +
        "id='" + id + '\'' +
        ", lights=" + lights +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommentHighlightedPayload that = (CommentHighlightedPayload) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(lights, that.lights);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, lights);
  }
}
