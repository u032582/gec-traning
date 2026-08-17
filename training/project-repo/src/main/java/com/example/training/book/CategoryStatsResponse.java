package com.example.training.book;

public class CategoryStatsResponse {
    private final String category;
    private final Integer titleCount;
    private final Integer totalCount;
    private final Integer availableCount;

    public CategoryStatsResponse(String category, Integer titleCount, Integer totalCount, Integer availableCount){
        this.category = category;
        this.titleCount = titleCount;
        this.totalCount = totalCount;
        this.availableCount = availableCount;
    }

    public String getCategory(){
        return category;
    }

    public Integer getTitleCount(){
        return titleCount;
    }

    public Integer getTotalCount(){
        return totalCount;
    }

    public Integer getAvailableCount(){
        return availableCount;
    }
}
