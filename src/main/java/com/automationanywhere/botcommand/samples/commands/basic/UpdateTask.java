package com.automationanywhere.botcommand.samples.commands.basic;

import static com.automationanywhere.commandsdk.model.AttributeType.GROUP;

import com.automationanywhere.botcommand.samples.Utils.TodoistTaskActions;
import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import org.json.simple.parser.ParseException;

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
        name = "UpdateTask", label = "Update Task",
        node_label = "Update task in session {{sessionName}}", description = "Updates a Todoist task",
        icon = "todo.svg", comment = true ,  text_color = "#7B848B" , background_color =  "#a6a6a6")


public class UpdateTask {
    @Sessions
    private Map<String, Object> sessionMap;
    @Idx(index = "3", type = GROUP)
    @Pkg(label = "Parameters to Update")
    String nameGroup;
    @Execute
    public void action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Task ID", default_value_type = STRING)
            @NotEmpty String taskId,
            @Idx(index = "3.1", type = TEXT) @Pkg(label = "Task Name", default_value_type = STRING)
            @NotEmpty String taskName,
            @Idx(index = "3.2", type = AttributeType.TEXT)
            @Pkg(label = "Due Date", default_value_type = STRING, description = "input format: YYYY-MM-DD")
                    String dueDate,
            @Idx(index = "4", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "4.1", pkg = @Pkg(label = "1", value = "1")),
                    @Idx.Option(index = "4.2", pkg = @Pkg(label = "2", value = "2")),
                    @Idx.Option(index = "4.3", pkg = @Pkg(label = "3", value = "3")),
                    @Idx.Option(index = "4.4", pkg = @Pkg(label = "4", value = "4"))
            })
            @Pkg(label = "Priority", default_value_type = STRING, description = "Enter priority level 1, 2, 3 or 4", default_value = "4")
            String priority
    ) throws ParseException {

        if ("".equals(taskName.trim())) {
            taskName = null;
        }

        if ("".equals(dueDate.trim())) {
            dueDate = null;
        }
        Integer numPrio = Integer.parseInt(priority);

        TodoistServer todoistServer = (TodoistServer) this.sessionMap.get(sessionName);
        String token = todoistServer.getToken();

        TodoistTaskActions.UpdateTask(token, taskId, taskName, dueDate, numPrio);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
