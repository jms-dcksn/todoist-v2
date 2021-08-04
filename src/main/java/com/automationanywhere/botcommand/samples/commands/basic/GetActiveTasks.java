package com.automationanywhere.botcommand.samples.commands.basic;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;
import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
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
        name = "GetActiveTasks", label = "Get Tasks",
        node_label = "Get list of tasks in session {{sessionName}}", description = "Gets list of Todoist active tasks",
        icon = "todo.svg", comment = true ,  text_color = "#7B848B" , background_color =  "#a6a6a6",
        return_label = "Assign output to a table variable", return_type = DataType.TABLE, return_required = true,
        return_description = "Outputs table with task id, name, priority, due date and url")

public class GetActiveTasks {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public TableValue action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default")
            @NotEmpty String sessionName
    ) throws IOException, ParseException {

        TodoistServer todoistServer = (TodoistServer) this.sessionMap.get(sessionName);
        String token = todoistServer.getToken();

        String url = "https://api.todoist.com/rest/v1/tasks";
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
        //Create Table for output
        Table table = new Table();
        //Create List of type "Row" objects - this will be used to store the list of Row objects
        List<Row> tableRows = new ArrayList<Row>();

        //Create Table Header row
        List<Value> headerValues = new ArrayList<>();
        headerValues.add(new StringValue("id"));
        headerValues.add(new StringValue("name"));
        headerValues.add(new StringValue("priority"));
        headerValues.add(new StringValue("due date"));
        headerValues.add(new StringValue("url"));

        Row headerRow = new Row();
        //Set header row values with the List of StringValues in rowValues
        headerRow.setValues(headerValues);
        //add Row object headerRow to the List of type Row
        tableRows.add(0,headerRow);

        Object obj = new JSONParser().parse(response);
        JSONArray array = (JSONArray) obj;

        for(int i=0; i<array.size(); i++){
            List<Value> items = new ArrayList<>();
            JSONObject arrayElement = (JSONObject) array.get(i);
            items.add(new StringValue(arrayElement.get("id").toString()));
            items.add(new StringValue(arrayElement.get("content").toString()));
            items.add(new StringValue(arrayElement.get("priority").toString()));
            if(arrayElement.get("due") != null){
                JSONObject due = (JSONObject) arrayElement.get("due");
                items.add(new StringValue(due.get("string").toString()));
            } else { items.add(new StringValue("No due date found"));}
            items.add(new StringValue(arrayElement.get("url").toString()));
            Row currentRow = new Row(items);
            tableRows.add(i+1, currentRow);
            table.setRows(tableRows);
        }

        return new TableValue(table);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
