/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jcu.taskgenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Writes set of tasks into JSON file.
 * 
 * @author Michal Konopa
 */
final class TasksJsonWriter {
    // writes specified tasks into the "tasks.json" file in the current directory
    static void writeTasks(Collection<Task> tasks) throws IOException {
        JSONArray taskArrJson = new JSONArray();
        try (FileWriter taskFileWriter = new FileWriter("tasks.json")) {
            for ( var task : tasks ) {
                JSONObject taskJson = new JSONObject();
                taskJson.put("priority", task.getPriority());
                taskJson.put("isStoppable", task.isStopable());
                taskJson.put("isMigrable", task.isMigrable());
                taskJson.put("deadline", task.getDeadline());
                taskJson.put("timeslices", task.getTimeslices());
                taskJson.put("cudaCoresNumber", task.getCudaCoresNumber());
                taskJson.put("arrivalTime", task.getArrivalTime());
                
                taskArrJson.put(taskJson);
            }
            taskArrJson.write(taskFileWriter);
        }
    }
}
