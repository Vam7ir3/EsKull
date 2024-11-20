package se.ki.education.nkcx.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    private final String errorCode;
    private String fieldName;
    private Integer row;

    public CustomException(String errorCode) {
        this(errorCode, null, null);
    }

    public CustomException(String errorCode, Integer row) {
        this(errorCode, null, row);
    }

    public CustomException(String errorCode, String fieldName) {
        this(errorCode, fieldName, null);
    }

    public CustomException(String errorCode, String fieldName, Integer row) {
        this.errorCode = errorCode;
        this.fieldName = fieldName;
        this.row = row;
    }
}
