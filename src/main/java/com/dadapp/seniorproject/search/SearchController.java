package com.dadapp.seniorproject.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(path = "search")
public class SearchController {

    private final static Logger log = LoggerFactory.getLogger(SearchController.class);
    private final SearchRepo searchRepo;
    private final SearchService searchService;

    public SearchController(SearchService searchService, SearchRepo searchRepo) {
        this.searchService = searchService;
        this.searchRepo = searchRepo;
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Search> fetchAllSearches() {
        return searchRepo.findAll();
    }

    @GetMapping("/searchlist")
    public String searchList(Model model) {
        model.addAttribute("listSearches", searchService.getSearches());
        model.addAttribute("text", "Search List");
        return "searchlist";
    }

    @ModelAttribute("allSearches")
    public List<Search> allSearches() {
        List<Search> searchList = searchService.getSearches();
        return searchList;
    }

    @DeleteMapping("/search/{searchid}/")
    public ResponseEntity<ResponseStatus> deleteSearch(@PathVariable("searchid") Long searchid) {
        try {
            searchRepo.deleteById(searchid);
            return new ResponseEntity<ResponseStatus>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
