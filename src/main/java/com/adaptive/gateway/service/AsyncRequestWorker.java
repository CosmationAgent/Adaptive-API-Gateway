package com.adaptive.gateway.service;

import com.adaptive.gateway.models.SubmitPremiumQueueItem;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AsyncRequestWorker implements Runnable {

    private final AsyncRequestQueueService queueService;
    private final Thread workerThread;

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
                queueService.markProcessing(item.getRequestId());

                // Simulate processing (replace with real logic)
                Thread.sleep(1000); // Simulate work

                queueService.markDone(item.getRequestId());
            } catch (Exception e) {
                // Optionally log and mark failed
                // queueService.markFailed(item.getRequestId());
                // e.printStackTrace();
            }
        }
    }
}