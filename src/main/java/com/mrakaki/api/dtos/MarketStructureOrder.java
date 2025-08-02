package com.mrakaki.api.dtos;

public record MarketStructureOrder(
        int duration, boolean is_buy_order, String issued,
        int location_id, int min_volume, int order_id,
        float price, String range, int type_id,
        int volume_remain, int volume_total
) {
}
