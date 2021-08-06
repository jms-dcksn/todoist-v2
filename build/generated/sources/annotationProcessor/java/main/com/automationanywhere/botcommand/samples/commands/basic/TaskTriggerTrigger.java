package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.bot.service.TriggerException;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.core.security.SecureString;
import com.automationanywhere.toolchain.runtime.Trigger3;
import com.automationanywhere.toolchain.runtime.Trigger3ListenerContext;
import java.lang.ClassCastException;
import java.lang.Double;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskTriggerTrigger implements Trigger3 {
  private static final Logger logger = LogManager.getLogger(TaskTriggerTrigger.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  private static TaskTriggerTrigger thisInstance = new TaskTriggerTrigger();

  private final TaskTrigger command = new TaskTrigger();

  private TaskTriggerTrigger() {
    super();
  }

  public static TaskTriggerTrigger getInstance() {
    return thisInstance;
  }

  public Object clone() {
    return thisInstance;
  }

  public void startListen(Trigger3ListenerContext triggerListenerContext,
      Map<String, Value> parameters) throws TriggerException {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null);
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("token") && parameters.get("token") != null && parameters.get("token").get() != null) {
      convertedParameters.put("token", parameters.get("token").get());
      if(convertedParameters.get("token") !=null && !(convertedParameters.get("token") instanceof SecureString)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","token", "SecureString", parameters.get("token").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("token") == null) {
      throw new TriggerException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","token"));
    }

    if(parameters.containsKey("interval") && parameters.get("interval") != null && parameters.get("interval").get() != null) {
      convertedParameters.put("interval", parameters.get("interval").get());
      if(convertedParameters.get("interval") !=null && !(convertedParameters.get("interval") instanceof Double)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","interval", "Double", parameters.get("interval").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("interval") == null) {
      throw new TriggerException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","interval"));
    }
    if(convertedParameters.containsKey("interval")) {
      try {
        if(convertedParameters.get("interval") != null && !((double)convertedParameters.get("interval") > 0)) {
          throw new TriggerException(MESSAGES_GENERIC.getString("generic.validation.GreaterThan","interval", "0"));
        }
      }
      catch(ClassCastException e) {
        throw new TriggerException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","interval", "Number", convertedParameters.get("interval").getClass().getSimpleName()));
      }
      catch(NullPointerException e) {
        throw new TriggerException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","interval"));
      }
      if(convertedParameters.get("interval")!=null && !(convertedParameters.get("interval") instanceof Number)) {
        throw new TriggerException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","interval", "Number", convertedParameters.get("interval").getClass().getSimpleName()));
      }

    }
    command.setConsumer(triggerListenerContext.getEventCallback());
    command.setTriggerUid(triggerListenerContext.getTriggerUid());
    try {
      command.startTrigger((SecureString)convertedParameters.get("token"),(Double)convertedParameters.get("interval"));}
    catch (ClassCastException e) {
      throw new TriggerException(MESSAGES_GENERIC.getString("generic.IllegalParameters","startTrigger"));
    }
    catch (TriggerException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new TriggerException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }

  public void stopListen(String triggerUid) throws TriggerException {
    try {
      command.stopListen(triggerUid);}
    catch (TriggerException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new TriggerException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }

  public void stopAllTriggers() throws TriggerException {
    try {
      command.stopAllTriggers();}
    catch (TriggerException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new TriggerException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
