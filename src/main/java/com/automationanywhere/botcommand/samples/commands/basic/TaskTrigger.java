package com.automationanywhere.botcommand.samples.commands.basic;


import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.RecordValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.record.Record;
import com.automationanywhere.botcommand.samples.Utils.TodoistTaskActions;
import com.automationanywhere.commandsdk.annotations.*;

import static com.automationanywhere.commandsdk.model.DataType.RECORD;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import com.automationanywhere.commandsdk.annotations.rules.GreaterThan;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.NumberInteger;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@BotCommand(commandType = BotCommand.CommandType.Trigger)
@CommandPkg(label = "Task Trigger", description = "Task Trigger", icon = "todo.svg", name = "todoisttasktrigger",
        return_type = RECORD, return_name = "TriggerData", return_description = "Available keys: triggerType, id")

public class TaskTrigger {

    private ZonedDateTime lastRun;
    // Map storing multiple tasks
    private static final Map<String, TimerTask> taskMap = new ConcurrentHashMap<>();
    private static final Timer TIMER = new Timer(true);

    @TriggerId
    private String triggerUid;
    @TriggerConsumer
    private Consumer consumer;

    /*
     * Starts the trigger.
     */
    @StartListen
    public void startTrigger(
            @Idx(index = "1", type = AttributeType.CREDENTIAL) @Pkg(label = "API token",  default_value_type = STRING) @NotEmpty SecureString token,
            @Idx(index = "2", type = AttributeType.NUMBER)
                             @Pkg(label = "Please provide the interval to trigger in seconds", default_value = "120", default_value_type = DataType.NUMBER)
                             @GreaterThan("0")
                             @NumberInteger
                             @NotEmpty
                                     Double interval) {

        String strToken = token.getInsecureString();
        JSONArray total = TodoistTaskActions.getTasks(strToken);
        //Get the last object
        JSONObject lastTask = (JSONObject) total.get(total.size()-1);
        final Long[] lastTaskId = {(Long) lastTask.get("id")};
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                JSONArray total = TodoistTaskActions.getTasks(strToken);
                //Get the last object
                JSONObject lastTask = (JSONObject) total.get(total.size()-1);
                Long currentTaskid = (Long) lastTask.get("id");
                if(currentTaskid > lastTaskId[0]){
                    String strCurrentTask = currentTaskid.toString();
                    lastTaskId[0] = currentTaskid;
                    consumer.accept(getRecordValue(strCurrentTask));
                    return;
                }
            }
        };
        taskMap.put(this.triggerUid, timerTask);
        TIMER.schedule(timerTask, interval.longValue()*1000, interval.longValue()*1000);
    }

    private RecordValue getRecordValue(String id) {
        List<Schema> schemas = new LinkedList<>();
        List<Value> values = new LinkedList<>();
        schemas.add(new Schema("triggerType"));
        values.add(new StringValue("Todoist Task"));
        schemas.add(new Schema("id"));
        values.add(new StringValue(id));

        RecordValue recordValue = new RecordValue();
        recordValue.set(new Record(schemas,values));
        return recordValue;
    }

    /*
     * Cancel all the task and clear the map.
     */
    @StopAllTriggers
    public void stopAllTriggers() {
        taskMap.forEach((k, v) -> {
            if (v.cancel()) {
                taskMap.remove(k);
            }
        });
    }

    /*
     * Cancel the task and remove from map
     *
     * @param triggerUid
     */
    @StopListen
    public void stopListen(String triggerUid) {
        if (taskMap.get(triggerUid).cancel()) {
            taskMap.remove(triggerUid);
        }
    }

    public String getTriggerUid() {
        return triggerUid;
    }

    public void setTriggerUid(String triggerUid) {
        this.triggerUid = triggerUid;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

}



