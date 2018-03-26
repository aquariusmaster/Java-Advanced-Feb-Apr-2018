package com.flowergarden.dao.impl;

import com.flowergarden.dao.ResultSetExtractor;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.domain.bouquet.MarriedBouquet;
import com.flowergarden.domain.flowers.Chamomile;
import com.flowergarden.domain.flowers.Rose;
import com.flowergarden.domain.flowers.Tulip;
import com.flowergarden.domain.properties.FreshnessInteger;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GeneralBouquetExtractor implements ResultSetExtractor<GeneralBouquet> {

    public List<GeneralBouquet> extract(ResultSet rs) {

        Map<Integer, GeneralBouquet> extractedBouquets = new HashMap<>();

        try {

            while (rs.next()) {

                int bouquetId = rs.getInt("bouquet_id");
                if (bouquetId != 0) {

                    String bouquetType = rs.getString("bouquet_name");
                    float assemble_price = rs.getFloat("assemble_price");

                    GeneralBouquet bouquet = extractedBouquets.get(bouquetId);

                    if (bouquet == null) {
                        switch (bouquetType) {

                            case "married":
                                MarriedBouquet marriedBouquet = new MarriedBouquet();
                                marriedBouquet.setId(bouquetId);
                                marriedBouquet.setAssemblePrice(assemble_price);
                                bouquet = marriedBouquet;
                                break;
                            default:
                                throw new RuntimeException("Cannot get bouquet: " + bouquetType + " type does not support");
                        }
                    }

                    int id = rs.getInt("flower_id");
                    float price = rs.getFloat("price");
                    int length = rs.getInt("length");
                    FreshnessInteger freshness = new FreshnessInteger(rs.getInt("freshness"));
                    String flowerType = rs.getString("flower_name");

                    switch (flowerType) {

                        case "rose":
                            Rose rose = new Rose();
                            rose.setId(id);
                            rose.setPrice(price);
                            rose.setLength(length);
                            rose.setFreshness(freshness);
                            Boolean spike = rs.getBoolean("spike");
                            rose.setSpike(spike);
                            rose.setBouquet(bouquet);
                            bouquet.addFlower(rose);
                            break;
                        case "chamomile":
                            Chamomile chamomile = new Chamomile();
                            Integer petals = rs.getInt("petals");
                            chamomile.setPetals(petals);
                            chamomile.setId(id);
                            chamomile.setPrice(price);
                            chamomile.setLength(length);
                            chamomile.setFreshness(freshness);
                            chamomile.setBouquet(bouquet);
                            bouquet.addFlower(chamomile);
                            break;
                        case "tulip":
                            Tulip tulip = new Tulip();
                            tulip.setId(id);
                            tulip.setPrice(price);
                            tulip.setLength(length);
                            tulip.setFreshness(freshness);
                            tulip.setBouquet(bouquet);
                            bouquet.addFlower(tulip);
                            break;
                        default:
                            throw new RuntimeException("Cannot get flower: " + flowerType + " type does not support");
                    }
                    extractedBouquets.put(bouquetId, bouquet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcHandler.closeResultSetAndStatementAndConnection(rs);
        }

        return new ArrayList<>(extractedBouquets.values());
    }
}
