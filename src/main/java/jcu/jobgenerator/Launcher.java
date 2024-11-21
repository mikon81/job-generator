/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package jcu.jobgenerator;

import java.util.Collection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Generator of jobs running in cloud environment.
 * 
 * Eeach job has specific properties including but not limited to:
 * - priority
 * - duration [in time units]
 * - memory maximal consumption
 * - CUDA cores consumption
 * - indication, if job is interruptible 
 *  
 * Properties of generated job can be controlled by generator's configuration
 * JSON file residing in generator's directory. Generated jobs are written into
 * the output JSON file inside the current directory. 
 * 
 * @author Michal Konopa
 */
public final class Launcher {

    // logger
    private static final Logger logger = LogManager.getLogger(Launcher.class);
    
    // prints help info about the program 
    private static void printHelp() {
        final String helpString = """
            JOB GENERATOR generates jobs and writes them into the output JSON
                           file in the current directory. Properties of each
                           generated job and the total number of generated jobs
                           is set in the input configuration file.
            usage: job-generator[version] <configuration JSON file>
            output: job.json in the current directory   
            """;
        
        System.out.println(helpString);
    }
    
    
    /**
     * Launches the generator.
     * 
     * Properties of the generated jobs will be set according to the configuration
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
            
            Collection<Job> tasks = JobGenerator.generate(configSettings);
            logger.info("Jobs successfully generated.");
            
            JobsJsonWriter.writeJobs(tasks);
            logger.info("Jobs written into the output file.");
        } catch (Exception ex) {
             logger.error(ex);
        }
    }
}
