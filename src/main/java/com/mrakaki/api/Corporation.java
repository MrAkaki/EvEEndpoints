package com.mrakaki.api;

import com.mrakaki.api.dtos.*;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.List;

import static com.mrakaki.api.Common.getMultiEntity;
import static com.mrakaki.api.Common.getSingleEntity;

public class Corporation {
    public static final String URI_PUBLIC_INFORMATION = "/corporations/{corporation_id}/";
    public static final String URI_ALLIANCE_HISTORY = "/corporations/{corporation_id}/alliancehistory/";
    public static final String URI_ICONS = "/corporations/{corporation_id}/icons/";
    public static final String URI_NPC_CORPS = "/corporations/npccorps/";

    public static Response<CorporationPublicInformation> getPublicInformation(int corporationId) {
        return getSingleEntity(CorporationPublicInformation.class, URI_PUBLIC_INFORMATION.replace("{corporation_id}", "" + corporationId));
    }

    public static Response<CorporationIcons> getIcons(int corporationId) {
        return getSingleEntity(CorporationIcons.class, URI_ICONS.replace("{corporation_id}", "" + corporationId));
    }

    public static Response<ArrayList<CorporationAllianceHistory>> getAllianceHistory(int corporationId) {
        return getMultiEntity(URI_ALLIANCE_HISTORY.replace("{corporation_id}", "" + corporationId), new ParameterizedTypeReference<ArrayList<CorporationAllianceHistory>>() {
        });
    }

    public static Response<ArrayList<Integer>> getNpcCorporations() {
        return getMultiEntity(URI_NPC_CORPS, new ParameterizedTypeReference<ArrayList<Integer>>() {
        });
    }
}
