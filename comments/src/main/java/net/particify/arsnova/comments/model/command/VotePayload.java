package net.particify.arsnova.comments.model.command;

import java.util.Objects;
import java.util.UUID;

import net.particify.arsnova.comments.model.WebSocketPayload;

public class VotePayload implements WebSocketPayload {
  private UUID userId;
  private UUID commentId;

  public VotePayload() {
  }

  public VotePayload(UUID userId, UUID commentId) {
    this.userId = userId;
    this.commentId = commentId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public UUID getCommentId() {
    return commentId;
  }

  public void setCommentId(UUID commentId) {
    this.commentId = commentId;
  }

  @Override
  public String toString() {
    return "VotePayload{" +
        "userId='" + userId + '\'' +
        ", commentId='" + commentId + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VotePayload that = (VotePayload) o;
    return Objects.equals(userId, that.userId) &&
        Objects.equals(commentId, that.commentId);
  }

  @Override
  public int hashCode() {

    return Objects.hash(userId, commentId);
  }
}
