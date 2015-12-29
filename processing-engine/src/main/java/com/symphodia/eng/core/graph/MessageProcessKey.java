package com.symphodia.eng.core.graph;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.symphodia.spring.common.graph.ProcessKey;
import com.symphodia.eng.integrations.servicebus.dto.MessageType;

public class MessageProcessKey implements ProcessKey {
    private MessageType messageType;

    public MessageProcessKey(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageProcessKey that = (MessageProcessKey) o;

        return Objects.equal(this.messageType, that.messageType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.messageType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(messageType).toString();
    }
}
