package se.ki.education.nkcx.repo.specification;

import se.ki.education.nkcx.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserSpecificationBuilder {

    private final List<SearchCriteria> params;

    public UserSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public UserSpecificationBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public Specification<UserEntity> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<UserEntity>> specs = params.stream()
                .map(UserSpecification::new)
                .collect(Collectors.toList());

        Specification<UserEntity> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}

class UserSpecification implements Specification<UserEntity> {

    private SearchCriteria criteria;

    public UserSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}