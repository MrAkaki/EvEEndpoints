package com.mrakaki.api.ccp;

import com.mrakaki.api.dtos.AllianceIcons;
import com.mrakaki.api.dtos.AlliancePublicInformation;
import com.mrakaki.api.dtos.Response;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;

public class Alliance {
    public static final String URI_ALL_ALLIANCES = "/alliances/";
    public static final String URI_PUBLIC_INFORMATION = "/alliances/{alliance_id}/";
    public static final String URI_ALLIANCE_CORPORATIONS = "/alliances/{alliance_id}/corporations/";
    public static final String URI_ALLIANCE_ICONS = "/alliances/{alliance_id}/icons/";

    public static Response<AlliancePublicInformation> getPublicInformation(int allianceId) {
        return Common.getSingleEntity(AlliancePublicInformation.class, URI_PUBLIC_INFORMATION.replace("{alliance_id}", "" + allianceId));
    }

    public static Response<ArrayList<Integer>> getAllAlliances() {
        return Common.getMultiEntity(URI_ALL_ALLIANCES, new ParameterizedTypeReference<ArrayList<Integer>>() {
        });
    }

    public static Response<ArrayList<Integer>> getAllianceCorporations(int allianceId) {
        return Common.getMultiEntity(URI_ALLIANCE_CORPORATIONS.replace("{alliance_id}", "" + allianceId), new ParameterizedTypeReference<ArrayList<Integer>>() {
        });
    }

    public static Response<AllianceIcons> getIcons(int allianceId) {
        return Common.getSingleEntity(AllianceIcons.class, URI_ALLIANCE_ICONS.replace("{alliance_id}", "" + allianceId));
    }
}
