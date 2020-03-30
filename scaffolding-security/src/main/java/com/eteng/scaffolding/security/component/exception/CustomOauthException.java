package com.eteng.scaffolding.security.component.exception;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
@Deprecated
class CustomOauthException extends OAuth2Exception {

    private final CommonResult result;

    CustomOauthException(OAuth2Exception oAuth2Exception) {
        super(oAuth2Exception.getSummary(), oAuth2Exception);
        this.result = CommonResult.forbidden();
    }
}