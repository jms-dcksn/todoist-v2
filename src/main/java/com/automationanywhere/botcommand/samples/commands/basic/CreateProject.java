package com.automationanywhere.botcommand.samples.commands.basic;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;
import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.BOOLEAN;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

/**
 * @author James Dickson
 *
 */
@BotCommand

//CommandPks adds required information to be displayable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "CreateProject", label = "Create Project",
        node_label = "Create a new project in session {{sessionName}}", description = "Creates a new Todoist project",
        icon = "todo.svg", comment = true ,  text_color = "#7B848B" , background_color =  "#a6a6a6",
        return_label = "Assign output to a dictionary variable", return_type = DataType.DICTIONARY,
        return_description = "Outputs the ID of the project")

public class CreateProject {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public DictionaryValue action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Project Name", default_value_type = STRING)
            @NotEmpty String projectName,
            @Idx(index = "3", type = TEXT) @Pkg(label = "Parent Project ID", default_value_type = STRING)
                    String parentId,
            @Idx(index = "4", type = AttributeType.BOOLEAN)
            @Pkg(label = "Mark as favorite", default_value_type = BOOLEAN, default_value = "false")
            @NotEmpty
                    Boolean favorite
    ) throws ParseException {

        if ("".equals(projectName.trim())) {
            throw new BotCommandException("Project Name is empty");
        }
        Long lParentId;
        if ("".equals(parentId.trim())) {
            lParentId = null;
        } else {
            lParentId = Long.parseLong(parentId);
        }
        TodoistServer todoistServer = (TodoistServer) this.sessionMap.get(sessionName);
        String token = todoistServer.getToken();

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
            throw new BotCommandException("An error occurred when attempting to reach the server. Please try again.");
        }

        Object obj = new JSONParser().parse(response);
        JSONObject details = (JSONObject) obj;
        Map<String, Value> ResMap = new LinkedHashMap();
        ResMap.put("id", new StringValue(details.get("id").toString()));
        ResMap.put("url", new StringValue(details.get("url").toString()));
        return new DictionaryValue(ResMap);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}