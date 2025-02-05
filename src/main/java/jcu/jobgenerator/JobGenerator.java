/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.jobgenerator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Generator of jobs.
 *
 * Jobs are generated according to specified configuration settings by the
 * usage of Java standard classes.
 *
 * @author Michal Konopa
 */
final class JobGenerator {

    // randomizer
    private static Random randomizer;
    
    // last job arrival time
    private static double lastJobArrivalTime = 0;
    
    // returns the job's priority
    private static int getPriority(ConfigSettings.PriorityConfigSettings prioritySettings) {
        switch (prioritySettings.getType()) {
            case Fixed -> {
                return prioritySettings.getPriority();
            }
            case Random -> {
                ConfigSettings.PriorityConfigSettings.DistributionType distribType = prioritySettings.getDistributionType();
                switch (distribType) {
                    case Uniform -> {
                        ConfigSettings.UniformDistributionParams uniformParams = prioritySettings.getUniformDistributionParams();
                        return randomizer.nextInt(uniformParams.getLowerBound(), uniformParams.getUpperBound() + 1);
                    }
                    case Normal -> {
                        ConfigSettings.NormalDistributionParams normalDistributionParams
                                = prioritySettings.getNormalDistributionParams();
                        return Math.max(1,
                                Math.round(
                                        (int) randomizer.nextGaussian(
                                                normalDistributionParams.getMean(),
                                                normalDistributionParams.getSd()
                                        )
                                )
                        );
                    }
                    default ->
                        throw new IllegalStateException("Unsupported type of priority distribution type: " + distribType);
                }
            }
            default ->
                throw new IllegalStateException("Unsupported type of priority config type: " + prioritySettings.getType());
        }
    }

    private static boolean getStoppable(ConfigSettings.StoppabilityConfigSettings stoppabilityConfigSettings) {
        if (stoppabilityConfigSettings.getType() == ConfigSettings.StoppabilityConfigSettings.Type.Fixed) {
            return stoppabilityConfigSettings.isStoppable();
        }
        return randomizer.nextFloat() < stoppabilityConfigSettings.getStoppableProbability();
    }

    private static boolean getMigrable(ConfigSettings.MigrabilityConfigSettings migrabilityConfigSettings) {
        if (migrabilityConfigSettings.getType() == ConfigSettings.MigrabilityConfigSettings.Type.Fixed) {
            return migrabilityConfigSettings.isMigrable();
        }
        return randomizer.nextFloat() < migrabilityConfigSettings.getMigrableProbability();
    }

    private static int getDeadline(ConfigSettings.DeadlineConfigSettings deadlineSettings) {
        int deadline = 0;

        ConfigSettings.DeadlineConfigSettings.Type deadlineType = deadlineSettings.getType();
        switch (deadlineType) {
            case Fixed ->
                deadline = deadlineSettings.getDeadline();
            case NotDefined ->
                deadline = Job.NO_DEADLINE;
            case Random -> {
                ConfigSettings.DeadlineConfigSettings.DistributionType deadlineDistributionType
                        = deadlineSettings.getDistributionType();
                switch (deadlineDistributionType) {
                    case Uniform -> {
                        ConfigSettings.UniformDistributionParams uniformDistributionParams
                                = deadlineSettings.getUniformDistributionParams();
                        deadline = randomizer.nextInt(
                                uniformDistributionParams.getLowerBound(),
                                uniformDistributionParams.getUpperBound() + 1
                        );
                    }
                    case Normal -> {
                        ConfigSettings.NormalDistributionParams normalDistributionParams
                                = deadlineSettings.getNormalDistributionParams();
                        Math.max(1,
                                Math.round(
                                        (int) randomizer.nextGaussian(
                                                normalDistributionParams.getMean(),
                                                normalDistributionParams.getSd()
                                        )
                                )
                        );
                    }
                    default -> {
                        throw new IllegalStateException("Unsupported type of deadline distribution type: " + deadlineDistributionType);
                    }
                }
            }
            default -> {
                throw new IllegalStateException("Unsupported type of deadline type: " + deadlineType);
            }

        }

        return deadline;
    }

    // returns number of timeslices
    private static int getTimeslicesNum(
            ConfigSettings.MaxTimeslicesNumberConfigSettings maxTimeslicesNumberSettings
    ) {
        if (maxTimeslicesNumberSettings.getType() == ConfigSettings.MaxTimeslicesNumberConfigSettings.Type.Fixed) {
            return maxTimeslicesNumberSettings.getMaxTimeslicesNum();
        }

        switch (maxTimeslicesNumberSettings.getDistributionType()) {
            case Uniform -> {
                ConfigSettings.UniformDistributionParams uniformParams = maxTimeslicesNumberSettings.getUniformDistributionParams();
                return randomizer.nextInt(
                        uniformParams.getLowerBound(), uniformParams.getUpperBound() + 1
                );
            }
            case Normal -> {
                ConfigSettings.NormalDistributionParams normalParams = maxTimeslicesNumberSettings.getNormalDistributionParams();
                return Math.max(1,
                        Math.round((int) randomizer.nextGaussian(
                                normalParams.getMean(),
                                normalParams.getSd()
                        )
                        )
                );
            }
            default ->
                throw new IllegalStateException(
                        "Unsupported type of maximal timeslices number distribution: "
                        + maxTimeslicesNumberSettings.getDistributionType()
                );
        }
    }

    private static void fillFixedValueTimeslaces(int[] timeslaces, int ramUsage) {
        for (int tsId = 0; tsId < timeslaces.length; tsId++) {
            timeslaces[tsId] = ramUsage;
        }
    }

    private static void fillRandomIndependentTimeslaces(
            int[] timeslaces,
            ConfigSettings.MaxRamUsageConfigSettings maxRamUsageConfigSettings
    ) {
        switch (maxRamUsageConfigSettings.getDistributionType()) {
            case Uniform -> {
                ConfigSettings.UniformDistributionParams uniformParams = maxRamUsageConfigSettings.getUniformDistributionParams();
                for (int tsId = 0; tsId < timeslaces.length; tsId++) {
                    timeslaces[tsId] = randomizer.nextInt(
                            uniformParams.getLowerBound(),
                            uniformParams.getUpperBound()
                    );
                }
            }
            case Normal -> {
                ConfigSettings.NormalDistributionParams normalParams = maxRamUsageConfigSettings.getNormalDistributionParams();
                for (int tsId = 0; tsId < timeslaces.length; tsId++) {
                    timeslaces[tsId] = Math.abs(
                            Math.round(
                                    (int) randomizer.nextGaussian(
                                            normalParams.getMean(),
                                            normalParams.getSd()
                                    )
                            )
                    );
                }
            }
            default ->
                throw new IllegalArgumentException("Unsupported distribution type of maximal RAM usage: " + maxRamUsageConfigSettings.getDistributionType());
        }
    }

    private static void fillRandomDependentOnPreviousTimeslaces(
            int[] timeslices,
            ConfigSettings.MaxRamUsageConfigSettings maxRamUsageConfigSettings
    ) {
        // filling the first timeslice
        ConfigSettings.MaxRamUsageConfigSettings.DistributionType distrType = maxRamUsageConfigSettings.getDistributionType();
        switch (distrType) {
            case Uniform -> {
                ConfigSettings.UniformDistributionParams uniformParams = maxRamUsageConfigSettings.getUniformDistributionParams();
                timeslices[0] = randomizer.nextInt(
                        uniformParams.getLowerBound(),
                        uniformParams.getUpperBound()
                );
            }
            case Normal -> {
                ConfigSettings.NormalDistributionParams normalParams = maxRamUsageConfigSettings.getNormalDistributionParams();
                timeslices[0] = Math.abs(
                        Math.round(
                                (int) randomizer.nextGaussian(
                                        normalParams.getMean(),
                                        normalParams.getSd()
                                )
                        )
                );
            }
            default ->
                throw new IllegalArgumentException("Unsupported distribution type of maximal RAM usage: " + distrType);
        }

        // filling the next timeslices
        for (int tmId = 1; tmId < timeslices.length; tmId++) {
            switch (distrType) {
                case Uniform -> {
                    ConfigSettings.UniformDistributionParams uniformParams = maxRamUsageConfigSettings.getUniformDistributionParams();
                    int intervalLen = uniformParams.getUpperBound() - uniformParams.getLowerBound();
                    int lowerBound = timeslices[tmId - 1] - intervalLen/2;
                    int upperBound = timeslices[tmId - 1] + intervalLen/2;
                    
                    timeslices[tmId] = randomizer.nextInt(
                            Math.max(0,lowerBound),
                            upperBound
                    );
                }
                case Normal -> {
                    ConfigSettings.NormalDistributionParams normalParams = maxRamUsageConfigSettings.getNormalDistributionParams();
                    
                    timeslices[tmId] = Math.max(0,
                            Math.round(
                                    (int) randomizer.nextGaussian(
                                            timeslices[tmId - 1],
                                            normalParams.getSd()
                                    )
                            )
                    );
                    
                }
                default ->
                    throw new IllegalArgumentException("Unsupported distribution type of maximal RAM usage: " + distrType);
            }
        }
    }

    private static int[] getTimeslices(
            ConfigSettings.MaxTimeslicesNumberConfigSettings maxTimeslicesNumberSettings,
            ConfigSettings.MaxRamUsageConfigSettings maxRamUsageConfigSettings
    ) {
        int timeslicesNum = getTimeslicesNum(maxTimeslicesNumberSettings);
        int[] timeslices = new int[timeslicesNum];

        ConfigSettings.MaxRamUsageConfigSettings.Type type = maxRamUsageConfigSettings.getType();

        // RAM usage in the first timeslot
        switch (type) {
            case Fixed -> {
                fillFixedValueTimeslaces(timeslices, maxRamUsageConfigSettings.getMaxRamUsage());
            }
            case Random_Independent -> {
                fillRandomIndependentTimeslaces(timeslices, maxRamUsageConfigSettings);
            }
            case Random_DependentOnPrevious -> {
                fillRandomDependentOnPreviousTimeslaces(timeslices, maxRamUsageConfigSettings);
            }
            default ->
                throw new IllegalArgumentException("Unsupported type of maximal RAM usage: " + type);
        }

        return timeslices;
    }
    
    private static int getCudaCoresNumber(ConfigSettings.CudaCoresConfigSettings cudaCoresConfigSettings) {
        switch (cudaCoresConfigSettings.getType()) {
            case Fixed -> {
                return cudaCoresConfigSettings.getCoresNumber();
            }
            case Random -> {
                ConfigSettings.CudaCoresConfigSettings.DistributionType distribType = cudaCoresConfigSettings.getDistributionType();
                switch (distribType) {
                    case Uniform -> {
                        ConfigSettings.UniformDistributionParams uniformParams = cudaCoresConfigSettings.getUniformDistributionParams();
                        return randomizer.nextInt(uniformParams.getLowerBound(), uniformParams.getUpperBound() + 1);
                    }
                    case Normal -> {
                        ConfigSettings.NormalDistributionParams normalDistributionParams
                                = cudaCoresConfigSettings.getNormalDistributionParams();
                        return Math.max(1,
                                Math.round(
                                        (int) randomizer.nextGaussian(
                                                normalDistributionParams.getMean(),
                                                normalDistributionParams.getSd()
                                        )
                                )
                        );
                    }
                    default ->
                        throw new IllegalStateException("Unsupported type of priority distribution type: " + distribType);
                }
            }
            default ->
                throw new IllegalStateException("Unsupported type of priority config type: " + cudaCoresConfigSettings.getType());
        }
    }
    
    private static double getJobArrival(ConfigSettings.JobArrivalConfigSettings jobArrivalConfigSettings) {
        switch (jobArrivalConfigSettings.getType()) {
            case Fixed -> {
                lastJobArrivalTime += jobArrivalConfigSettings.getInterval();
            }
            case Random -> {
                ConfigSettings.JobArrivalConfigSettings.DistributionType distribType = jobArrivalConfigSettings.getDistributionType();
                switch (distribType) {
                    case Uniform -> {
                        ConfigSettings.UniformDistributionParams uniformParams = jobArrivalConfigSettings.getUniformDistributionParams();
                        lastJobArrivalTime += randomizer.nextInt(uniformParams.getLowerBound(), uniformParams.getUpperBound() + 1);
                    }
                    case Poisson -> {
                        ConfigSettings.PoissonDistributionParams poissonDistributionParams
                                = jobArrivalConfigSettings.getPoissonDistributionParams();
                        lastJobArrivalTime += (-Math.log(1.0 - randomizer.nextDouble(1.0)) / ((double) poissonDistributionParams.getLambda()));
                    }
                    default ->
                        throw new IllegalStateException("Unsupported type of priority distribution type: " + distribType);
                }
            }
            default ->
                throw new IllegalStateException("Unsupported type of priority config type: " + jobArrivalConfigSettings.getType());
        }
        
        return lastJobArrivalTime;
    }

    // generates new job according to the specified settings
    private static Job generateJob(ConfigSettings configSettings) {
        int priority = getPriority(configSettings.getPriorityConfigSettings());
        boolean isStoppable = getStoppable(configSettings.getStoppabilityConfigSettings());
        int deadline = getDeadline(configSettings.getDeadlineConfigSettings());
        int[] timeslices = getTimeslices(
                configSettings.getMaxTimeslicesNumberConfigSettings(),
                configSettings.getMaxRamUsageConfigSettings()
        );
        int cudaCoresNumber = getCudaCoresNumber(configSettings.getCudaCoresConfigSettings());
        double jobArrival = getJobArrival(configSettings.getJobArrivalConfigSettings());

        return new Job(priority, isStoppable, deadline, timeslices, cudaCoresNumber, jobArrival);
    }

    /**
     * Generates and returns collection of jobs according to the specified
     * configuration settings.
     *
     * @param configSettings config settings to use for jobs generation
     * @return collection of generated jobs
     */
    static Collection<Job> generate(ConfigSettings configSettings) {
        randomizer = new Random(configSettings.getSeed());
        List<Job> jobs = new LinkedList();

        for (int jobId = 0; jobId < configSettings.getNumberOfJobs(); jobId++) {
            jobs.add(generateJob(configSettings));
        }

        return jobs;
    }

}
