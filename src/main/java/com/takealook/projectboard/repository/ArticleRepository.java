package com.takealook.projectboard.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.takealook.projectboard.domain.Article;
import com.takealook.projectboard.domain.QArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%${v}%' or StringExpression::likeIgnoreCase => like '${v}' (%를 직접 넣어줘야함)
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like '%${v}%' or StringExpression::likeIgnoreCase => like '${v}' (%를 직접 넣어줘야함)
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase); // like '%${v}%' or StringExpression::likeIgnoreCase => like '${v}' (%를 직접 넣어줘야함)
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // like '%${v}%' or StringExpression::likeIgnoreCase => like '${v}' (%를 직접 넣어줘야함)
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // like '%${v}%' or StringExpression::likeIgnoreCase => like '${v}' (%를 직접 넣어줘야함)
    }
}
