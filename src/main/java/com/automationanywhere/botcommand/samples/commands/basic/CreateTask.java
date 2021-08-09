package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.samples.Utils.TodoistTaskActions;
import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.LinkedHashMap;
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
        name = "CreateTask",
        label = "Create Task",
        node_label = "Create a new task in session {{sessionName}}",
        group_label = "Tasks",
        description = "Creates a new Todoist task",
        icon = "todo.svg",
        comment = true,
        text_color = "#7B848B",
        background_color =  "#a6a6a6",
        return_label = "Assign output to a dictionary variable",
        return_type = DataType.DICTIONARY,
        return_description = "Outputs the id and url of the task with keys 'id' and 'url'")


public class CreateTask {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public DictionaryValue action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Task Name", default_value_type = STRING)
            @NotEmpty String taskName,
            @Idx(index = "3", type = TEXT) @Pkg(label = "Project ID", default_value_type = STRING, description = "e.g. 2270452658")
                    String projectId,
            @Idx(index = "4", type = AttributeType.TEXT)
            @Pkg(label = "Due Date", default_value_type = STRING, description = "input format: YYYY-MM-DD")
                    String dueDate,
            @Idx(index = "5", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "5.1", pkg = @Pkg(label = "1", value = "1")),
                    @Idx.Option(index = "5.2", pkg = @Pkg(label = "2", value = "2")),
                    @Idx.Option(index = "5.3", pkg = @Pkg(label = "3", value = "3")),
                    @Idx.Option(index = "5.4", pkg = @Pkg(label = "4", value = "4"))
            })
            @Pkg(label = "Priority", default_value_type = STRING, description = "Enter priority level 1, 2, 3 or 4", default_value = "4")
            @NotEmpty String priority
    ) throws ParseException {

        if ("".equals(taskName.trim())) {
            throw new BotCommandException("Task Name is empty");
        }
        Long lProjId;
        if ("".equals(projectId)) {
            lProjId = null;
        } else {
            lProjId = Long.parseLong(projectId);
        }

        if ("".equals(dueDate.trim())) {
            dueDate = null;
        }
        Integer numPrio = Integer.parseInt(priority);

        TodoistServer todoistServer = (TodoistServer) this.sessionMap.get(sessionName);
        String token = todoistServer.getToken();

        JSONObject details = TodoistTaskActions.CreateTask(token, taskName, lProjId, dueDate, numPrio);

        Map<String, Value> ResMap = new LinkedHashMap();
        ResMap.put("id", new StringValue(details.get("id").toString()));
        ResMap.put("url", new StringValue(details.get("url").toString()));
        return new DictionaryValue(ResMap);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
