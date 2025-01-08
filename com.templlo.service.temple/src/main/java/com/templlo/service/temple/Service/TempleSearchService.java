package com.templlo.service.temple.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.templlo.service.temple.dto.TempleResponse;
import com.templlo.service.temple.global.response.PageResponse;
import com.templlo.service.temple.model.SearchTemple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempleSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public PageResponse<TempleResponse> searchTemples(String keyword, Pageable pageable) {

        log.info("Searching temples with keyword: '{}' and pageable: {}", keyword, pageable);

        NativeQueryBuilder queryBuilder = NativeQuery.builder();
        BoolQuery.Builder boolQuery = QueryBuilders.bool();

        // 키워드 검색: 사찰 이름, 주소, 설명 필드를 대상으로 검색
        boolQuery.must(QueryBuilders.multiMatch(field ->
                field.query(keyword)
                        .fields(List.of("templeName^3", "templeDescription", "address.roadAddress", "address.detailAddress"))
                        .fuzziness("AUTO") // 오타 허용
        ));

        queryBuilder.withQuery(boolQuery.build()._toQuery())
                .withPageable(pageable);

        // Elasticsearch에서 쿼리 실행
        SearchHits<SearchTemple> searchHits = elasticsearchOperations.search(
                queryBuilder.build(), SearchTemple.class
        );

        List<TempleResponse> content = searchHits.map(hit -> TempleResponse.from(hit.getContent())).toList();
        int totalHits = (int) searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalHits / pageable.getPageSize());

        return new PageResponse<>(totalPages, pageable.getPageNumber() + 1, content);
    }
}
