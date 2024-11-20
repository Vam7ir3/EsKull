package se.ki.education.nkcx.repo.specification;

import org.springframework.data.jpa.domain.Specification;
import se.ki.education.nkcx.entity.PersonEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonSpecificationBuilder {

    private final List<SearchCriteria> params;

    public PersonSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public PersonSpecificationBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public Specification<PersonEntity> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<PersonEntity>> specs = params.stream()
                .map(PersonSpecification::new)
                .collect(Collectors.toList());

        Specification<PersonEntity> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}

class PersonSpecification implements Specification<PersonEntity> {
    private final SearchCriteria criteria;

    public PersonSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<PersonEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (root.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }
    }
}