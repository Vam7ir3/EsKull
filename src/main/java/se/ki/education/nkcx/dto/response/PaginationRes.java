package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationRes<Res> {
    private Integer startPosition;
    private Integer endPosition;
    private Long totalRecord;
    private Integer totalPage;
    private Integer pageSize;
    private Integer currentPage;
    private List<Res> data;

    public PaginationRes<Res> setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public PaginationRes<Res> setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
        return this;
    }

    public PaginationRes<Res> setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
        return this;
    }

    public PaginationRes<Res> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public PaginationRes<Res> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PaginationRes<Res> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public PaginationRes<Res> setData(List<Res> data) {
        this.data = data;
        return this;
    }
}
