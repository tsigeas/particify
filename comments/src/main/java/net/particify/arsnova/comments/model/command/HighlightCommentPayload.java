package net.particify.arsnova.comments.model.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;

import net.particify.arsnova.comments.model.WebSocketPayload;

public class HighlightCommentPayload implements WebSocketPayload {
  private UUID id;
  private boolean lights;

  public HighlightCommentPayload() {
  }

  public HighlightCommentPayload(UUID id) {
    this.id = id;
  }

  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  @JsonProperty("lights")
  public boolean getLights() {
    return lights;
  }

  @JsonProperty("lights")
  public void setLights(boolean lights) {
    this.lights = lights;
  }

  @Override
  public String toString() {
    return "HighlightCommentPayload{" +
        "id='" + id + '\'' +
        ", lights=" + lights +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HighlightCommentPayload that = (HighlightCommentPayload) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(lights, that.lights);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, lights);
  }
}
