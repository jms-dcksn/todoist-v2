package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;
import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

/**
 * @author James Dickson
 *
 */
@BotCommand

//CommandPks adds required information to be displayable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "GetProjectId", label = "Get Project ID",
        node_label = "Get a project ID in session {{sessionName}}", description = "Returns the ID of a project, given the name", icon = "todo.svg",
        comment = true ,  text_color = "#7B848B" , background_color =  "#a6a6a6",
        return_label = "Assign output to a string variable", return_type = DataType.STRING, return_description = "Outputs the ID of the project")


public class GetProjectId {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public StringValue action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Project Name", default_value_type = STRING) @NotEmpty String project
    ) throws IOException, ParseException {


        TodoistServer todoistServer = (TodoistServer) this.sessionMap.get(sessionName);
        String token = todoistServer.getToken();
        String url = "https://api.todoist.com/rest/v1/projects";
        String method = "GET";
        String auth = "Bearer " + token;

        //insert Try/Catch block here, catching non-200 codes as well as any error
        String response = "";
        try {
            response = HTTPRequest.Request(url, method, auth);
            if (response.contains("An error occurred")) {
                throw new BotCommandException("An error occurred" + response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("Exception caught");
        }
        Object obj = new JSONParser().parse(response);
        JSONArray array = (JSONArray) obj;
        String projectId = "";

        for(int i=0; i<array.size(); i++){
            JSONObject arrayElement = (JSONObject) array.get(i);
            if(project.equals(arrayElement.get("name").toString())){
                projectId = arrayElement.get("id").toString();
                break;
            }
        }

        if(projectId.equals("")){
            return new StringValue("Project ID not found for provided project name");
        }
        return new StringValue(projectId);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
