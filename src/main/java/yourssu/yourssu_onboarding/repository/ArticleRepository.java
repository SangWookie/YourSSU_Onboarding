package yourssu.yourssu_onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    void deleteByUser(User user);
    List<Article> findAllByUser(User user);
    Optional<Article> findByArticleId(Long id);
}
