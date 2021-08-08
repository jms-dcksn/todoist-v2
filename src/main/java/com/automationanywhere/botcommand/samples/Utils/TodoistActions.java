package com.automationanywhere.botcommand.samples.Utils;

import com.automationanywhere.botcommand.exception.BotCommandException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TodoistActions {

    public static JSONArray getTasks(String token){
        String url = "https://api.todoist.com/rest/v1/tasks";
        String method = "GET";
        String auth = "Bearer " + token;
        String response = "";
        try {
            response = HTTPRequest.Request(url, method, auth);
            if (response.contains("An error occurred")) {
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("Something went wrong with the request to Todoist server. Please try again.");
        }
        Object obj = null;
        try {
            obj = new JSONParser().parse(response);
        } catch (ParseException e) {
            throw new BotCommandException("An unexpected response was received. Please try again.");
        }
        JSONArray array = (JSONArray) obj;
        return array;
    }

    public static JSONObject CreateTask(String token, String taskName, Long lProjId, String dueDate, Integer numPrio) throws ParseException {

        String url = "https://api.todoist.com/rest/v1/tasks";
        String auth = "Bearer " + token;
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("content", taskName);
        jsonBody.put("project_id", lProjId);
        jsonBody.put("due_date", dueDate);
        jsonBody.put("priority", numPrio);
        String response = "";

        try {
            response = HTTPRequest.POST(auth, url, jsonBody.toString());
            if (response.contains("An error occurred")) {
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("Something went wrong with the request to Todoist server. Please try again.");
        }

        Object obj = new JSONParser().parse(response);
        JSONObject details = (JSONObject) obj;
        return details;
    }

    public static void UpdateTask(String token, String taskId, String taskName, String dueDate, Integer numPrio) throws ParseException {

        String url = "https://api.todoist.com/rest/v1/tasks/" + taskId;
        String auth = "Bearer " + token;
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("content", taskName);
        jsonBody.put("due_date", dueDate);
        jsonBody.put("priority", numPrio);
        String response = "";

        try {
            response = HTTPRequest.POST(auth, url, jsonBody.toString());
            if (response.contains("An error occurred")) {
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("Something went wrong with the request to Todoist server. Please try again. " + response);
        }
    }

    public static void CloseTask(String token, String taskId) throws ParseException {

        String url = "https://api.todoist.com/rest/v1/tasks/" + taskId + "/close";
        String auth = "Bearer " + token;
        String response = "";

        try {
            response = HTTPRequest.POST(auth, url, "{}");
            if (response.contains("An error occurred")) {
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("Something went wrong with the request to Todoist server. Please try again. " + response);
        }
    }
}
