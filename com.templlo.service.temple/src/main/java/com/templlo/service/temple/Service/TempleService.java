package com.templlo.service.temple.Service;

import com.templlo.service.temple.common.response.ApiResponse;
import com.templlo.service.temple.dto.CreateTempleRequest;
import com.templlo.service.temple.dto.TempleResponse;
import com.templlo.service.temple.dto.UpdateTempleRequest;
import com.templlo.service.temple.common.exception.BaseException;
import com.templlo.service.temple.common.response.BasicStatusCode;
import com.templlo.service.temple.common.response.PageResponse;
import com.templlo.service.temple.external.client.UserClient;
import com.templlo.service.temple.external.dto.UserData;
import com.templlo.service.temple.model.SearchTemple;
import com.templlo.service.temple.model.Temple;
import com.templlo.service.temple.repository.TempleRepository;
import com.templlo.service.temple.repository.elasticsearch.TempleElasticSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempleService {

    private final UserClient userClient;
    private final TempleRepository templeRepository;
    private final TempleElasticSearchRepository templeElasticSearchRepository;

    @Transactional
    @CacheEvict(value = "templesByRegion", allEntries = true)
    public TempleResponse createTemple(CreateTempleRequest request, String loginId) {

        // 1. FeignClient를 통해 사용자 정보 가져오기
        ApiResponse<UserData> userResponse = userClient.getUserInfo(loginId);

        if (userResponse == null || Integer.parseInt(userResponse.status()) != 200 || userResponse.getData() == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        UserData userData = userResponse.getData();

        // 2. 사용자 권한 검증 (TEMPLE_ADMIN만 허용)
        if (!"TEMPLE_ADMIN".equals(userData.role())) {
            throw new AccessDeniedException("TEMPLE_ADMIN 권한이 필요합니다.");
        }

        // 3. JPA로 Temple 저장
        Temple temple = Temple.of(
                request.getTempleName(),
                request.getTempleDescription(),
                request.getTemplePhone(),
                request.getRoadAddress(),
                request.getDetailAddress()
        );

        temple.setUserId(userData.id());

        Temple savedTemple = templeRepository.save(temple);

        // 4. Elasticsearch에 SearchTemple 저장
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
