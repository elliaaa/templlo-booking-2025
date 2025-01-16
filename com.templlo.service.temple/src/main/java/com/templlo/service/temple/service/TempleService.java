package com.templlo.service.temple.service;

import com.templlo.service.temple.common.exception.BaseException;
import com.templlo.service.temple.common.response.ApiResponse;
import com.templlo.service.temple.common.response.BasicStatusCode;
import com.templlo.service.temple.common.response.PageResponse;
import com.templlo.service.temple.dto.CreateTempleRequest;
import com.templlo.service.temple.dto.TempleResponse;
import com.templlo.service.temple.dto.UpdateTempleRequest;
import com.templlo.service.temple.external.client.UserClient;
import com.templlo.service.temple.external.dto.UserData;
import com.templlo.service.temple.model.SearchTemple;
import com.templlo.service.temple.model.Temple;
import com.templlo.service.temple.repository.TempleRepository;
import com.templlo.service.temple.repository.elasticsearch.TempleElasticSearchRepository;
import com.templlo.service.temple.service.elasticsearch.ElasticSearchSynchronizer;
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
    private final ElasticSearchSynchronizer elasticSearchSynchronizer;

    @Transactional
    @CacheEvict(value = "templesByRegion", key = "#request.getRoadAddress().substring(0,2)+'-*'")
    public TempleResponse createTemple(CreateTempleRequest request, String loginId) {

        UserData userData = getUserDataAndValidateRole(loginId, "TEMPLE_ADMIN");

        // 1. JPA로 Temple 저장
        Temple temple = Temple.of(
                request.getTempleName(),
                request.getTempleDescription(),
                request.getTemplePhone(),
                request.getRoadAddress(),
                request.getDetailAddress()
        );

        temple.setUserId(userData.id());

        Temple savedTemple = templeRepository.save(temple);

        // 2. Elasticsearch에 SearchTemple 저장
        try {
            elasticSearchSynchronizer.saveTemple(savedTemple);
        } catch (Exception e) {
            log.error("Elasticsearch 저장 실패: {}", e.getMessage());
        }

        return TempleResponse.from(savedTemple);
    }


    @Transactional
    @CacheEvict(value = "templesByRegion", key = "@templeRepository.findById(#templeId).orElseThrow().address.roadAddress.substring(0,2)+'-*'")
    public TempleResponse updateTemple(UUID templeId, UpdateTempleRequest request, String loginId) {

        UserData userData = getUserDataAndValidateRole(loginId, "TEMPLE_ADMIN");

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

        Temple savedTemple = templeRepository.save(temple);

        // Elasticsearch에도 수정내용 반영
        try {
            elasticSearchSynchronizer.saveTemple(savedTemple);
        } catch (Exception e) {
            log.error("Elasticsearch 업데이트 실패: {}", e.getMessage());
        }

        return TempleResponse.from(savedTemple);
    }


    @Transactional
    public void deleteTemple(UUID templeId,String loginId) {

        UserData userData = getUserDataAndValidateRole(loginId, "MASTER");

        Temple temple = templeRepository.findById(templeId)
                .orElseThrow(() -> new BaseException(BasicStatusCode.TEMPLE_NOT_FOUND));

        temple.delete(loginId);

        templeRepository.save(temple);

        try {
            elasticSearchSynchronizer.deleteTemple(templeId.toString());
        } catch (Exception e) {
            log.error("Elasticsearch 삭제 실패: {}", e.getMessage());
        }
    }

    @Cacheable(value = "templesByRegion", key = "#region + '-' + #pageable.pageNumber")
    public PageResponse<TempleResponse> getTemplesByRegion(String region, Pageable pageable) {

        Page<Temple> templesPage = templeRepository.findTemplesByRegion(region, pageable);
        Page<TempleResponse> templeResponses = templesPage.map(temple -> TempleResponse.from(temple));

        return PageResponse.of(templeResponses);
    }

    @Transactional(readOnly = true)
    public void validateTempleAdmin(UUID templeId, String loginId) {
        // 사용자 정보 및 권한 확인
        UserData userData = getUserDataAndValidateRole(loginId, "TEMPLE_ADMIN");

        // Temple 엔티티 확인 및 userId 매칭
        Temple temple = templeRepository.findById(templeId)
                .orElseThrow(() -> new BaseException(BasicStatusCode.TEMPLE_NOT_FOUND));

        if (!temple.getUserId().equals(userData.id())) {
            throw new AccessDeniedException("해당 사찰의 관리자가 아닙니다.");
        }
    }


    // 사용자 정보 및 권한 검증을 처리하는 공통 메서드
    private UserData getUserDataAndValidateRole(String loginId, String requiredRole) {

        ApiResponse<UserData> userResponse = userClient.getUserInfo(loginId);

        if (userResponse == null || Integer.parseInt(userResponse.status()) != 200 || userResponse.getData() == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        UserData userData = userResponse.getData();

        if (!requiredRole.equals(userData.role())) {
            throw new AccessDeniedException(requiredRole + " 권한이 필요합니다.");
        }

        return userData;
    }
}
