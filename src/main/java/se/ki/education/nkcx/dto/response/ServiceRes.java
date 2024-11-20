package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRes {
    private boolean status;
    private String message;

    private Integer startPosition;
    private Integer endPosition;
    private Long totalRecord;
    private Integer totalPage;
    private Integer pageSize;
    private Integer currentPage;

    private List<Object> response;
    private Map<String, Object> data;

    public ServiceRes() {
    }

    public ServiceRes(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ServiceRes setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public ServiceRes setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceRes setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public ServiceRes setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
        return this;
    }

    public ServiceRes setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
        return this;
    }

    public ServiceRes setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public ServiceRes setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ServiceRes setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public ServiceRes setResponse(List<Object> response) {
        this.response = response;
        return this;
    }

    public ServiceRes addData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }
}
