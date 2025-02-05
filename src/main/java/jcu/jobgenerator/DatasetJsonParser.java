/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.jobgenerator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Parses JSON source files containing jobs dataset.
 * 
 * @author Michal Konopa
 */
final class DatasetJsonParser {
    
    // returns array of timeslices for specified JSON job object
    static private int[] getTimeslices(JSONObject jsonJob) {
        JSONArray jsonTimeslices = jsonJob.getJSONArray("timeslices");
        int[] timeslices = new int[jsonTimeslices.length()];
        
        for ( int tmId = 0; tmId < timeslices.length; tmId++ ) {
            timeslices[tmId] = jsonTimeslices.getInt(tmId);
        }
        
        return timeslices;
    }
    
    
    /**
     * Parses source file of jobs dataset as specified by it name and returns
     * queue of jobs contained in the dataset.
     * 
     * @param fileName name of source dataset file
     * @return queue of jobs contained in the source dataset file
     */
    static Queue<Job> parseJsonDataset(String fileName) throws IOException {
        String fileContent = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
        JSONArray jsonFileContent = new JSONArray(fileContent);
        
        Queue<Job> jobs = new LinkedList<>();
        
        for ( int jobId = 0; jobId < jsonFileContent.length(); jobId++ ) {
            JSONObject jsonJob = jsonFileContent.getJSONObject(jobId);
            
            
            Job job = new Job(
                    jsonJob.getInt("priority"),
                    jsonJob.getBoolean("isStoppable"),
                    jsonJob.getInt("deadline"),
                    getTimeslices(jsonJob),
                    jsonJob.getInt("cudaCoresNumber"),
                    jsonJob.getDouble("arrivalTime")
            );
            jobs.add(job);
        }
        
        return jobs;
    }
}
