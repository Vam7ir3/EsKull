package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class PaginationReq {
    private int pageNumber = 0;
    private int pageSize = 10;
    private String sortOrder ="ASC";
    private String sortBy = "id";
    private String searchTerm;

    public PaginationReq(int pageNumber, int pageSize, String sortOrder, String sortBy, String searchTerm) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
        this.searchTerm = searchTerm;
    }

    public PaginationReq() {}
}
