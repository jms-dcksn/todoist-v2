package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CreateTaskCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(CreateTaskCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    CreateTask command = new CreateTask();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("sessionName") && parameters.get("sessionName") != null && parameters.get("sessionName").get() != null) {
      convertedParameters.put("sessionName", parameters.get("sessionName").get());
      if(convertedParameters.get("sessionName") !=null && !(convertedParameters.get("sessionName") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","sessionName", "String", parameters.get("sessionName").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("sessionName") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","sessionName"));
    }

    if(parameters.containsKey("taskName") && parameters.get("taskName") != null && parameters.get("taskName").get() != null) {
      convertedParameters.put("taskName", parameters.get("taskName").get());
      if(convertedParameters.get("taskName") !=null && !(convertedParameters.get("taskName") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","taskName", "String", parameters.get("taskName").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("taskName") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","taskName"));
    }

    if(parameters.containsKey("projectId") && parameters.get("projectId") != null && parameters.get("projectId").get() != null) {
      convertedParameters.put("projectId", parameters.get("projectId").get());
      if(convertedParameters.get("projectId") !=null && !(convertedParameters.get("projectId") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","projectId", "String", parameters.get("projectId").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("dueDate") && parameters.get("dueDate") != null && parameters.get("dueDate").get() != null) {
      convertedParameters.put("dueDate", parameters.get("dueDate").get());
      if(convertedParameters.get("dueDate") !=null && !(convertedParameters.get("dueDate") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","dueDate", "String", parameters.get("dueDate").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("priority") && parameters.get("priority") != null && parameters.get("priority").get() != null) {
      convertedParameters.put("priority", parameters.get("priority").get());
      if(convertedParameters.get("priority") !=null && !(convertedParameters.get("priority") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","priority", "String", parameters.get("priority").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("priority") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","priority"));
    }
    if(convertedParameters.get("priority") != null) {
      switch((String)convertedParameters.get("priority")) {
        case "1" : {

        } break;
        case "2" : {

        } break;
        case "3" : {

        } break;
        case "4" : {

        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","priority"));
      }
    }

    command.setSessionMap(sessionMap);
    try {
      Optional<Value> result =  Optional.ofNullable(command.action((String)convertedParameters.get("sessionName"),(String)convertedParameters.get("taskName"),(String)convertedParameters.get("projectId"),(String)convertedParameters.get("dueDate"),(String)convertedParameters.get("priority")));
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","action"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
