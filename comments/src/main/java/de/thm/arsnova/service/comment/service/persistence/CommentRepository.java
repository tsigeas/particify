package de.thm.arsnova.service.comment.service.persistence;

import de.thm.arsnova.service.comment.model.Comment;

import java.util.Set;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findByRoomId(String roomId);
    List<Comment> findByIdInAndRoomId(Set<String> ids, String roomId);
    @Transactional
    List<Comment> deleteByRoomId(String roomId);
    long countByRoomIdAndAckAndArchiveIdNull(String roomId, Boolean ack);
    List<Comment> findByArchiveId(String archiveId);
}
