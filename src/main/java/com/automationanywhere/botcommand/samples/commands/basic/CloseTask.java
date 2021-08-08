package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.samples.Utils.TodoistActions;
import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import org.json.simple.parser.ParseException;

import java.util.Map;

import static com.automationanywhere.commandsdk.model.AttributeType.GROUP;
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
        name = "UpdateTask", label = "Update Task",
        node_label = "Update task in session {{sessionName}}", description = "Updates a Todoist task",
        icon = "todo.svg", comment = true ,  text_color = "#7B848B" , background_color =  "#a6a6a6")


public class CloseTask {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public void action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Task ID", default_value_type = STRING)
            @NotEmpty String taskId
    ) throws ParseException {

        TodoistServer todoistServer = (TodoistServer) this.sessionMap.get(sessionName);
        String token = todoistServer.getToken();

        TodoistActions.CloseTask(token, taskId);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
