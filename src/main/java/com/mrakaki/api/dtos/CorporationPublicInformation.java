package com.mrakaki.api.dtos;

public record CorporationPublicInformation(Integer alliance_id, int ceo_id, int creator_id, String date_founded,
                                           String description, Integer faction_id, Integer home_station_id,
                                           int member_count, String name, Integer shares, float tax_rate, String ticker,
                                           String url, Boolean war_eligible) {
}
