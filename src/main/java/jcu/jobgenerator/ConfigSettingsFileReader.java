/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.jobgenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

/**
 * Reader of the configuration file.
 * 
 * @author Michal Konopa
 */
final class ConfigSettingsFileReader {
    // constants for fields names in JSON document
    private static final String JNAME_CONFIG_TYPE = "type";
    private static final String JNAME_DISTRIBUTION_TYPE = "distributionType";
    private static final String JNAME_DISTRIBUTION_PARAMS = "distributionParams";
    
    private static final String JNAME_PRIORITY_CONFIG_SETTINGS = "priorityConfigSettings";
    private static final String JNAME_PRIORITY = "priority"; 
    
    private static final String JNAME_DEADLINE_CONFIG_SETTINGS = "deadlineConfigSettings";
    private static final String JNAME_DEADLINE = "deadline";
    
    private static final String JNAME_MAXRUMUSAGE_CONFIG_SETTINGS = "maxRamUsageConfigSettings";
    private static final String JNAME_MAXRUMUSAGE = "maxRamUsage";
    
    private static final String JNAME_MAXTIMESLICESNUMBER_CONFIG_SETTINGS = "maxTimeslicesNumberConfigSettings";
    private static final String JNAME_MAXTIMESLICES_NUMBER = "maxTimeslicesNumber";
    
    private static final String JNAME_STOPPABILITY_CONFIG_SETTINGS = "stoppabilityConfigSettings";
    private static final String JNAME_IS_STOPPABLE = "isStoppable";
    private static final String JNAME_STOPPABLE_PROBABILITY = "stoppableProbability";
    
    private static final String JNAME_CUDACORES_CONFIG_SETTINGS = "cudaCoresConfigSettings";
    private static final String JNAME_CORES_NUMBER = "coresNumber"; 
    
    private static final String JNAME_LOWER_BOUND = "lowerBound"; 
    private static final String JNAME_UPPER_BOUND = "upperBound"; 
    
    private static final String JNAME_CONFIG_TYPE_FIXED = "fixed"; 
    private static final String JNAME_CONFIG_TYPE_RANDOM = "random";
    private static final String JNAME_CONFIG_TYPE_NOTDEFINED = "notdefined";
    private static final String JNAME_CONFIG_TYPE_RANDOM_INDEPENDENT = "randomindependent";
    private static final String JNAME_CONFIG_TYPE_RANDOM_DEPENDENT_ON_PREVIOUS = "randomdependentonprevious";
    
    private static final String JNAME_UNIFORM_DISTR = "uniform";
    private static final String JNAME_NORMAL_DISTR = "normal";
    private static final String JNAME_POISSON_DISTR = "poisson";
    
    private static final String JNAME_MEAN = "mean";
    private static final String JNAME_STANDARD_DEVIATION = "sd";
    
    private static final String JNAME_POISSON_LAMBDA = "lambda";
    
    private static final String JNAME_NUMBER_OF_JOBS = "numberOfJobs";
    
    private static final String JNAME_JOBS_ARRIVAL_CONFIG_SETTINGS = "jobsArrivalConfigSettings";
    private static final String JNAME_ARRIVAL_INTERVAL = "interval";
    
    private static final String JNAME_SEED = "seed";
    
    
    // reads parameters of Uniform distribution from specified JSON objects 
    private static ConfigSettings.UniformDistributionParams readUniformDistributionParams(JSONObject jsonObj) {
        return new ConfigSettings.UniformDistributionParams(
                jsonObj.getInt(JNAME_LOWER_BOUND), 
                jsonObj.getInt(JNAME_UPPER_BOUND)
        );
    }
    
    // reads parameters of Normal distribution from specified JSON objects 
    private static ConfigSettings.NormalDistributionParams readNormalDistributionParams(JSONObject jsonObj) {
        return new ConfigSettings.NormalDistributionParams(
                jsonObj.getDouble(JNAME_MEAN), 
                jsonObj.getDouble(JNAME_STANDARD_DEVIATION)
        );
    }
    
    // reads parameters of Poisson distribution from specified JSON objects 
    private static ConfigSettings.PoissonDistributionParams readPoissonDistributionParams(JSONObject jsonObj) {
        return new ConfigSettings.PoissonDistributionParams(
                jsonObj.getInt(JNAME_POISSON_LAMBDA)
        );
    }
    
    // parses priority config settings
    private static ConfigSettings.PriorityConfigSettings parsePriorityConfigSettings(JSONObject configObjJson) {
        JSONObject prioritySettingJson = configObjJson.getJSONObject(JNAME_PRIORITY_CONFIG_SETTINGS);
        String configTypeStr = prioritySettingJson.getString(JNAME_CONFIG_TYPE);

        switch (configTypeStr.toLowerCase()) {
            case JNAME_CONFIG_TYPE_FIXED -> {
                return new ConfigSettings.PriorityConfigSettings(prioritySettingJson.getInt(JNAME_PRIORITY));
            }
            case JNAME_CONFIG_TYPE_RANDOM -> {
                String distrTypeStr = prioritySettingJson.getString(JNAME_DISTRIBUTION_TYPE);
                JSONObject distribParamsJson = prioritySettingJson.getJSONObject(JNAME_DISTRIBUTION_PARAMS);
                switch (distrTypeStr.toLowerCase()) {
                    case JNAME_UNIFORM_DISTR -> {
                        return new ConfigSettings.PriorityConfigSettings(
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case JNAME_NORMAL_DISTR -> {
                        return new ConfigSettings.PriorityConfigSettings(
                                readNormalDistributionParams(distribParamsJson)
                        );
                    }
                    default ->
                        throw new IllegalArgumentException("Unsupported distribution type: " + distrTypeStr);
                }
            }
            default ->
                throw new IllegalArgumentException("Unsupported config type: " + configTypeStr);
        }
    }
    
    // parses deadline config settings
    private static ConfigSettings.DeadlineConfigSettings parseDeadlineConfigSettings(JSONObject configObjJson) {
        JSONObject deadlineSettingJson = configObjJson.getJSONObject(JNAME_DEADLINE_CONFIG_SETTINGS);
        String configTypeStr = deadlineSettingJson.getString(JNAME_CONFIG_TYPE);

        switch (configTypeStr.toLowerCase()) {
            case JNAME_CONFIG_TYPE_NOTDEFINED -> {
                return new ConfigSettings.DeadlineConfigSettings();
            }
            case JNAME_CONFIG_TYPE_FIXED -> {
                return new ConfigSettings.DeadlineConfigSettings(deadlineSettingJson.getInt(JNAME_DEADLINE));
            }
            case JNAME_CONFIG_TYPE_RANDOM -> {
                String distrTypeStr = deadlineSettingJson.getString(JNAME_DISTRIBUTION_TYPE);
                JSONObject distribParamsJson = deadlineSettingJson.getJSONObject(JNAME_DISTRIBUTION_PARAMS);
                switch (distrTypeStr.toLowerCase()) {
                    case JNAME_UNIFORM_DISTR -> {
                        return new ConfigSettings.DeadlineConfigSettings(
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case JNAME_NORMAL_DISTR -> {
                        return new ConfigSettings.DeadlineConfigSettings(
                                readNormalDistributionParams(distribParamsJson)
                        );
                    }
                    default ->
                        throw new IllegalArgumentException("Unsupported distribution type: " + distrTypeStr);
                }
            }
            default ->
                throw new IllegalArgumentException("Unsupported config type: " + configTypeStr);
        }
    }
    
    // parses max RAM usage config settings
    private static ConfigSettings.MaxRamUsageConfigSettings parseMaxRamUsageConfigSettings(JSONObject configObjJson) {
        JSONObject maxRamUsageSettingJson = configObjJson.getJSONObject(JNAME_MAXRUMUSAGE_CONFIG_SETTINGS);
        String configTypeStr = maxRamUsageSettingJson.getString(JNAME_CONFIG_TYPE);

        switch (configTypeStr.toLowerCase()) {
            case JNAME_CONFIG_TYPE_FIXED -> {
                return new ConfigSettings.MaxRamUsageConfigSettings(maxRamUsageSettingJson.getInt(JNAME_MAXRUMUSAGE));
            }
            case JNAME_CONFIG_TYPE_RANDOM_INDEPENDENT, JNAME_CONFIG_TYPE_RANDOM_DEPENDENT_ON_PREVIOUS -> {
                ConfigSettings.MaxRamUsageConfigSettings.Type configType
                        = (configTypeStr.toLowerCase().equals(JNAME_CONFIG_TYPE_RANDOM_INDEPENDENT)
                        ? ConfigSettings.MaxRamUsageConfigSettings.Type.Random_Independent
                        : ConfigSettings.MaxRamUsageConfigSettings.Type.Random_DependentOnPrevious);
                
                String distrTypeStr = maxRamUsageSettingJson.getString(JNAME_DISTRIBUTION_TYPE);
                JSONObject distribParamsJson = maxRamUsageSettingJson.getJSONObject(JNAME_DISTRIBUTION_PARAMS);
                switch (distrTypeStr.toLowerCase()) {
                    case JNAME_UNIFORM_DISTR -> {
                        return new ConfigSettings.MaxRamUsageConfigSettings(
                                configType,
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case JNAME_NORMAL_DISTR -> {
                        return new ConfigSettings.MaxRamUsageConfigSettings(
                                configType,
                                readNormalDistributionParams(distribParamsJson)
                        );
                    }
                    default ->
                        throw new IllegalArgumentException("Unsupported distribution type: " + distrTypeStr);
                }
            }
            default ->
                throw new IllegalArgumentException("Unsupported config type: " + configTypeStr);
        }
    }
    
    // parses max number of timeslices config settings
    private static ConfigSettings.MaxTimeslicesNumberConfigSettings parseMaxSlicesNumberConfigSettings(JSONObject configObjJson) {
        JSONObject maxSlicesNumberSettingJson = configObjJson.getJSONObject(JNAME_MAXTIMESLICESNUMBER_CONFIG_SETTINGS);
        String configTypeStr = maxSlicesNumberSettingJson.getString(JNAME_CONFIG_TYPE);

        switch (configTypeStr.toLowerCase()) {
            case JNAME_CONFIG_TYPE_FIXED -> {
                return new ConfigSettings.MaxTimeslicesNumberConfigSettings(maxSlicesNumberSettingJson.getInt(JNAME_MAXTIMESLICES_NUMBER));
            }
            case JNAME_CONFIG_TYPE_RANDOM -> {
                String distrTypeStr = maxSlicesNumberSettingJson.getString(JNAME_DISTRIBUTION_TYPE);
                JSONObject distribParamsJson = maxSlicesNumberSettingJson.getJSONObject(JNAME_DISTRIBUTION_PARAMS);
                switch (distrTypeStr.toLowerCase()) {
                    case JNAME_UNIFORM_DISTR -> {
                        return new ConfigSettings.MaxTimeslicesNumberConfigSettings(
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case JNAME_NORMAL_DISTR -> {
                        return new ConfigSettings.MaxTimeslicesNumberConfigSettings(
                                readNormalDistributionParams(distribParamsJson)
                        );
                    }
                    default ->
                        throw new IllegalArgumentException("Unsupported distribution type: " + distrTypeStr);
                }
            }
            default ->
                throw new IllegalArgumentException("Unsupported config type: " + configTypeStr);
        }
    }
    
    // parses stoppability config settings
    private static ConfigSettings.StoppabilityConfigSettings parseStoppabilityConfigSettings(JSONObject configObjJson) {
        JSONObject stoppabilitySettingJson = configObjJson.getJSONObject(JNAME_STOPPABILITY_CONFIG_SETTINGS);
        String configTypeStr = stoppabilitySettingJson.getString(JNAME_CONFIG_TYPE);

        switch (configTypeStr.toLowerCase()) {
            case JNAME_CONFIG_TYPE_FIXED -> {
                return new ConfigSettings.StoppabilityConfigSettings(stoppabilitySettingJson.getBoolean(JNAME_IS_STOPPABLE));
            }
            case JNAME_CONFIG_TYPE_RANDOM -> {
                return new ConfigSettings.StoppabilityConfigSettings(stoppabilitySettingJson.getDouble(JNAME_STOPPABLE_PROBABILITY));
            }
            default ->
                throw new IllegalArgumentException("Unsupported config type: " + configTypeStr);
        }
    }
    
    // parses CUDA cores
    private static ConfigSettings.CudaCoresConfigSettings parseCudaCoresConfigSettings(JSONObject configObjJson) {
        JSONObject cudaCoresConfigSettings = configObjJson.getJSONObject(JNAME_CUDACORES_CONFIG_SETTINGS);
        String configTypeStr = cudaCoresConfigSettings.getString(JNAME_CONFIG_TYPE);

        switch (configTypeStr.toLowerCase()) {
            case JNAME_CONFIG_TYPE_FIXED -> {
                return new ConfigSettings.CudaCoresConfigSettings(cudaCoresConfigSettings.getInt(JNAME_CORES_NUMBER));
            }
            case JNAME_CONFIG_TYPE_RANDOM -> {
                String distrTypeStr = cudaCoresConfigSettings.getString(JNAME_DISTRIBUTION_TYPE);
                JSONObject distribParamsJson = cudaCoresConfigSettings.getJSONObject(JNAME_DISTRIBUTION_PARAMS);
                switch (distrTypeStr.toLowerCase()) {
                    case JNAME_UNIFORM_DISTR -> {
                        return new ConfigSettings.CudaCoresConfigSettings(
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case JNAME_NORMAL_DISTR -> {
                        return new ConfigSettings.CudaCoresConfigSettings(
                                readNormalDistributionParams(distribParamsJson)
                        );
                    }
                    default ->
                        throw new IllegalArgumentException("Unsupported distribution type: " + distrTypeStr);
                }
            }
            default ->
                throw new IllegalArgumentException("Unsupported config type: " + configTypeStr);
        }
    }
    
    // parses job arrival config settings
    private static ConfigSettings.JobArrivalConfigSettings parseJobArrivalConfigSettings(JSONObject configObjJson) {
        JSONObject jobArrivalSettingJson = configObjJson.getJSONObject(JNAME_JOBS_ARRIVAL_CONFIG_SETTINGS);
        String configTypeStr = jobArrivalSettingJson.getString(JNAME_CONFIG_TYPE);

        switch (configTypeStr.toLowerCase()) {
            case JNAME_CONFIG_TYPE_FIXED -> {
                return new ConfigSettings.JobArrivalConfigSettings(jobArrivalSettingJson.getInt(JNAME_ARRIVAL_INTERVAL));
            }
            case JNAME_CONFIG_TYPE_RANDOM -> {
                String distrTypeStr = jobArrivalSettingJson.getString(JNAME_DISTRIBUTION_TYPE);
                JSONObject distribParamsJson = jobArrivalSettingJson.getJSONObject(JNAME_DISTRIBUTION_PARAMS);
                switch (distrTypeStr.toLowerCase()) {
                    case JNAME_UNIFORM_DISTR -> {
                        return new ConfigSettings.JobArrivalConfigSettings(
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case JNAME_POISSON_DISTR -> {
                        return new ConfigSettings.JobArrivalConfigSettings(
                                readPoissonDistributionParams(distribParamsJson)
                        );
                    }
                    default ->
                        throw new IllegalArgumentException("Unsupported distribution type: " + distrTypeStr);
                }
            }
            default ->
                throw new IllegalArgumentException("Unsupported config type: " + configTypeStr);
        }
    }
    
    /**
     * Reads configuration JSON file of specified name from current directory,
     * parses it and returns object of corresponding configuration settings.
     * 
     * @param fileName name of the configuration file in the current directory to parse
     * @return object of configuration settings read from the config file
     */
    static ConfigSettings read(String fileName) throws IOException {
        String fileContentStr = Files.readString(Paths.get(fileName));
        
        // parsing JSON string
        JSONObject configObjJson = new JSONObject(fileContentStr);
        
        ConfigSettings.PriorityConfigSettings priorityConfigSettings = parsePriorityConfigSettings(configObjJson);
        ConfigSettings.DeadlineConfigSettings deadlineConfigSettings = parseDeadlineConfigSettings(configObjJson);
        ConfigSettings.MaxRamUsageConfigSettings maxRamUsageConfigSettings = parseMaxRamUsageConfigSettings(configObjJson);
        ConfigSettings.MaxTimeslicesNumberConfigSettings maxTimeslicesNumberConfigSettings = parseMaxSlicesNumberConfigSettings(configObjJson);
        ConfigSettings.StoppabilityConfigSettings stoppabilityConfigSettings = parseStoppabilityConfigSettings(configObjJson);
        ConfigSettings.CudaCoresConfigSettings cudaCoresConfigSettings = parseCudaCoresConfigSettings(configObjJson);
        
        long seed = configObjJson.getLong(JNAME_SEED);
        int numberOfJobs = configObjJson.getInt(JNAME_NUMBER_OF_JOBS);
        ConfigSettings.JobArrivalConfigSettings jobArrivalConfigSettings = parseJobArrivalConfigSettings(configObjJson);
        
        return new ConfigSettings(
                priorityConfigSettings, 
                deadlineConfigSettings, 
                maxRamUsageConfigSettings, 
                maxTimeslicesNumberConfigSettings, 
                stoppabilityConfigSettings, 
                cudaCoresConfigSettings,
                numberOfJobs,
                jobArrivalConfigSettings,
                seed
        );
    }
    
}
