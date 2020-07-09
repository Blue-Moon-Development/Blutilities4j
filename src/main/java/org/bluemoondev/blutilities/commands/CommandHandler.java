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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.collections.ArrayUtil;
import org.bluemoondev.blutilities.errors.Errors;
import org.bluemoondev.blutilities.errors.exceptions.CommandException;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> CommandHandler.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class CommandHandler {

    private Map<String, ICommand>      commands;
    private Map<String, CommandParser> parsers;
    private Map<String, Boolean>       subCommandMap;

    public CommandHandler() {
        commands = new HashMap<>();
        parsers = new HashMap<>();
        subCommandMap = new HashMap<>();
    }

    public void addCommand(ICommand cmd) {
        if (!cmd.getClass().isAnnotationPresent(Command.class)) {
            throw new CommandException(Errors.COMMAND_EXPECTED_ANNOTATION,
                                       "An ICommand can not be added to the default Blutilties4j "
                                                                           + "Command Handler without the"
                                                                           + " @Command annotation present");
        }
        Command c = cmd.getClass().getAnnotation(Command.class);
        if (!c.allowNoArgs()) {
            CommandParser parser = null;
            if(c.useCli())
                parser = new CliCommandParser(cmd.getClass(), c);
            else parser = new StandardCommandParser(cmd.getClass(), c);
            parsers.put(c.name(), parser);
        }
        
        commands.put(c.name(), cmd);
        subCommandMap.put(c.name(), c.subCmds());
    }

    public Errors execute(String cmd, String[] args, boolean canRun, CommandConsumer consumer) {
        ICommand c = commands.get(cmd);
        Errors[] ret = new Errors[1];
        ret[0] = Errors.SUCCESS;
        if (c == null) return Errors.COMMAND_HANDLER_INVALID_COMMAND;
        if (parsers.get(cmd) != null) {
            parsers.get(cmd).parse(args, canRun, error -> {
                ret[0] = error;
                if (error == Errors.SUCCESS || error == Errors.NO_ERROR) {
                    if (parsers.get(cmd).hasSubCommands()) {
                        c.preRun(args[0], parsers.get(cmd));
                        consumer.consume(args[0], Arrays.copyOfRange(args, 1, args.length));
                    } else {
                        c.preRun(null, parsers.get(cmd));
                        consumer.consume(null, args);
                    }
                }
            });
        } else {
            if (subCommandMap.get(cmd))
                consumer.consume(args[0], Arrays.copyOfRange(args, 1, args.length));
            else consumer.consume(null, args);
        }

        return ret[0];
    }

    public ICommand getCommand(String name) {
        return commands.get(name);
    }

    public CommandParser getParser(String name) {
        return parsers.get(name);
    }

    @FunctionalInterface
    public interface CommandConsumer {
        public void consume(String subCmd, String[] args);
    }

}
