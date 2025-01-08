package com.templlo.service.temple.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.templlo.service.temple.dto.CreateTempleRequest;
import com.templlo.service.temple.dto.TempleResponse;
import com.templlo.service.temple.dto.UpdateTempleRequest;
import com.templlo.service.temple.global.exception.BaseException;
import com.templlo.service.temple.global.response.BasicStatusCode;
import com.templlo.service.temple.global.response.PageResponse;
import com.templlo.service.temple.model.SearchTemple;
import com.templlo.service.temple.model.SearchTempleAddress;
import com.templlo.service.temple.model.Temple;
import com.templlo.service.temple.repository.TempleRepository;
import com.templlo.service.temple.repository.elasticsearch.TempleElasticSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempleService {

    private final TempleRepository templeRepository;
    private final TempleElasticSearchRepository templeElasticSearchRepository;

    @Transactional
    @CacheEvict(value = "templesByRegion", allEntries = true)
    public TempleResponse createTemple(CreateTempleRequest request) {

        // 1. JPA로 Temple 저장
        Temple temple = Temple.of(
                request.getTempleName(),
                request.getTempleDescription(),
                request.getTemplePhone(),
                request.getRoadAddress(),
                request.getDetailAddress()
        );

        Temple savedTemple = templeRepository.save(temple);

        // 2. Elasticsearch에 SearchTemple 저장
        SearchTemple searchTemple = SearchTemple.from(savedTemple);
        templeElasticSearchRepository.save(searchTemple);

        return TempleResponse.from(savedTemple);
    }

    @Cacheable(value = "templesByRegion", key = "#region + '-' + #pageable.pageNumber")
    public PageResponse<TempleResponse> getTemplesByRegion(String region, Pageable pageable) {

        Page<Temple> templesPage = templeRepository.findTemplesByRegion(region, pageable);
        Page<TempleResponse> templeResponses = templesPage.map(temple -> TempleResponse.from(temple));

        return PageResponse.of(templeResponses);
    }


    @Transactional
    public TempleResponse updateTemple(UUID templeId, UpdateTempleRequest request) {

        Temple temple = templeRepository.findById(templeId)
                .orElseThrow(() -> new BaseException(BasicStatusCode.TEMPLE_NOT_FOUND));

        // 수정할 필드 업데이트
        if (request.getTempleName() != null) {
            temple.setTempleName(request.getTempleName());
        }
        if (request.getTempleDescription() != null) {
            temple.setTempleDescription(request.getTempleDescription());
        }
        if (request.getTemplePhone() != null) {
            temple.setTemplePhone(request.getTemplePhone());
        }
        if (request.getRoadAddress() != null) {
            temple.getAddress().setRoadAddress(request.getRoadAddress());
        }
        if (request.getDetailAddress() != null) {
            temple.getAddress().setDetailAddress(request.getDetailAddress());
        }

        templeRepository.save(temple);
        return TempleResponse.from(temple);
    }

    @Transactional
    public void deleteTemple(UUID templeId) {

        Temple temple = templeRepository.findById(templeId)
                .orElseThrow(() -> new BaseException(BasicStatusCode.TEMPLE_NOT_FOUND));

        templeRepository.delete(templeId);
    }

}
