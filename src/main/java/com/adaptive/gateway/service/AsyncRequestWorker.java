package com.adaptive.gateway.service;

import com.adaptive.gateway.models.SubmitPremiumQueueItem;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AsyncRequestWorker implements Runnable {

    private final AsyncRequestQueueService queueService;
    private final Thread workerThread;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker(3, 10000); // Example: 3 failures, 10s timeout

    public AsyncRequestWorker(AsyncRequestQueueService queueService) {
        this.queueService = queueService;
        this.workerThread = new Thread(this, "AsyncRequestWorker");
        this.workerThread.setDaemon(true); // Optional: won't block JVM shutdown
    }

    @PostConstruct
    public void startWorker() {
        workerThread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                SubmitPremiumQueueItem item = queueService.takeNext();

                if(!circuitBreaker.allowRequest()) {
                    queueService.markFailed(item.getRequestId());
                    continue;
                }

                queueService.markProcessing(item.getRequestId());

                // Simulate processing (replace with real logic)
                Thread.sleep(1000); // Simulate work

                boolean success = Math.random() > 0.5; // 50% chance to fail
                if (success) {
                    queueService.markDone(item.getRequestId());
                    circuitBreaker.recordSuccess();
                } else {
                    queueService.markFailed(item.getRequestId());
                    circuitBreaker.recordFailure();
                }
            } catch (Exception e) {
                // Optionally log and mark failed
                // queueService.markFailed(item.getRequestId());
                // circuitBreaker.recordFailure();
                // e.printStackTrace();
            }
        }
    }
}