package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;
import com.automationanywhere.botcommand.samples.Utils.TodoistProjectActions;
import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
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
        name = "UpdateProject",
        label = "Update Project",
        node_label = "Update a project in session {{sessionName}}",
        group_label = "Projects",
        description = "Updates a Todoist project",
        icon = "todo.svg",
        comment = true ,
        text_color = "#7B848B" ,
        background_color =  "#a6a6a6")

public class UpdateProject {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public void action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Project ID", default_value_type = STRING)
            @NotEmpty String projectId,
            @Idx(index = "3", type = TEXT) @Pkg(label = "Project Name", default_value_type = STRING)
            @NotEmpty String projectName,
            @Idx(index = "4", type = AttributeType.BOOLEAN)
            @Pkg(label = "Mark as favorite", default_value_type = BOOLEAN, default_value = "false")
            @NotEmpty
                    Boolean favorite
    ) throws ParseException {

        if ("".equals(projectName.trim())) {
            throw new BotCommandException("Project Name is empty");
        }
        TodoistServer todoistServer = (TodoistServer) this.sessionMap.get(sessionName);
        String token = todoistServer.getToken();

        TodoistProjectActions.UpdateProject(token, projectId, projectName, favorite);

    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
