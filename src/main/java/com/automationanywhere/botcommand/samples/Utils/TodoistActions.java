package com.automationanywhere.botcommand.samples.Utils;

import com.automationanywhere.botcommand.exception.BotCommandException;
import org.json.simple.JSONArray;
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
}
