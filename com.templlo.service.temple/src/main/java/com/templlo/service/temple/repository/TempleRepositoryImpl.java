package com.templlo.service.temple.repository;

import com.templlo.service.temple.model.Temple;
import com.templlo.service.temple.repository.elasticsearch.TempleElasticSearchRepository;
import com.templlo.service.temple.repository.jpa.TempleCustomRepository;
import com.templlo.service.temple.repository.jpa.TempleJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TempleRepositoryImpl implements TempleRepository{

    private final TempleJpaRepository templeJpaRepository;
    private final TempleCustomRepository templeCustomRepository;
    private final TempleElasticSearchRepository templeElasticSearchRepository;

    public TempleRepositoryImpl(TempleJpaRepository templeJpaRepository, TempleCustomRepository templeCustomRepository, TempleElasticSearchRepository templeElasticSearchRepository) {
        this.templeJpaRepository = templeJpaRepository;
        this.templeCustomRepository = templeCustomRepository;
        this.templeElasticSearchRepository = templeElasticSearchRepository;

    }

    @Override
    public Page<Temple> findTemplesByRegion(String region, Pageable pageable) {
        return templeCustomRepository.findTemplesByRegion(region, pageable);
    }

    @Override
    public Optional<Temple> findById(UUID templeId) {
        return templeJpaRepository.findById(templeId);
    }

    @Override
    public Temple save(Temple temple) {
        return templeJpaRepository.save(temple);
    }

    @Override
    public void delete(UUID templeId) {
        // 1. 데이터베이스에서 Temple 삭제
        templeJpaRepository.deleteById(templeId);

        // 2. Elasticsearch에서 해당 Temple 데이터 삭제
        templeElasticSearchRepository.deleteById(templeId.toString());
    }


}
