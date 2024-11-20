package se.ki.education.nkcx.repo.specification;

public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private boolean orPredicate;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, String operation, Object value, boolean orPredicate) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.orPredicate = orPredicate;
    }

    public String getKey() {
        return key;
    }

    public SearchCriteria setKey(String key) {
        this.key = key;
        return this;
    }

    public String getOperation() {
        return operation;
    }

    public SearchCriteria setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public SearchCriteria setValue(Object value) {
        this.value = value;
        return this;
    }

    public boolean isOrPredicate() {
        return orPredicate;
    }

    public SearchCriteria setOrPredicate(boolean orPredicate) {
        this.orPredicate = orPredicate;
        return this;
    }
}
