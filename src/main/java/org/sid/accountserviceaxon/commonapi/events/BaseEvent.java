package org.sid.accountserviceaxon.commonapi.events;

import lombok.Getter;

public class BaseEvent<T> {
    @Getter private T id;

    public BaseEvent(T id) {
        this.id = id;
    }
}
