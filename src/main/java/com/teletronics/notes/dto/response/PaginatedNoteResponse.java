package com.teletronics.notes.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedNoteResponse<T> {
    private List<T> notes;
    private long totalNotes;
    private int totalPages;
    private int currentPage;

    public PaginatedNoteResponse(List<T> notes, long totalNotes, int totalPages, int currentPage) {
        this.notes = notes;
        this.totalNotes = totalNotes;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }
}
