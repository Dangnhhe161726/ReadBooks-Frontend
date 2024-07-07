package com.example.frontend.responses;

import com.example.frontend.models.Book;

import java.util.List;

public class PaginationResponse {
    private List<Book> content;
    private PageableResponse pageable;
    private boolean last;
    private int totalElements;
    private int totalPages;
    private int size;
    private int number;
    private SortResponse sort;
    private boolean first;
    private int numberOfElements;
    private boolean empty;

    public PaginationResponse() {
    }

    public List<Book> getContent() {
        return content;
    }

    public void setContent(List<Book> content) {
        this.content = content;
    }

    public PageableResponse getPageable() {
        return pageable;
    }

    public void setPageable(PageableResponse pageable) {
        this.pageable = pageable;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SortResponse getSort() {
        return sort;
    }

    public void setSort(SortResponse sort) {
        this.sort = sort;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
