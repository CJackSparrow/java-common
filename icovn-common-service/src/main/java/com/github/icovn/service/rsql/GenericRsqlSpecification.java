package com.github.icovn.service.rsql;

import com.github.icovn.util.MapperUtil;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class GenericRsqlSpecification<T> implements Specification<T> {

  private String property;
  private ComparisonOperator operator;
  private List<String> arguments;

  public GenericRsqlSpecification(
      final String property, final ComparisonOperator operator, final List<String> arguments) {
    super();
    this.property = property;
    this.operator = operator;
    this.arguments = arguments;
  }

  @Override
  public Predicate toPredicate(
      final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
    final List<Object> args = castArguments(root);
    final Object argument = args.get(0);

    log.debug("(toPredicate)operator: {}", MapperUtil.toJson(operator));
    RsqlSearchOperation searchOperation = RsqlSearchOperation.getSimpleOperator(operator);
    log.debug("(toPredicate)searchOperation: {}", MapperUtil.toJson(searchOperation));
    switch (searchOperation) {
      case EQUAL: {
        log.debug("(toPredicate)argument: {}, is: {}", argument, argument.getClass());
        if (argument == null) {
          return builder.isNull(root.get(property));
        } else if (argument instanceof String && argument.toString().contains("*")) {
          return builder.like(root.get(property), argument.toString().replace('*', '%'));
        } else {
          return builder.equal(root.get(property), argument);
        }
      }
      case NOT_EQUAL: {
        if (argument instanceof String) {
          return builder.notLike(root.get(property), argument.toString().replace('*', '%'));
        } else if (argument == null) {
          return builder.isNotNull(root.get(property));
        } else {
          return builder.notEqual(root.get(property), argument);
        }
      }
      case GREATER_THAN: {
        return builder.greaterThan(root.get(property), argument.toString());
      }
      case GREATER_THAN_OR_EQUAL: {
        return builder.greaterThanOrEqualTo(root.get(property), argument.toString());
      }
      case LESS_THAN: {
        return builder.lessThan(root.get(property), argument.toString());
      }
      case LESS_THAN_OR_EQUAL: {
        return builder.lessThanOrEqualTo(root.get(property), argument.toString());
      }
      case IN:
        return root.get(property).in(args);
      case NOT_IN:
        return builder.not(root.get(property).in(args));
      case IS_NULL:
        return builder.isNull(root.get(property));
    }

    return null;
  }

  // === private
  private List<Object> castArguments(final Root<T> root) {
    final Class<? extends Object> type = root.get(property).getJavaType();

    return arguments.stream()
        .map(
            arg -> {
              if (type.equals(Boolean.class)) {
                return Boolean.parseBoolean(arg);
              } else if (type.equals(Integer.class)) {
                return Integer.parseInt(arg);
              } else if (type.equals(Long.class)) {
                return Long.parseLong(arg);
              } else {
                return arg;
              }
            })
        .collect(Collectors.toList());
  }
}
