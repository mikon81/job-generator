/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package jcu.taskgenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Generator of tasks running in cloud environment.
 * 
 * Eeach task has specific properties including but not limited to:
 * - duration
 * - RAM maximal consumption
 * - total number of generated tasks
 *  
 * Properties of generated task can be controlled by generator's configuration
 * JSON file residing in generator's directory. Generated tasks are written into
 * the output JSON file insided current directory. 
 * 
 * @author Michal Konopa
 */
public final class Launcher {

    // logger
    private static final Logger logger = LogManager.getLogger(Launcher.class);
    
    // prints help info about the program 
    private static void printHelp() {
        final String helpString = """
            TASK GENERATOR generates tasks and writes them into the output JSON
                           file in the current directory. Properties of each
                           generated task and the total number of generated task
                           is set in the input configuration file.
            usage: task-generator[version] <configuration JSON file>
            output: tasks.json in the current directory   
            """;
        
        System.out.println(helpString);
    }
    
    
    /**
     * Launches the generator.
     * 
     * Properties of the generated tasks will be set according to the configuration
     * JSON file as specified by the only parameter. If there is no parameter or 
     * multiple parametres supplied, help will be printed onto the standard output.
     * 
     * @param args name of the configuration file
     */
    public static void main(String[] args) {
        if ( args.length != 1 ) {
            printHelp();
            return;
        }
        
        try {
            ConfigSettings configSettings = ConfigSettingsFileReader.read(args[0]);
            
            Collection<Task> tasks = TaskGenerator.generate(configSettings);
            logger.info("Tasks successfully generated.");
            
            TasksJsonWriter.writeTasks(tasks);
            logger.info("Tasks written into the output file.");
        } catch (Exception ex) {
             logger.error(ex);
        }
    }
}
