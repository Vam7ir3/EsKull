package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomPaginationRes<Res> {
    private Integer startPosition;
    private Integer endPosition;
    private Long totalRecord;
    private Integer totalPage;
    private Integer pageSize;
    private Integer currentPage;
    private Map<LocalDate, List<Res>> data;

    public CustomPaginationRes<Res> setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public CustomPaginationRes<Res> setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
        return this;
    }

    public CustomPaginationRes<Res> setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
        return this;
    }

    public CustomPaginationRes<Res> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public CustomPaginationRes<Res> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public CustomPaginationRes<Res> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public CustomPaginationRes<Res> setData(Map<LocalDate, List<Res>>data) {
        this.data = data;
        return this;
    }
}