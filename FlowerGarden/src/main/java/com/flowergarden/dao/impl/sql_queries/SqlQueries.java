package com.flowergarden.dao.impl.sql_queries;

public class SqlQueries {

    public final static String SELECT_FLOWER_JOIN_BOUQUET =
            "select" +
            " b.id bouquet_id," +
            " b.name bouquet_name," +
            " b.assemble_price," +
            " f.id flower_id," +
            " f.name flower_name," +
            " f.length length," +
            " f.freshness," +
            " f.price," +
            " f.petals," +
            " f.spike " +
            "from flower f" +
            " left join bouquet b" +
            " on f.bouquet_id = b.id";

    public final static String SELECT_BOUQUET_JOIN_FLOWER =
            "select" +
            " b.id bouquet_id," +
            " b.name bouquet_name," +
            " b.assemble_price," +
            " f.id flower_id," +
            " f.name flower_name," +
            " f.length length," +
            " f.freshness," +
            " f.price," +
            " f.petals," +
            " f.spike " +
            "from bouquet b" +
            " left join flower f" +
            " on f.bouquet_id = b.id";


}
