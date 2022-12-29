package com.takealook.projectboard.config;

import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 쿼리 파라이터 바인딩하여 로그 출력 포맷터 설정 Bean
 * 개발에서만 사용하도록 주의
 * 1.gradle
 * implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1'
 * 2.application.yml
 * logging:
 *   level:
 *     p6spy: info
 * decorator:
 *   datasource:
 *     p6spy:
 *       enable-logging: true
 *       logging: slf4j
 */
@Component
public class P6SpyFormatter implements MessageFormattingStrategy {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(category).append(" ").append(elapsed).append("ms");
        if (StringUtils.hasText(sql)) {
            sb.append(highlight(format(sql)));
        }
        return sb.toString();
    }

    private String format(String sql) {
        if (isDDL(sql)) {
            return FormatStyle.DDL.getFormatter().format(sql);
        } else if (isBasic(sql)) {
            return FormatStyle.BASIC.getFormatter().format(sql);
        }
        return sql;
    }

    private String highlight(String sql) {
        return FormatStyle.HIGHLIGHT.getFormatter().format(sql);
    }

    private boolean isDDL(String sql) {
        return sql.startsWith("create") || sql.startsWith("alter") || sql.startsWith("comment");
    }

    private boolean isBasic(String sql) {
        return sql.startsWith("select") || sql.startsWith("insert") || sql.startsWith("update") || sql.startsWith("delete");
    }
}
