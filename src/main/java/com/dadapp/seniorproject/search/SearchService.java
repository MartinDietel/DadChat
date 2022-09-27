package com.dadapp.seniorproject.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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


//    @Override
//    public List<Search> searchSearches(String searchTerm) throws IOException {
//        return (List<Search>) searchDAO.searchSearches(searchTerm);
//    }
}
