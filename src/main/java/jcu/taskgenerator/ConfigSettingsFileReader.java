/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.taskgenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

/**
 * Reader of the configuration file.
 * 
 * @author Michal Konopa
 */
class ConfigSettingsFileReader {
    
    // reads parameters of Uniform distribution from specified JSON objects 
    private static ConfigSettings.UniformDistributionParams readUniformDistributionParams(JSONObject jsonObj) {
        return new ConfigSettings.UniformDistributionParams(
                jsonObj.getInt("lowerBound"), 
                jsonObj.getInt("upperBound")
        );
    }
    
    // reads parameters of Normal distribution from specified JSON objects 
    private static ConfigSettings.NormalDistributionParams readNormalDistributionParams(JSONObject jsonObj) {
        return new ConfigSettings.NormalDistributionParams(
                jsonObj.getDouble("mean"), 
                jsonObj.getDouble("sd")
        );
    }
    
    // parses priority config settings
    private static ConfigSettings.PriorityConfigSettings parsePriorityConfigSettings(JSONObject configObjJson) {
        JSONObject prioritySettingJson = configObjJson.getJSONObject("priorityConfigSettings");
        String configTypeStr = prioritySettingJson.getString("type");

        switch (configTypeStr) {
            case "Fixed" -> {
                return new ConfigSettings.PriorityConfigSettings(prioritySettingJson.getInt("priority"));
            }
            case "Random" -> {
                String distrTypeStr = prioritySettingJson.getString("distributionType");
                JSONObject distribParamsJson = prioritySettingJson.getJSONObject("distributionParams");
                switch (distrTypeStr) {
                    case "Uniform" -> {
                        return new ConfigSettings.PriorityConfigSettings(
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case "Normal" -> {
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
        JSONObject deadlineSettingJson = configObjJson.getJSONObject("deadlineConfigSettings");
        String configTypeStr = deadlineSettingJson.getString("type");

        switch (configTypeStr) {
            case "NotDefined" -> {
                return new ConfigSettings.DeadlineConfigSettings();
            }
            case "Fixed" -> {
                return new ConfigSettings.DeadlineConfigSettings(deadlineSettingJson.getInt("deadline"));
            }
            case "Random" -> {
                String distrTypeStr = deadlineSettingJson.getString("distributionType");
                JSONObject distribParamsJson = deadlineSettingJson.getJSONObject("distributionParams");
                switch (distrTypeStr) {
                    case "Uniform" -> {
                        return new ConfigSettings.DeadlineConfigSettings(
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case "Normal" -> {
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
        JSONObject maxRamUsageSettingJson = configObjJson.getJSONObject("maxRamUsageConfigSettings");
        String configTypeStr = maxRamUsageSettingJson.getString("type");

        switch (configTypeStr) {
            case "Fixed" -> {
                return new ConfigSettings.MaxRamUsageConfigSettings(maxRamUsageSettingJson.getInt("maxRamUsage"));
            }
            case "Random_Independent", "Random_DependentOnPrevious" -> {
                ConfigSettings.MaxRamUsageConfigSettings.Type configType
                        = (configTypeStr.equals("Random_Independent")
                        ? ConfigSettings.MaxRamUsageConfigSettings.Type.Random_Independent
                        : ConfigSettings.MaxRamUsageConfigSettings.Type.Random_DependentOnPrevious);
                
                String distrTypeStr = maxRamUsageSettingJson.getString("distributionType");
                JSONObject distribParamsJson = maxRamUsageSettingJson.getJSONObject("distributionParams");
                switch (distrTypeStr) {
                    case "Uniform" -> {
                        return new ConfigSettings.MaxRamUsageConfigSettings(
                                configType,
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case "Normal" -> {
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
        JSONObject maxSlicesNumberSettingJson = configObjJson.getJSONObject("maxTimeslicesNumberConfigSettings");
        String configTypeStr = maxSlicesNumberSettingJson.getString("type");

        switch (configTypeStr) {
            case "Fixed" -> {
                return new ConfigSettings.MaxTimeslicesNumberConfigSettings(maxSlicesNumberSettingJson.getInt("maxTimeslicesNum"));
            }
            case "Random" -> {
                String distrTypeStr = maxSlicesNumberSettingJson.getString("distributionType");
                JSONObject distribParamsJson = maxSlicesNumberSettingJson.getJSONObject("distributionParams");
                switch (distrTypeStr) {
                    case "Uniform" -> {
                        return new ConfigSettings.MaxTimeslicesNumberConfigSettings(
                                readUniformDistributionParams(distribParamsJson)
                        );
                    }
                    case "Normal" -> {
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
        JSONObject stoppabilitySettingJson = configObjJson.getJSONObject("stoppabilityConfigSettings");
        String configTypeStr = stoppabilitySettingJson.getString("type");

        switch (configTypeStr) {
            case "Fixed" -> {
                return new ConfigSettings.StoppabilityConfigSettings(stoppabilitySettingJson.getBoolean("isStoppable"));
            }
            case "Random" -> {
                return new ConfigSettings.StoppabilityConfigSettings(stoppabilitySettingJson.getDouble("stoppableProbability"));
            }
            default ->
                throw new IllegalArgumentException("Unsupported config type: " + configTypeStr);
        }
    }
    
    // parses stoppability config settings
    private static ConfigSettings.MigrabilityConfigSettings parseMigrabilityConfigSettings(JSONObject configObjJson) {
        JSONObject migrabilitySettingsJson = configObjJson.getJSONObject("migrabilityConfigSettings");
        String configTypeStr = migrabilitySettingsJson.getString("type");

        switch (configTypeStr) {
            case "Fixed" -> {
                return new ConfigSettings.MigrabilityConfigSettings(migrabilitySettingsJson.getBoolean("isMigrable"));
            }
            case "Random" -> {
                return new ConfigSettings.MigrabilityConfigSettings(migrabilitySettingsJson.getDouble("migrableProbability"));
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
        ConfigSettings.MigrabilityConfigSettings migrabilityConfigSettings = parseMigrabilityConfigSettings(configObjJson);
        
        long seed = configObjJson.getLong("seed");
        int numberOfTasks = configObjJson.getInt("numberOfTasks");
        
        return new ConfigSettings(
                priorityConfigSettings, 
                deadlineConfigSettings, 
                maxRamUsageConfigSettings, 
                maxTimeslicesNumberConfigSettings, 
                stoppabilityConfigSettings, 
                migrabilityConfigSettings, 
                numberOfTasks, 
                seed
        );
    }
    
}
