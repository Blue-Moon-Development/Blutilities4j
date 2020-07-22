/*
 * Copyright (C) 2020 Blue Moon Development
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bluemoondev.blutilities.commands;

import org.apache.commons.cli.Options;
import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.cli.ArgumentUtil;
import org.bluemoondev.blutilities.cli.OptionImpl;
import org.bluemoondev.blutilities.collections.UnmodifiableBiPair;
import org.bluemoondev.blutilities.errors.Errors;
import org.bluemoondev.blutilities.errors.exceptions.CommandException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> CommandParser.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public abstract class CommandParser implements ICommandParser {

	protected Map<String, String[]>   argNames;
	protected Map<String, Class<?>[]> argTypes;
	protected Map<String, String>     defaultValues;
	protected List<String>            subCommands;
	protected Map<String, String>     helpMap;

	protected boolean hasSubCommands;
	protected String  name;
	protected String  description;
	protected boolean useCli;
	protected int     numArgsRequired;
	protected Command cmdInfo;

	public CommandParser(Class<?> clazz, Command cmd) {
		argNames = new HashMap<>();
		argTypes = new HashMap<>();
		subCommands = new ArrayList<>();
		defaultValues = new HashMap<>();
		helpMap = new HashMap<>();
		cmdInfo = cmd;
		if (cmdInfo == null) {
			if (!clazz.isAnnotationPresent(Command.class)) {
				throw new CommandException(Errors.COMMAND_EXPECTED_ANNOTATION,
										   "A Command Parser cannot be created for a class with no @Command " +
                                           "annotation");
			}
			cmdInfo = clazz.getClass().getAnnotation(Command.class);
		}
		if (cmdInfo.allowNoArgs())
			throw new CommandException(Errors.COMMAND_PARSER_NULL_OR_EMPTY_ARGS,
									   "Command was marked has having no arguments, a parser cannot be created for " +
                                       "this");
		description = cmdInfo.description();
		UnmodifiableBiPair<Options, List<OptionImpl>> pair = ArgumentUtil.getArguments(clazz);
		init(pair);
	}

	public CommandParser(Class<?> clazz) {
		this(clazz, null);
	}

	protected abstract void init(UnmodifiableBiPair<Options, List<OptionImpl>> pair);

	public abstract String get(String argName);

	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		if(!description.equalsIgnoreCase(""))
		    sb.append(name + ": " + description).append("\n");
		helpMap.forEach((k, v) -> {
			sb.append(v).append("\n");
		});
		return sb.toString();
	}

	public String getHelp(String subCmd) {
		if (!helpMap.containsKey(subCmd)) { return getHelp(); }
		return helpMap.get(subCmd);
	}

	public String getName() { return this.name; }

	public boolean hasSubCommands() {
		return this.hasSubCommands;
	}

}
