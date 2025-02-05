/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.jobgenerator;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Producer of jobs that will be processed by GPUs.
 * 
 * The producer continuously places jobs from his internal queue into a 
 * shared queue according to the arrival time specified for each job. 
 * 
 * IMPORTANT NOTE: The time unit of arrivals of individual jobs to the shared 
 * queue is 1 minute.
 * 
 * @author Michal Konopa
 */
public final class JobProducer implements Runnable {
    // for logging class events
    private static final Logger logger = LogManager.getLogger(JobProducer.class);
    
    
    private final Queue<Job> jobs;
    
    // reference to shared queue of jobs
    private final BlockingQueue<Job> blockingQueue;
    
    
    /**
     * Creates new instance of job producer with specified internal queue of jobs
     * and reference to blocking queue, which is shared with job consumer(s).
     * The producer continuously places jobs from his internal queue into a 
     * shared queue according to the arrival time specified for each job.
     * 
     * @param jobs producer internal source queue of jobs
     * @param blockingQueue 
     */
    public JobProducer(Queue<Job> jobs, BlockingQueue<Job> blockingQueue) {
        this.jobs = jobs;
        this.blockingQueue = blockingQueue;
    }
    
    /**
     * Produces job object and puts it into the shared queue.
     * @return 
     */
    private void produce() {
        while (!jobs.isEmpty()) {
            Job job = jobs.poll();
            double jobArrivalTime = job.getArrivalTime();
            
            try {
                blockingQueue.put(job);
            } catch (InterruptedException e) {
                logger.error("Error while putting job into queue. Producer will be terminated.");
                return;
            }
            
            if ( jobs.isEmpty() ) {
                logger.info("Generation of jobs complete.");
                return;
            }
            
            double sleepTime = jobs.peek().getArrivalTime() - jobArrivalTime;
            logger.info("Producer sleeping time [in seconds]: " + (sleepTime * 60));
            
            try {
                Thread.sleep(Math.round(sleepTime * 60 * 1000));
            } catch (InterruptedException ex) {
                logger.error("Producer prematurely interrupted. Producer will be terminated.");
                return;
            }
        }
        
        logger.info("Generation of jobs complete.");
    }
    
    
    @Override
    public void run() {
        produce();
    }
    
}
