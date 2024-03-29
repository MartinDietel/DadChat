package com.dadapp.seniorproject.model;

public class SearchNavCriteria {

    public String search;
    public String searchType;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public SearchNavCriteria(String search, String searchType) {
        this.search = search;
        this.searchType = searchType;
    }

}
