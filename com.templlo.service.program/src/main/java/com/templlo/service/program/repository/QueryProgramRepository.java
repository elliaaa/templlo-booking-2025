package com.templlo.service.program.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.templlo.service.program.entity.Program;
import com.templlo.service.program.entity.ProgramType;
import com.templlo.service.program.entity.QProgram;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class QueryProgramRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Program> findByKeyword(String keyword, ProgramType type, List<String> days, Pageable pageable) {

        QProgram program = QProgram.program;

        BooleanBuilder builder = new BooleanBuilder();

        // 키워드 검색 (title, description)
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(program.title.containsIgnoreCase(keyword).or(program.description.containsIgnoreCase(keyword))
            );
        }

        // ProgramType 조건
        if (type != null) {
            builder.and(program.type.eq(type));
        }

        // days 조건
        if (days != null && !days.isEmpty()) {
            BooleanBuilder daysCondition = new BooleanBuilder();
            days.forEach(day -> daysCondition.or(program.programDays.contains(day)));
            builder.and(daysCondition);
        }

        // 현재 페이지 데이터 조회
        List<Program> results = queryFactory.selectFrom(program)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        // 전체 데이터 개수 조회
        long total = Optional.ofNullable(queryFactory.select(program.count())
                .from(program)
                .where(builder)
                .fetchOne())
                .orElse(0L);

        return new PageImpl<>(results, pageable, total);

    }
}
