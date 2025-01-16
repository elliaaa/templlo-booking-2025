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

    public TempleRepositoryImpl(TempleJpaRepository templeJpaRepository, TempleCustomRepository templeCustomRepository) {
        this.templeJpaRepository = templeJpaRepository;
        this.templeCustomRepository = templeCustomRepository;
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
        templeJpaRepository.deleteById(templeId);
    }


}
