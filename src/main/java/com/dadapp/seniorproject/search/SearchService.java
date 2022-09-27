package com.dadapp.seniorproject.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class SearchService implements ISearchService {

    private final static Logger log = LoggerFactory.getLogger(SearchService.class);
    private final SearchRepo searchRepo;

    public SearchService(SearchRepo searchRepo) {
        this.searchRepo = searchRepo;
    }

    public List<Search> getSearches() {
        return searchRepo.findAll();
    }
}
