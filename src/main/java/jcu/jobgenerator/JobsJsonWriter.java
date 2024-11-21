/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.jobgenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Writes set of jobs into JSON file.
 * 
 * @author Michal Konopa
 */
final class JobsJsonWriter {
    // writes specified tasks into the "jobs.json" file in the current directory
    static void writeJobs(Collection<Job> jobs) throws IOException {
        JSONArray taskArrJson = new JSONArray();
        try (FileWriter jobFileWriter = new FileWriter("jobs.json")) {
            for ( var job : jobs ) {
                JSONObject jobJson = new JSONObject();
                jobJson.put("priority", job.getPriority());
                jobJson.put("isStoppable", job.isStopable());
                jobJson.put("deadline", job.getDeadline());
                jobJson.put("timeslices", job.getTimeslices());
                jobJson.put("cudaCoresNumber", job.getCudaCoresNumber());
                jobJson.put("arrivalTime", job.getArrivalTime());
                
                taskArrJson.put(jobJson);
            }
            taskArrJson.write(jobFileWriter);
        }
    }
}
