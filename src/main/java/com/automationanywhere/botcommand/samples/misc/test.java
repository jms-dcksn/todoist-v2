package com.automationanywhere.botcommand.samples.misc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.samples.Utils.TodoistActions;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class test {

    public static void main(String[] args) throws Exception {
        /*HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();*/
        /*ZonedDateTime lastRun;
        lastRun = ZonedDateTime.now(ZoneOffset.UTC);
        System.out.println(lastRun);
        String token = "3506cfbaca226d3d6664905a2338abcdc89b2de1";
        JSONArray total = TodoistActions.getTasks(token);
        //Get the last object
        JSONObject lastTask = (JSONObject) total.get(total.size()-1);
        JSONObject secondlastTask = (JSONObject) total.get(total.size()-2);
        String creationTime = (String) lastTask.get("created");
        ZonedDateTime creationTimeZ = ZonedDateTime.parse(creationTime);
        System.out.println(creationTimeZ);

        if(lastRun.compareTo(creationTimeZ) > 0){
            System.out.println("Trigger will happen");
        }else{System.out.println("Trigger won't happen");}
        long difference = creationTimeZ.compareTo(lastRun);
        System.out.println(difference);*/

        String token = "3506cfbaca226d3d6664905a2338abcdc89b2de1";
        String taskId = "5050852812";
        String taskName = "Prep to crush it on Friday again!";
        String dueDate = "2021-08-08";
        Integer numPrio = 4;

        //JSONObject details = TodoistActions.CreateTask(token, taskName, null, dueDate, numPrio);

        TodoistActions.CloseTask(token, "5054012585");

        //System.out.println(details.get("id").toString());


    }


}



