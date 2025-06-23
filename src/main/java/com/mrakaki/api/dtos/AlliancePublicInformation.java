package com.mrakaki.api.dtos;

public record AlliancePublicInformation(int creator_corporation_id, int creator_id, String date_founded,
                                        Integer executor_corporation_id, Integer faction_id, String name,
                                        String ticker) {
}
