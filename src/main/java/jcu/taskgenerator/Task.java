/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.taskgenerator;

/**
 * Properties of generated task.
 * 
 * @author Michal Konopa
 */
final class Task {
    static int NO_DEADLINE = -1;
    
    private final int priority;
    private final boolean isStopable;
    private final boolean isMigrable;
    private final int deadline;
    private final int[] timeslices;
    private final int cudaCoresNumber;
    private final double arrivalTime;

    
    public Task(
            int priority, 
            boolean isStopable, 
            boolean isMigrable, 
            int deadline, 
            int[] timeslices,
            int cudaCoresNumber,
            double arrivalTime
    ) {
        this.priority = priority;
        this.isStopable = isStopable;
        this.isMigrable = isMigrable;
        this.deadline = deadline;
        this.timeslices = timeslices;
        this.cudaCoresNumber = cudaCoresNumber;
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return the priority
     */
    int getPriority() {
        return priority;
    }

    /**
     * @return the isStopable
     */
    boolean isStopable() {
        return isStopable;
    }

    /**
     * @return the isMigrable
     */
    boolean isMigrable() {
        return isMigrable;
    }

    /**
     * @return the deadline
     */
    int getDeadline() {
        return deadline;
    }

    /**
     * @return the timeslices
     */
    int[] getTimeslices() {
        return timeslices;
    }

    /**
     * @return the arrival time
     */
    double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @return the number of used CUDA cores 
     */
    int getCudaCoresNumber() {
        return cudaCoresNumber;
    }
    
    
}
