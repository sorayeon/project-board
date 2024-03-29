package com.takealook.projectboard.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.takealook.projectboard.domain.ArticleComment;
import com.takealook.projectboard.domain.QArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {
    List<ArticleComment> findByArticle_Id(Long articleId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.createdAt, root.createdBy);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like '%${v}%' or StringExpression::likeIgnoreCase => like '${v}' (%를 직접 넣어줘야함)
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // like '%${v}%' or StringExpression::likeIgnoreCase => like '${v}' (%를 직접 넣어줘야함)
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // like '%${v}%' or StringExpression::likeIgnoreCase => like '${v}' (%를 직접 넣어줘야함)
    }
}
