package com.templlo.service.temple.repository.jpa;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.templlo.service.temple.global.exception.BaseException;
import com.templlo.service.temple.global.response.BasicStatusCode;
import com.templlo.service.temple.model.QTemple;
import com.templlo.service.temple.model.Temple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TempleCustomRepositoryImpl implements TempleCustomRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Temple> findTemplesByRegion(String region, Pageable pageable) {
        QTemple temple = QTemple.temple;

        if (region == null || region.isEmpty()) {
            throw new BaseException(BasicStatusCode.BAD_REQUEST);
        }

        // 사용자가 입력한 지역명에서 앞 두 글자만 추출
        String regionKeyword = region.trim().substring(0, Math.min(region.length(), 2));

        // QueryDSL에서 페이징 처리
        JPQLQuery<Temple> query = queryFactory.selectFrom(temple)
                .where(temple.address.roadAddress.likeIgnoreCase("%" + regionKeyword + "%"));

        List<Temple> temples = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수를 구하기 위한 Query
        JPQLQuery<Long> countQuery = queryFactory
                .select(temple.count())
                .from(temple)
                .where(temple.address.roadAddress.likeIgnoreCase("%" + regionKeyword + "%"));

        long total = countQuery.fetchOne();

        return new PageImpl<>(temples, pageable, total);
    }



}
