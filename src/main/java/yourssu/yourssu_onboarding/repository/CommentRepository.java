package yourssu.yourssu_onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.Comment;
import yourssu.yourssu_onboarding.entity.User;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticle(Article article);
    List<Comment> findAllByUser(User user);
}
