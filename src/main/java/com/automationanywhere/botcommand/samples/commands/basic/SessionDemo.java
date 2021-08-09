/*
 * Copyright (c) 2020 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */

/**
 * @author James Dickson
 */
package com.automationanywhere.botcommand.samples.commands.basic;

import static com.automationanywhere.commandsdk.model.AttributeType.CREDENTIAL;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import com.automationanywhere.botcommand.samples.Utils.TodoistServer;
import java.util.Map;

import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.Sessions;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.core.security.SecureString;

/**
 * @author James Dickson
 */
@BotCommand
@CommandPkg(label = "Start Session",
		description = "Starts Session",
		icon = "todo.svg",
		name = "startSession",
		node_label = "Start Session {{sessionName}}",
		group_label="Admin",
		comment = true ,
		text_color = "#7B848B" ,
		background_color =  "#a6a6a6")
public class SessionDemo {

	@Sessions
	private Map<String, Object> sessionMap;

	@Execute
	public void execute(@Idx(index = "1", type = TEXT)
	@Pkg(label = "Start Session", default_value_type = STRING, default_value = "Default")
	@NotEmpty String name,
	@Idx(index = "2", type = CREDENTIAL) @Pkg(label = "Account Token") @NotEmpty SecureString token) {
		String accountToken=null;
		if (!sessionMap.containsKey(name))
			accountToken = token.getInsecureString();
			TodoistServer todoServer = new TodoistServer(accountToken);
			sessionMap.put(name, todoServer);
	}

	// Ensure that a public setter exists.
	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

}
