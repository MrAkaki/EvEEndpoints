package com.mrakaki.api;

import com.mrakaki.api.dtos.CharacterPortrait;
import com.mrakaki.api.dtos.CharacterPublicInformation;
import com.mrakaki.api.dtos.Response;

import static com.mrakaki.api.Common.getSingleEntity;

public class Character {
    public static final String URI_PUBLIC_INFORMATION = "/characters/{character_id}/";
    public static final String URI_PORTRAIT = "/characters/{character_id}/portrait/";

    public static Response<CharacterPublicInformation> getPublicInformation(int characterId) {
        return getSingleEntity(CharacterPublicInformation.class, URI_PUBLIC_INFORMATION.replace("{character_id}", "" + characterId));
    }

    public static Response<CharacterPortrait> getCharacterPortrait(int characterId) {
        return getSingleEntity(CharacterPortrait.class, URI_PORTRAIT.replace("{character_id}", "" + characterId));
    }
}
