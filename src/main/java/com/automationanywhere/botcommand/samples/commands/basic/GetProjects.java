package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;
import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;

import java.io.IOException;
import java.util.*;

import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * @author James Dickson
 *
 */
@BotCommand

//CommandPks adds required information to be displayable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "GetProjects", label = "Get Projects",
        node_label = "Get list of projects in session {{sessionName}}", description = "Gets list of Todoist projects", icon = "todo.svg",
        comment = true ,  text_color = "#7B848B" , background_color =  "#a6a6a6",
        return_label = "Assign output to a list variable", return_type = DataType.LIST, return_description = "Outputs all projects in a list")


public class GetProjects {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public ListValue action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default") @NotEmpty String sessionName
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
                throw new BotCommandException(response);
            }
        }
        catch(Exception e){
            throw new BotCommandException("Something went wrong with the request to Todoist server. Please try again.");
        }
        Object obj = new JSONParser().parse(response);
        JSONArray array = (JSONArray) obj;
        ListValue<String> returnValue = new ListValue<String>();
        List<Value> values = new ArrayList<Value>();

        for(int i=0; i<array.size(); i++) {
            JSONObject arrayElement = (JSONObject) array.get(i);
            String projectName = arrayElement.get("name").toString();
            values.add(new StringValue(projectName));
        }

        returnValue.set(values);
        return returnValue;
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
