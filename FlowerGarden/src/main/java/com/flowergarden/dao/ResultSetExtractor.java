package com.flowergarden.dao;

import java.sql.ResultSet;
import java.util.List;

@FunctionalInterface
public interface ResultSetExtractor<T> {
    List<T> extract(ResultSet resultSet);
}
