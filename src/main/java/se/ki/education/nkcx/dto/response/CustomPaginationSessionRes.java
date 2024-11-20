package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomPaginationSessionRes<Res> {
    private Integer startPosition;
    private Integer endPosition;
    private Long totalRecord;
    private Integer totalPage;
    private Integer pageSize;
    private Integer currentPage;
    private Map<String, List<Res>> data;

    public CustomPaginationSessionRes<Res> setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public CustomPaginationSessionRes<Res> setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
        return this;
    }

    public CustomPaginationSessionRes<Res> setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
        return this;
    }

    public CustomPaginationSessionRes<Res> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public CustomPaginationSessionRes<Res> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public CustomPaginationSessionRes<Res> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public CustomPaginationSessionRes<Res> setData(Map<String, List<Res>>data) {
        this.data = data;
        return this;
    }
}
