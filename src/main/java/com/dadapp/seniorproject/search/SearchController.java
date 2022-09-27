package com.dadapp.seniorproject.search;


import com.dadapp.seniorproject.review.Review;
import javassist.bytecode.stackmap.TypeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public List<Search> fetchAllSearches() { return searchRepo.findAll(); }

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
    public ResponseEntity deleteSearch(@PathVariable("searchid") Long searchid) {
        try {
            searchRepo.deleteById(searchid);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



//    @GetMapping("/searches")
//    public ResponseEntity searchSearches(@RequestParam(value="searchTerm", required = false, defaultValue = "None") String searchTerm) {
//        try {
//            List<Search> searches = searchService.searchSearches(searchTerm);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            return new ResponseEntity(searches, headers, HttpStatus.OK);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }








    //    @GetMapping("/doctors")
//    public ResponseEntity searchDoctors(@RequestParam(value="searchTerm", required = false, defaultValue = "None") String searchTerm) throws IOException {
//        List<Search> doctors = searchDAO.getDoctor(searchTerm);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        return new ResponseEntity(doctors, headers, HttpStatus.OK);
//    }

//    @GetMapping("/search/doctors")
//    public String searchDoctors(Model model) {
//        List<Search> searchDoctors = searchService.getSearches();
//        model.addAttribute("searchDoctors", searchDoctors);
//
//        return "searchDoctors";
//    }
//
//    @GetMapping("/search/lawyers")
//    public String searchLawyers(Model model) {
//        List<Search> searchLawyers = searchService.getSearches();
//        model.addAttribute("searchLawyers", searchLawyers);
//
//        return "searchLawyers";
//    }
//
//    @GetMapping("/search/daycare")
//    public String searchDaycare(Model model) {
//        List<Search> searchDaycare = searchService.getSearches();
//        model.addAttribute("searchDaycare", searchDaycare);
//
//        return "searchDaycare";
//    }

}
