package de.thm.arsnova.service.comment;

import de.thm.arsnova.service.comment.model.Vote;
import de.thm.arsnova.service.comment.model.command.Downvote;
import de.thm.arsnova.service.comment.model.command.Upvote;
import de.thm.arsnova.service.comment.model.command.VotePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoteCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(VoteCommandHandler.class);

    private final VoteService service;
    private final CommentEventSource eventer;

    @Autowired
    public VoteCommandHandler(
            VoteService service,
            CommentEventSource eventer
    ) {
        this.service = service;
        this.eventer = eventer;
    }

    public Vote handle(Upvote vote) {
        logger.trace("got new command: " + vote.toString());

        VotePayload p = vote.getPayload();
        Vote v = new Vote();
        v.setCommentId(p.getCommentId());
        v.setVote(1);
        v.setUserId(p.getUserId());

        Vote saved = service.create(v);

        eventer.ScoreChanged(p.getCommentId());

        return saved;
    }

    public Vote handle(Downvote vote) {
        logger.trace("got new command: " + vote.toString());

        VotePayload p = vote.getPayload();
        Vote v = new Vote();
        v.setCommentId(p.getCommentId());
        v.setVote(-1);
        v.setUserId(p.getUserId());

        Vote saved = service.create(v);

        eventer.ScoreChanged(p.getCommentId());

        return saved;
    }
}
