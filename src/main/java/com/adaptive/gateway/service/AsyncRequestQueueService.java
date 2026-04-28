package com.adaptive.gateway.service;

import com.adaptive.gateway.models.SubmitPremiumQueueItem;
import com.adaptive.gateway.models.QueuePriorityType;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class AsyncRequestQueueService {
    public enum Status { QUEUED, PROCESSING, DONE, FAILED }

    private final PriorityBlockingQueue<SubmitPremiumQueueItem> queue = new PriorityBlockingQueue<>();
    private final ConcurrentHashMap<String, Status> statusMap = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public String submit(String clientId, String data, QueuePriorityType priority) {
        String requestId = UUID.randomUUID().toString();
        long seq = sequence.incrementAndGet();
        SubmitPremiumQueueItem item = new SubmitPremiumQueueItem(
                requestId, clientId, data, priority, seq, LocalDateTime.now()
        );
        statusMap.put(requestId, Status.QUEUED);
        queue.offer(item);
        return requestId;
    }

    public SubmitPremiumQueueItem takeNext() throws InterruptedException {
        return queue.take();
    }

    public void markProcessing(String requestId) {
        statusMap.put(requestId, Status.PROCESSING);
    }

    public void markDone(String requestId) {
        statusMap.put(requestId, Status.DONE);
    }

    public void markFailed(String requestId) {
        statusMap.put(requestId, Status.FAILED);
    }

    public Status getStatus(String requestId) {
        return statusMap.get(requestId);
    }
}