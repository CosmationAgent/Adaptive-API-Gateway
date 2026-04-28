package com.adaptive.gateway.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubmitPremiumQueueItem implements Comparable<SubmitPremiumQueueItem> {

    private final String requestId;
    private final String clientId;
    private final String data;
    private final QueuePriorityType priority;
    private final long sequenceNumber;
    private final LocalDateTime createdAt;

    public SubmitPremiumQueueItem(
            String requestId,
            String clientId,
            String data,
            QueuePriorityType priority,
            long sequenceNumber,
            LocalDateTime createdAt
    ) {
        this.requestId = Objects.requireNonNull(requestId, "requestId must not be null");
        this.clientId = Objects.requireNonNull(clientId, "clientId must not be null");
        this.data = data;
        this.priority = Objects.requireNonNull(priority, "priority must not be null");
        this.sequenceNumber = sequenceNumber;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public String getRequestId() {
        return requestId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getData() {
        return data;
    }

    public QueuePriorityType getPriority() {
        return priority;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public int compareTo(SubmitPremiumQueueItem other) {
        int thisRank = (this.priority == QueuePriorityType.PREMIUM) ? 0 : 1;
        int otherRank = (other.priority == QueuePriorityType.PREMIUM) ? 0 : 1;

        if (thisRank != otherRank) {
            return Integer.compare(thisRank, otherRank);
        }

        return Long.compare(this.sequenceNumber, other.sequenceNumber);
    }
}