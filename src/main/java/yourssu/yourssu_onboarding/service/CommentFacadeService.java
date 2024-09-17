package yourssu.yourssu_onboarding.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.DTO.CommentDTO;
import yourssu.yourssu_onboarding.DTO.CommentReturnDTO;
import yourssu.yourssu_onboarding.DTO.Credential;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.Comment;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.CommentRepository;

import java.time.LocalDateTime;

@Service
public class CommentFacadeService {
    private final CommentService commentService;
    private final UserService userService;
    private final ArticleService articleService;

    public CommentFacadeService(CommentService commentService, UserService userService, ArticleService articleService) {
        this.commentService = commentService;
        this.userService = userService;
        this.articleService = articleService;
    }

    @Transactional
    public CommentReturnDTO newComment(CommentDTO commentDTO, Long articleId) {
        // authenticate user
        userService.authenticate(commentDTO.getEmail(), commentDTO.getPassword());
        // validate request input
        commentDTO.validateData();

        User user = userService.findByEmail(commentDTO.getEmail());

        Article article = articleService.findById(articleId);
        LocalDateTime now = LocalDateTime.now();

        Comment comment = Comment.builder()
                .user(user)
                .content(commentDTO.getContent())
                .created_at(now)
                .updated_at(now)
                .article(article)
                .build();

        commentService.save(comment);

        return CommentReturnDTO.builder()
                .commentId(comment.getComment_id())
                .content(comment.getContent())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public CommentReturnDTO fixComment(CommentDTO commentDTO, Long commentId) {
        // authenticate user
        userService.authenticate(commentDTO.getEmail(), commentDTO.getPassword());
        // validate request input
        commentDTO.validateData();

        // validate comment owner
        User user = userService.findByEmail(commentDTO.getEmail());
        commentService.validateOwner(commentId, user);

        // save changes
        Comment comment = commentService.findById(commentId);
        LocalDateTime now = LocalDateTime.now();
        comment.setContent(commentDTO.getContent());
        comment.setUpdated_at(now);

        comment = commentService.save(comment);

        return CommentReturnDTO.builder()
                .commentId(comment.getComment_id())
                .content(comment.getContent())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void deleteComment(Credential credential, Long commentId) {
        // authenticate user
        userService.authenticate(credential.getEmail(), credential.getPassword());

        // validate comment owner
        User user = userService.findByEmail(credential.getEmail());
        commentService.validateOwner(commentId, user);

        commentService.deleteById(commentId);
    }


}
