package com.templlo.service.temple.service.elasticsearch;

import com.templlo.service.temple.model.SearchTemple;
import com.templlo.service.temple.model.Temple;
import com.templlo.service.temple.repository.elasticsearch.TempleElasticSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticSearchSynchronizer {

    private final TempleElasticSearchRepository templeElasticSearchRepository;

    public void saveTemple(Temple temple) {
        SearchTemple searchTemple = SearchTemple.from(temple);
        templeElasticSearchRepository.save(searchTemple);
    }


    public void deleteTemple(String templeId) {
        templeElasticSearchRepository.deleteById(templeId);
    }
}
