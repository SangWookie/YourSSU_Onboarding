package yourssu.yourssu_onboarding.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.entity.Comment;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.CommentRepository;

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment findById(Long id){
        Optional<Comment> findComment = commentRepository.findById(id);
        if(findComment.isEmpty()){
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
        return findComment.get();
    }

    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void validateOwner(Long commentId, User user) {
        Comment comment = findById(commentId);
        if(!comment.getUser().equals(user)){
            throw new BadCredentialsException("You are not the owner of this comment");
        }
    }

    @Transactional
    public void deleteById(Long id){
        commentRepository.deleteById(id);
    }
}
