package com.mrakaki.api.dtos;

public record CharacterPublicInformation(Integer alliance_id, String birthday, int bloodline_id, int corporation_id,
                                         String description, Integer faction_id, String gender, String name,
                                         int race_id,
                                         float security_status, String title) {
}
