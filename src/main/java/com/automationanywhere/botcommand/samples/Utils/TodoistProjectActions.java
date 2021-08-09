package com.automationanywhere.botcommand.samples.Utils;

import com.automationanywhere.botcommand.exception.BotCommandException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TodoistProjectActions {

    public static String getProjects(String token){
        String url = "https://api.todoist.com/rest/v1/projects";
        String method = "GET";
        String auth = "Bearer " + token;

        //insert Try/Catch block here, catching non-200 codes as well as any error
        String response = "";
        try {
            response = HTTPRequest.Request(url, method, auth);
            if (response.contains("An error occurred")) {
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("Something went wrong with the request to Todoist server. Please try again." + response);
        }
        return response;
    }

    public static JSONObject CreateProject(String token, String projectName, Long lParentId, Boolean favorite) throws ParseException {

        String url = "https://api.todoist.com/rest/v1/projects";
        String auth = "Bearer " + token;
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("name", projectName);
        jsonBody.put("parent_id", lParentId);
        jsonBody.put("favorite", favorite);
        String response = "";

        try {
            response = HTTPRequest.POST(auth, url, jsonBody.toString());
            if (response.contains("An error occurred")) {
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("An error occurred when attempting to reach the server. Please try again." + response);
        }

        Object obj = new JSONParser().parse(response);
        JSONObject details = (JSONObject) obj;
        return details;
    }

    public static void UpdateProject(String token, String projectId, String projectName, Boolean favorite) {

        String url = "https://api.todoist.com/rest/v1/projects/" + projectId;
        String auth = "Bearer " + token;
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("name", projectName);
        jsonBody.put("favorite", favorite);
        String response = "";

        try {
            response = HTTPRequest.POST(auth, url, jsonBody.toString());
            if (response.contains("An error occurred")) {
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("An error occurred when attempting to reach the server. Please try again." + response);
        }
    }


    public static void DeleteProject(String token, String projectId){

        String url = "https://api.todoist.com/rest/v1/projects/" + projectId;
        String auth = "Bearer " + token;
        String response = "";

        try {
            response = HTTPRequest.Request(url, "DELETE", auth);
            if (response.contains("An error occurred")) {
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("Something went wrong with the request to Todoist server. Please try again. " + response);
        }
    }
}
