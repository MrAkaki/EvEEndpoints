package com.mrakaki.api.ccp;

import com.mrakaki.api.dtos.MarketStructureOrder;
import com.mrakaki.api.dtos.Response;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;

public class Market {
    public static final String URL_STRUCTURE_ORDERS = "/markets/structures/{structure_id}?page={page}";

    public static Response<ArrayList<MarketStructureOrder>> getStructureOrders(int structureId, int page, String token) {
        return Common.getMultiEntity(
                URL_STRUCTURE_ORDERS
                        .replace("{structure_id}", "" + structureId)
                        .replace("{page}", "" + page),
                new ParameterizedTypeReference<ArrayList<MarketStructureOrder>>() {
                },
                token);
    }
}
