package com.templlo.service.temple.repository.elasticsearch;

import com.templlo.service.temple.model.SearchTemple;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TempleElasticSearchRepository extends ElasticsearchRepository<SearchTemple, String> {
}
