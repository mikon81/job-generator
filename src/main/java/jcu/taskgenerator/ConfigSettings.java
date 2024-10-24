/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.taskgenerator;

/**
 * Configuration settings.
 *
 * Stores mainly properties of the generated tasks.
 *
 *
 * @author Michal Konopa
 */
final class ConfigSettings {

    /**
     * Parameters of uniform distribution.
     */
    static final class UniformDistributionParams {

        private final int lowerBound;
        private final int upperBound;

        /**
         * Creates new instance of uniform distribution parameters.
         *
         * @param lowerBound lower bound
         * @param upperBound upper bound
         */
        UniformDistributionParams(int lowerBound, int upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        /**
         * @return the lower bound
         */
        int getLowerBound() {
            return lowerBound;
        }

        /**
         * @return the upper bound
         */
        int getUpperBound() {
            return upperBound;
        }

    }

    /**
     * Parameters of normal distribution.
     */
    static final class NormalDistributionParams {

        private final double mean;
        private final double sd;

        /**
         * Creates new instance of normal distribution parameters.
         *
         * @param mean mean
         * @param sd standard deviation
         */
        NormalDistributionParams(double mean, double sd) {
            this.mean = mean;
            this.sd = sd;
        }

        /**
         * @return the mean
         */
        double getMean() {
            return mean;
        }

        /**
         * @return the standard deviation
         */
        double getSd() {
            return sd;
        }

    }

    /**
     * Parameters of Poisson distribution.
     */
    static final class PoissonDistributionParams {
        // lambda parameter
        private final int lambda;

        /**
         * Creates new instance of Poisson distribution parameters. Lambda
         * parameter means number of arrival tasks per 1 munute.
         *
         * @param lambda lambda
         */
        PoissonDistributionParams(int lambda) {
            this.lambda = lambda;
        }

        /**
         * @return the lambda
         */
        int getLambda() {
            return lambda;
        }

    }

    /**
     * All config settings concerning tasks priority.
     */
    static final class PriorityConfigSettings {

        /**
         * Type of task priority.
         *
         * Fixed - one concrete value of priority is defined for all the tasks
         * Random - each task has random value of priority assigned
         */
        static enum Type {
            Fixed, Random
        };

        /**
         * Probability distribution type of priority.
         *
         * Makes sense only if the priority type equals to "Random".
         */
        static enum DistributionType {
            Uniform, Normal
        };

        private final Type type;
        private final DistributionType distributionType;

        // parameters of respective distibutions - if deadline type is Type.Random
        private final UniformDistributionParams uniformDistributionParams;
        private final NormalDistributionParams normalDistributionParams;

        // interval - for Fixed type
        private final int priority;

        /**
         * Creates new instance of priority settings with Type.Fixed type and
         * specified value of priority. Value of probability distribution
         * function type will be se to <code>null</code>
         *
         * @param priority value of interval
         * @throws IllegalArgumentException if interval type is less or equal to
         * 0
         */
        PriorityConfigSettings(int priority) {
            this.type = Type.Fixed;
            this.distributionType = null;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = null;
            this.priority = checkPriority(priority);
        }

        /**
         * Creates new instance of priority settings with Type.Random type and
         * uniform distribution type with specified parameters. Value of
         * priority will be set to 0 - it should never be used.
         *
         * @param uniformDistrParams parameters of uniform distribution
         */
        PriorityConfigSettings(UniformDistributionParams uniformDistrParams) {
            this.type = Type.Random;
            this.distributionType = DistributionType.Uniform;
            this.uniformDistributionParams = uniformDistrParams;
            this.normalDistributionParams = null;
            this.priority = 0;
        }

        /**
         * Creates new instance of priority settings with Type.Random type and
         * normal distribution type with specified parameters. Value of priority
         * will be set to 0 - it should never be used.
         *
         * @param normalDistrParams parameters of normal distribution
         */
        PriorityConfigSettings(NormalDistributionParams normalDistrParams) {
            this.type = Type.Random;
            this.distributionType = DistributionType.Normal;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = normalDistrParams;
            this.priority = 0;
        }

        private int checkPriority(int priority) {
            if (priority > 0) {
                return priority;
            }

            throw new IllegalArgumentException("Invalid value of prioity - must be positive number. Got: " + priority);
        }

        /**
         * @return the interval config type
         */
        Type getType() {
            return type;
        }

        /**
         * @return the type of probability distribution function
         */
        DistributionType getDistributionType() {
            return distributionType;
        }

        /**
         * @return the value of interval
         */
        int getPriority() {
            return priority;
        }

        /**
         * @return the parameters of uniform distribution
         */
        UniformDistributionParams getUniformDistributionParams() {
            return uniformDistributionParams;
        }

        /**
         * @return the parameters of normal distribution
         */
        NormalDistributionParams getNormalDistributionParams() {
            return normalDistributionParams;
        }

    }

    /**
     * All config settings concerning task's deadline.
     */
    static final class DeadlineConfigSettings {

        /**
         * Type of task deadline.
         *
         * NotDefined - there is NO deadline dedined for any task Fixed - one
         * concrete value of deadline is defined for all the tasks Random - each
         * task has random value of deadline assigned
         */
        static enum Type {
            NotDefined, Fixed, Random
        };

        /**
         * Probability distribution type of deadline.
         *
         * Makes sense only of the deadline type equals to "Random".
         */
        static enum DistributionType {
            Uniform, Normal
        };

        private final DeadlineConfigSettings.Type type;
        private final DeadlineConfigSettings.DistributionType distributionType;

        // parameters of respective distibutions - if deadline type is Type.Random
        private final UniformDistributionParams uniformDistributionParams;
        private final NormalDistributionParams normalDistributionParams;

        private final int deadline;

        /**
         * Creates new instance of deadline settings with
         * DeadlineType.NotDefined type. Value of deadline will be set to 0 - it
         * should never be used. Value of probability distribution function type
         * will be se to <code>null</code>
         */
        DeadlineConfigSettings() {
            this.type = DeadlineConfigSettings.Type.NotDefined;
            this.distributionType = null;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = null;
            this.deadline = 0;
        }

        /**
         * Creates new instance of deadline settings with DeadlineType.Fixed
         * type and specified value of deadline. Value of probability
         * distribution function type will be se to <code>null</code>
         *
         * @param deadline value of deadline
         * @throws IllegalArgumentException if deadline type is less or equal to
         * 0
         */
        DeadlineConfigSettings(int deadline) {
            this.type = DeadlineConfigSettings.Type.Fixed;
            this.distributionType = null;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = null;
            this.deadline = checkDeadline(deadline);
        }

        /**
         * Creates new instance of deadline settings with DeadlineType.Random
         * type and uniform distribution type with specified parameters. Value
         * of deadline will be set to 0 - it should never be used.
         *
         * @param uniformDistrParams parameters of uniform distribution
         */
        DeadlineConfigSettings(UniformDistributionParams uniformDistrParams) {
            this.type = DeadlineConfigSettings.Type.Random;
            this.distributionType = DeadlineConfigSettings.DistributionType.Uniform;
            this.uniformDistributionParams = uniformDistrParams;
            this.normalDistributionParams = null;
            this.deadline = 0;
        }

        /**
         * Creates new instance of deadline settings with DeadlineType.Random
         * type and normal distribution type with specified parameters. Value of
         * deadline will be set to 0 - it should never be used.
         *
         * @param normalDistrParams parameters of normal distribution
         */
        DeadlineConfigSettings(NormalDistributionParams normalDistrParams) {
            this.type = DeadlineConfigSettings.Type.Random;
            this.distributionType = DeadlineConfigSettings.DistributionType.Normal;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = normalDistrParams;
            this.deadline = 0;
        }

        private int checkDeadline(int deadline) {
            if (deadline > 0) {
                return deadline;
            }

            throw new IllegalArgumentException("Invalid value of deadline - must be positive number. Got: " + deadline);
        }

        /**
         * @return the deadline types
         */
        DeadlineConfigSettings.Type getType() {
            return type;
        }

        /**
         * @return the type of probability distribution function
         */
        DeadlineConfigSettings.DistributionType getDistributionType() {
            return distributionType;
        }

        /**
         * @return the value of deadline
         */
        int getDeadline() {
            return deadline;
        }

        /**
         * @return the parameters of uniform distribution
         */
        UniformDistributionParams getUniformDistributionParams() {
            return uniformDistributionParams;
        }

        /**
         * @return the parameters of normal distribution
         */
        NormalDistributionParams getNormalDistributionParams() {
            return normalDistributionParams;
        }

    }

    /**
     * Configuration settings for maximal RAM usage.
     */
    static final class MaxRamUsageConfigSettings {

        /**
         * Type of task's maximal RAM usage in each timeslot.
         *
         * Fixed - fixed value for all timeslots Independent - in each timeslot
         * the maximal RAM usage is completely random. DependentOnPrevious -
         * maximal RAM usage in concrete timeslot depends on the value in the
         * previous timeslot
         */
        static enum Type {
            Fixed, Random_Independent, Random_DependentOnPrevious
        };

        /**
         * Probability distribution type of maximal usage of the RAM. Makes only
         * sense for types other then Type.Fixed.
         */
        static enum DistributionType {
            Uniform, Normal
        };

        // type of RAM usage in each task's timeslot 
        private final Type type;

        // probability distibution type for maximal RAM usage in each timeslot
        private final DistributionType distributionType;

        // parameters of respective distibutions - if deadline type is Type.Random
        private final UniformDistributionParams uniformDistributionParams;
        private final NormalDistributionParams normalDistributionParams;

        // maximal RAM usage
        private final int maxRamUsage;

        /**
         * Creates new instance of max RAM usage settings with Type.Fixed type.
         *
         * @param maxRamUsage maximal RAM usage
         * @throws IllegalArgumentException if value is less than or equal to 0
         */
        MaxRamUsageConfigSettings(int maxRamUsage) {
            this.type = Type.Fixed;
            this.distributionType = null;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = null;
            this.maxRamUsage = checkMaxRamValue(maxRamUsage);
        }

        /**
         * Creates new instance of maximal RAM usage config settings with
         * specified MaxRamUsageConfigSettings.Random type and and uniform
         * distribution type with specified parameters.
         *
         * Type type must NOT be Type.Fixed else an exception is thrown. Value
         * of maxRamUsage will be set to 0 - it should never be used.
         *
         * @param uniformDistrParams parameters of uniform distribution
         * @throws IllegalArgumentException if type equals to Type.Fixed
         */
        MaxRamUsageConfigSettings(
                Type type,
                UniformDistributionParams uniformDistrParams
        ) {
            if (type == Type.Fixed) {
                throw new IllegalArgumentException("Invalid type of maximal RAM usage: " + type);
            }

            this.type = type;
            this.distributionType = MaxRamUsageConfigSettings.DistributionType.Uniform;
            this.uniformDistributionParams = uniformDistrParams;
            this.normalDistributionParams = null;
            this.maxRamUsage = 0;
        }

        /**
         * Creates new instance of maximal RAM usage config settings with
         * specified MaxRamUsageConfigSettings.Random type and and normal
         * distribution type with specified parameters.
         *
         * Type type must NOT be Type.Fixed else an exception is thrown. Value
         * of maxRamUsage will be set to 0 - it should never be used.
         *
         * @param normalDistributionParams parameters of normal distribution
         * @throws IllegalArgumentException if type equals to Type.Fixed
         */
        MaxRamUsageConfigSettings(
                Type type,
                NormalDistributionParams normalDistributionParams
        ) {
            if (type == Type.Fixed) {
                throw new IllegalArgumentException("Invalid type of maximal RAM usage: " + type);
            }

            this.type = type;
            this.distributionType = MaxRamUsageConfigSettings.DistributionType.Normal;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = normalDistributionParams;
            this.maxRamUsage = 0;
        }

        private int checkMaxRamValue(int maxRamUsage) {
            if (maxRamUsage > 0) {
                return maxRamUsage;
            }

            throw new IllegalArgumentException("Invalid value of max RAM usage - must be positive number. Got: " + maxRamUsage);
        }

        /**
         * @return the deadline types
         */
        MaxRamUsageConfigSettings.Type getType() {
            return type;
        }

        /**
         * @return the type of probability distribution function
         */
        MaxRamUsageConfigSettings.DistributionType getDistributionType() {
            return distributionType;
        }

        /**
         * @return the value of maximal RAM usage
         */
        int getMaxRamUsage() {
            return maxRamUsage;
        }

        /**
         * @return the parameters of uniform distribution
         */
        UniformDistributionParams getUniformDistributionParams() {
            return uniformDistributionParams;
        }

        /**
         * @return the parameters of normal distribution
         */
        NormalDistributionParams getNormalDistributionParams() {
            return normalDistributionParams;
        }

    }

    /**
     * Configuration settings for maximal number of timeslices.
     */
    static final class MaxTimeslicesNumberConfigSettings {

        /**
         * Type of task's maximal RAM usage in each timeslot.
         *
         * Fixed - fixed value for all tasks Random - random for each task
         */
        static enum Type {
            Fixed, Random
        };

        /**
         * Probability distribution type of maximal usage of the RAM. Makes only
         * sense for types other than Type.Fixed.
         */
        static enum DistributionType {
            Uniform, Normal
        };

        // type of RAM usage in each task's timeslot 
        private final Type type;

        // probability distibution type for maximal RAM usage in each timeslot
        private final DistributionType distributionType;

        // parameters of respective distibutions - if deadline type is Type.Random
        private final UniformDistributionParams uniformDistributionParams;
        private final NormalDistributionParams normalDistributionParams;

        // maximal timeslices number
        private final int maxTimeslicesNum;

        /**
         * Creates new instance maximal timeslices number config settings with
         * the Type.Fixed type.
         *
         * @param maxTimeslicesNum maximal number of timeslices
         * @throws IllegalArgumentException if maxTimeslicesNum is less than or
         * equal to 0
         */
        MaxTimeslicesNumberConfigSettings(int maxTimeslicesNum) {
            this.type = Type.Fixed;
            this.distributionType = null;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = null;
            this.maxTimeslicesNum = checkMaxTimeslicesNum(maxTimeslicesNum);
        }

        /**
         * Creates new instance of maximal timeslices number config settings
         * with Random config type and and uniform distribution type with
         * specified parameters.
         *
         * Value of maxTimeslicesNum will be set to 0 - it should never be used.
         *
         * @param uniformDistrParams parameters of uniform distribution
         */
        MaxTimeslicesNumberConfigSettings(UniformDistributionParams uniformDistrParams) {
            this.type = Type.Random;
            this.distributionType = MaxTimeslicesNumberConfigSettings.DistributionType.Uniform;
            this.uniformDistributionParams = uniformDistrParams;
            this.normalDistributionParams = null;
            this.maxTimeslicesNum = 0;
        }

        /**
         * Creates new instance of maximal timeslices number config settings
         * with Random config type and normal distribution type with specified
         * parameters.
         *
         * Value of maxRamUsage will be set to 0 - it should never be used.
         *
         * @param normalDistributionParams parameters of normal distribution
         */
        MaxTimeslicesNumberConfigSettings(NormalDistributionParams normalDistributionParams) {
            this.type = Type.Random;
            this.distributionType = MaxTimeslicesNumberConfigSettings.DistributionType.Normal;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = normalDistributionParams;
            this.maxTimeslicesNum = 0;
        }

        private int checkMaxTimeslicesNum(int maxTimeslicesNum) {
            if (maxTimeslicesNum > 0) {
                return maxTimeslicesNum;
            }

            throw new IllegalArgumentException("Invalid value maximal timeslices - must be positive number. Got: " + maxTimeslicesNum);
        }

        /**
         * @return the deadline types
         */
        MaxTimeslicesNumberConfigSettings.Type getType() {
            return type;
        }

        /**
         * @return the type of probability distribution function
         */
        MaxTimeslicesNumberConfigSettings.DistributionType getDistributionType() {
            return distributionType;
        }

        /**
         * @return the value of maximal timeslices number
         */
        int getMaxTimeslicesNum() {
            return maxTimeslicesNum;
        }

        /**
         * @return the parameters of uniform distribution
         */
        UniformDistributionParams getUniformDistributionParams() {
            return uniformDistributionParams;
        }

        /**
         * @return the parameters of normal distribution
         */
        NormalDistributionParams getNormalDistributionParams() {
            return normalDistributionParams;
        }
    }

    /**
     * Configuration settings for task's stoppability.
     */
    static final class StoppabilityConfigSettings {

        /**
         * Type of task's stoppability.
         *
         * Fixed - fixed value for all tasks Random - random for each task
         */
        static enum Type {
            Fixed, Random
        };

        // type of config setting 
        private final Type type;

        // probability, that a task is stoppable
        private final double stoppableProbability;

        // indicates whether the tasks are stoppable
        private final boolean isStoppable;

        /**
         * Creates new instance of stoppability config settings with the
         * Type.Fixed type.
         *
         * @param isStoppable indicates, whether a task is stoppable
         */
        StoppabilityConfigSettings(boolean isStoppable) {
            this.type = Type.Fixed;
            this.stoppableProbability = 0;
            this.isStoppable = isStoppable;
        }

        /**
         * Creates new instance of stoppability config settings with the
         * specified value of probability, that the task is stoppable.
         *
         * @param stoppableProbability probability, that the task is stoppable
         * @throws IllegalArgumentException if the migrableProbability is out of
         * interval of (0,1)
         */
        StoppabilityConfigSettings(double stoppableProbability) {
            this.type = Type.Random;
            this.stoppableProbability = checkStoppableProbability(stoppableProbability);
            this.isStoppable = false;
        }

        /**
         * @return the stoppability config type
         */
        Type getType() {
            return type;
        }

        /**
         * @return if the tasks are stoppable
         */
        boolean isStoppable() {
            return isStoppable;
        }

        /**
         * @return the stoppable probability
         */
        double getStoppableProbability() {
            return stoppableProbability;
        }

        private double checkStoppableProbability(double stoppableProbability) {
            if (stoppableProbability >= 0 && stoppableProbability <= 1) {
                return stoppableProbability;
            }
            throw new IllegalArgumentException("Invalid value of stoppable probability: " + stoppableProbability);
        }
    }

    /**
     * Configuration settings for task's migrability.
     */
    static final class MigrabilityConfigSettings {

        /**
         * Type of task's migrability.
         *
         * Fixed - fixed value for all tasks Random - random for each task
         */
        static enum Type {
            Fixed, Random
        };

        // type of config setting 
        private final Type type;

        // probability, that a task is migrable
        private final double migrableProbability;

        // indicates whether the task is migrable
        private final boolean isMigrable;

        /**
         * Creates new instance of migrability config settings with the
         * Type.Fixed type.
         *
         * @param isMigrable indicates, whether a task is migrable
         */
        MigrabilityConfigSettings(boolean isMigrable) {
            this.type = Type.Fixed;
            this.migrableProbability = 0;
            this.isMigrable = isMigrable;
        }

        /**
         * Creates new instance of migrability config settings with the
         * specified value of probability, that the task is migrable.
         *
         * @param migrableProbability probability, that the task is migrable
         * @throws IllegalArgumentException if the migrableProbability is out of
         * interval of (0,1)
         */
        MigrabilityConfigSettings(double migrableProbability) {
            this.type = Type.Random;
            this.migrableProbability = checkMigrableProbability(migrableProbability);
            this.isMigrable = false;
        }

        /**
         * @return the stoppability config type
         */
        Type getType() {
            return type;
        }

        /**
         * @return if the tasks are migrable
         */
        boolean isMigrable() {
            return isMigrable;
        }

        /**
         * @return the migrableProbability
         */
        double getMigrableProbability() {
            return migrableProbability;
        }

        private double checkMigrableProbability(double migrableProbability) {
            if (migrableProbability >= 0 && migrableProbability <= 1) {
                return migrableProbability;
            }
            throw new IllegalArgumentException("Invalid value of migrable probability: " + migrableProbability);
        }
    }

    /**
     * Config settings for used CUDA cores.
     */
    static final class CudaCoresConfigSettings {

        /**
         * Fixed - one concrete value of CUDA cores number are same for all the
         * tasks Random - each task has random value of CUDA cores number
         */
        static enum Type {
            Fixed, Random
        };

        /**
         * Probability distribution type of CUDA cores number.
         *
         * Makes sense only if the type equals to "Random".
         */
        static enum DistributionType {
            Uniform, Normal
        };

        private final Type type;
        private final DistributionType distributionType;

        // parameters of respective distibutions - if deadline type is Type.Random
        private final UniformDistributionParams uniformDistributionParams;
        private final NormalDistributionParams normalDistributionParams;

        // CUDA cores number - for Fixed type
        private final int coresNumber;

        /**
         * Creates new instance of CUDA cores settings with Type.Fixed type and
         * specified value of cores number. Value of probability distribution
         * function type will be se to <code>null</code>
         *
         * @param coresNumber number of used CUDA cores
         * @throws IllegalArgumentException if interval type is less or equal to
         * 0
         */
        CudaCoresConfigSettings(int coresNumber) {
            this.type = Type.Fixed;
            this.distributionType = null;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = null;
            this.coresNumber = checkCoresNumber(coresNumber);
        }

        /**
         * Creates new instance of CUDA cores settings with Type.Random type and
         * uniform distribution type with specified parameters. Value of
         * coresNumber will be set to 0 - it should never be used.
         *
         * @param uniformDistrParams parameters of uniform distribution
         */
        CudaCoresConfigSettings(UniformDistributionParams uniformDistrParams) {
            this.type = Type.Random;
            this.distributionType = DistributionType.Uniform;
            this.uniformDistributionParams = uniformDistrParams;
            this.normalDistributionParams = null;
            this.coresNumber = 0;
        }

        /**
         * Creates new instance of CUDA cores settings with Type.Random type and
         * normal distribution type with specified parameters. Value of
         * coresNumber will be set to 0 - it should never be used.
         *
         * @param normalDistrParams parameters of normal distribution
         */
        CudaCoresConfigSettings(NormalDistributionParams normalDistrParams) {
            this.type = Type.Random;
            this.distributionType = DistributionType.Normal;
            this.uniformDistributionParams = null;
            this.normalDistributionParams = normalDistrParams;
            this.coresNumber = 0;
        }

        private int checkCoresNumber(int coresNumber) {
            if (coresNumber > 0) {
                return coresNumber;
            }
            throw new IllegalArgumentException("Invalid value of CUDA cores number - must be positive number. Got: " + coresNumber);
        }

        /**
         * @return the interval config type
         */
        Type getType() {
            return type;
        }

        /**
         * @return the type of probability distribution function
         */
        DistributionType getDistributionType() {
            return distributionType;
        }

        /**
         * @return the value of interval
         */
        int getCoresNumber() {
            return coresNumber;
        }

        /**
         * @return the parameters of uniform distribution
         */
        UniformDistributionParams getUniformDistributionParams() {
            return uniformDistributionParams;
        }

        /**
         * @return the parameters of normal distribution
         */
        NormalDistributionParams getNormalDistributionParams() {
            return normalDistributionParams;
        }
    }

    /**
     * All config settings concerning tasks arrival into the input queue.
     */
    static final class TaskArrivalConfigSettings {

        /**
         * Type of task arrival.
         *
         * Fixed - all tasks will arrive in regular time interval Random - each
         * task arrival will be random
         */
        static enum Type {
            Fixed, Random
        };

        /**
         * Probability distribution type of priority.
         *
         * Makes sense only of the deadline type equals to "Random".
         */
        static enum DistributionType {
            Uniform, Poisson
        };

        private final Type type;
        private final DistributionType distributionType;

        // parameters of respective distibutions - if the Type is Random
        private final UniformDistributionParams uniformDistributionParams;
        private final PoissonDistributionParams poissonDistributionParams;

        // interval [in minutes] - for Fixed type
        private final int interval;

        /**
         * Creates new instance of task arrival settings with Type.Fixed type
         * and specified value of regular time interval[in minutes]. Value of
         * probability distribution function type will be se to
         * <code>null</code>
         *
         * @param interval value of regular time interval[in minutes] of task
         * arrival
         * @throws IllegalArgumentException if interval type is less or equal to
         * 0
         */
        TaskArrivalConfigSettings(int interval) {
            this.type = Type.Fixed;
            this.distributionType = null;
            this.uniformDistributionParams = null;
            this.poissonDistributionParams = null;
            this.interval = checkInterval(interval);
        }

        /**
         * Creates new instance of task arrival settings with Type.Random type
         * and uniform distribution type with specified parameters. Value of
         * interval will be set to 0 - it should never be used.
         *
         * @param uniformDistrParams parameters of uniform distribution
         */
        TaskArrivalConfigSettings(UniformDistributionParams uniformDistrParams) {
            this.type = Type.Random;
            this.distributionType = DistributionType.Uniform;
            this.uniformDistributionParams = uniformDistrParams;
            this.poissonDistributionParams = null;
            this.interval = 0;
        }

        /**
         * Creates new instance of task arrival settings with Type.Random type
         * and Poisson distribution type with specified parameters. Value of
         * interval will be set to 0 - it should never be used.
         *
         * @param poissonDistrParams parameters of Poisson distribution
         */
        TaskArrivalConfigSettings(PoissonDistributionParams poissonDistrParams) {
            this.type = Type.Random;
            this.distributionType = DistributionType.Poisson;
            this.uniformDistributionParams = null;
            this.poissonDistributionParams = poissonDistrParams;
            this.interval = 0;
        }

        private int checkInterval(int interval) {
            if (interval > 0) {
                return interval;
            }
            throw new IllegalArgumentException("Invalid value of time interval - must be positive number. Got: " + interval);
        }

        /**
         * @return the interval config type
         */
        Type getType() {
            return type;
        }

        /**
         * @return the type of probability distribution function
         */
        DistributionType getDistributionType() {
            return distributionType;
        }

        /**
         * @return the value of interval
         */
        int getInterval() {
            return interval;
        }

        /**
         * @return the parameters of uniform distribution
         */
        UniformDistributionParams getUniformDistributionParams() {
            return uniformDistributionParams;
        }

        /**
         * @return the parameters of Poisson distribution
         */
        PoissonDistributionParams getPoissonDistributionParams() {
            return poissonDistributionParams;
        }

    }

    // interval config settings
    private final PriorityConfigSettings priorityConfigSettings;

    // deadline config settings
    private final DeadlineConfigSettings deadlineConfigSettings;

    // maximal RAM usage config settings
    private final MaxRamUsageConfigSettings maxRamUsageConfigSettings;

    // maximal timeslices number config settings
    private final MaxTimeslicesNumberConfigSettings maxTimeslicesNumberConfigSettings;

    // stoppable config settings
    private final StoppabilityConfigSettings stoppabilityConfigSettings;

    // migrable config settings
    private final MigrabilityConfigSettings migrabilityConfigSettings;

    // CUDA cores config settings
    private final CudaCoresConfigSettings cudaCoresConfigSettings;

    // Settings for all set of generated tasks.
    // number of generated tasks
    private final int numberOfTasks;

    // task arrival config settings
    private final TaskArrivalConfigSettings taskArrivalConfigSettings;

    // seed for internal randomizer
    private final long seed;

    /**
     * Creates new instance of configuration settings.
     *
     * @param priorityConfigSettings
     * @param deadlineConfigSettings
     * @param maxRamUsageConfigSettings
     * @param maxTimeslicesNumberConfigSettings
     * @param stoppabilityConfigSettings
     * @param migrabilityConfigSettings
     * @param cudaCoresConfigSettings 
     * @param numberOfTasks
     * @param taskArrivalConfigSettings
     * @param seed
     */
    ConfigSettings(
            PriorityConfigSettings priorityConfigSettings,
            DeadlineConfigSettings deadlineConfigSettings,
            MaxRamUsageConfigSettings maxRamUsageConfigSettings,
            MaxTimeslicesNumberConfigSettings maxTimeslicesNumberConfigSettings,
            StoppabilityConfigSettings stoppabilityConfigSettings,
            MigrabilityConfigSettings migrabilityConfigSettings,
            CudaCoresConfigSettings cudaCoresConfigSettings,
            int numberOfTasks,
            TaskArrivalConfigSettings taskArrivalConfigSettings,
            long seed
    ) {
        this.priorityConfigSettings = priorityConfigSettings;
        this.deadlineConfigSettings = deadlineConfigSettings;
        this.maxRamUsageConfigSettings = maxRamUsageConfigSettings;
        this.maxTimeslicesNumberConfigSettings = maxTimeslicesNumberConfigSettings;
        this.stoppabilityConfigSettings = stoppabilityConfigSettings;
        this.migrabilityConfigSettings = migrabilityConfigSettings;
        this.cudaCoresConfigSettings = cudaCoresConfigSettings;
        this.numberOfTasks = numberOfTasks;
        this.taskArrivalConfigSettings = taskArrivalConfigSettings;
        this.seed = seed;
    }

    /**
     * @return the number of tasks
     */
    int getNumberOfTasks() {
        return numberOfTasks;
    }

    /**
     * @return task arrival config settings
     */
    TaskArrivalConfigSettings getTaskArrivalConfigSettings() {
        return taskArrivalConfigSettings;
    }

    /**
     * @return the seed of the internal random generator
     */
    long getSeed() {
        return seed;
    }

    /**
     * @return the deadline config settings
     */
    DeadlineConfigSettings getDeadlineConfigSettings() {
        return deadlineConfigSettings;
    }

    /**
     * @return the maximum RAM usage config settings
     */
    MaxRamUsageConfigSettings getMaxRamUsageConfigSettings() {
        return maxRamUsageConfigSettings;
    }

    /**
     * @return the maximum timeslices number config settings
     */
    MaxTimeslicesNumberConfigSettings getMaxTimeslicesNumberConfigSettings() {
        return maxTimeslicesNumberConfigSettings;
    }

    /**
     * @return the interval config settings
     */
    PriorityConfigSettings getPriorityConfigSettings() {
        return priorityConfigSettings;
    }

    /**
     * @return the stoppabilityConfigSettings
     */
    StoppabilityConfigSettings getStoppabilityConfigSettings() {
        return stoppabilityConfigSettings;
    }

    /**
     * @return the migrabilityConfigSettings
     */
    MigrabilityConfigSettings getMigrabilityConfigSettings() {
        return migrabilityConfigSettings;
    }

    /**
     * @return the CUDA cores config settings
     */
    CudaCoresConfigSettings getCudaCoresConfigSettings() {
        return cudaCoresConfigSettings;
    }

}
