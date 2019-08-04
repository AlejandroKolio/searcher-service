package com.task.searcherservice.controller.exceptionhandler.errorresponse;

import com.devskiller.friendly_id.FriendlyId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Alexander Shakhov
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ErrorResponse {
    private final String id = FriendlyId.createFriendlyId();
    @NonNull
    private final Integer status;
    @NonNull
    private final String code;
    @NonNull
    private final String message;
}
