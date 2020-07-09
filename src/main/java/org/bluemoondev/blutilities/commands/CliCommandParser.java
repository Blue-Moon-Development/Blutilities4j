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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Options;
import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.cli.ArgumentParser;
import org.bluemoondev.blutilities.cli.Helper;
import org.bluemoondev.blutilities.cli.OptionImpl;
import org.bluemoondev.blutilities.collections.UnmodifiableBiPair;
import org.bluemoondev.blutilities.errors.Checks;
import org.bluemoondev.blutilities.errors.Errors;
import org.bluemoondev.blutilities.errors.exceptions.CommandException;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> CliCommandParser.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class CliCommandParser extends CommandParser {

    private Map<String, ArgumentParser> argParsers;

    public CliCommandParser(Class<?> clazz, Command cmd) {
        super(clazz, cmd);
    }

    @Override
    public void parse(String[] args, boolean canRun, CommandFallback fallback) {
        Errors error = Errors.SUCCESS;
        if(!canRun) error = Errors.COMMAND_PARSER_NO_PERMISSION;
        else if (!hasSubCommands)
            error = getArgParser(name).parse(args);
        else {
            if (!subCommands.contains(args[0])) error = Errors.COMMAND_PARSER_INVALID_SUB_COMMAND;
            else for (String sub : subCommands) {
                if (sub.equalsIgnoreCase(args[0])) {
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    error = getArgParser(sub).parse(newArgs);
                    break;
                }
            }

        }

        fallback.fallback(error);
    }

    @Override
    protected void init(UnmodifiableBiPair<Options, List<OptionImpl>> pair) {
        argParsers = new HashMap<>();
        if (!Checks.isNotNull(cmdInfo)) return;
        List<OptionImpl> wrappers = pair.second;
        hasSubCommands = cmdInfo.subCmds();
        Helper helper = new Helper();
        if (hasSubCommands) {
            Map<String, UnmodifiableBiPair<Options, List<OptionImpl>>> args = new HashMap<>();
            for (OptionImpl oi : wrappers) {
                if (oi.getCmd() == null || oi.getCmd().equals(""))
                    throw new CommandException(Errors.COMMAND_PARSER_INVALID_SUB_COMMAND,
                                               "The Command Handler is expecting sub commands,"
                                                       + " but there are arguments not attached to any");
                String subCmd = oi.getCmd();
                if (oi.isRequired()) numArgsRequired++;
                if (args.containsKey(subCmd)) {
                    args.get(subCmd).first.addOption(oi);
                    args.get(subCmd).second.add(oi);
                } else {
                    subCommands.add(oi.getCmd());
                    args.put(subCmd, new UnmodifiableBiPair<>(new Options(), new ArrayList<>()));
                    args.get(subCmd).first.addOption(oi);
                    args.get(subCmd).second.add(oi);
                }
            }

            args.forEach((k, v) -> {
                argParsers.put(k, new ArgumentParser(k, v.first, v.second));
                helpMap.put(k, helper.getFormatted(argParsers.get(k).getUtilityName(), argParsers.get(k).getOptions()));

                String[] strs = new String[v.second.size()];
                Class<?>[] types = new Class<?>[wrappers.size()];
                for (int i = 0; i < strs.length; i++) {
                    strs[i] = v.second.get(i).getLongOpt();
                    types[i] = v.second.get(i).getActualType();
                    String key = v.second.get(i).getCmd();
                    defaultValues.put(key + strs[i], v.second.get(i).getDefaultValue());
                }
                argNames.put(v.second.get(0).getCmd(), strs);
                argTypes.put(v.second.get(0).getCmd(), types);
            });
        } else {
            String[] strs = new String[wrappers.size()];
            Class<?>[] types = new Class<?>[wrappers.size()];
            if (!useCli)
                helpMap.put(name, name);
            for (int i = 0; i < wrappers.size(); i++) {
                OptionImpl ow = wrappers.get(i);
                if (ow.isRequired()) numArgsRequired++;
                strs[i] = ow.getLongOpt();
                types[i] = ow.getActualType();
                defaultValues.put(strs[i], ow.getDefaultValue());
            }
            argNames.put(name, strs);
            argTypes.put(name, types);
            argParsers.put(name, new ArgumentParser(name, pair.first, pair.second));
            helpMap.put(name,
                        helper.getFormatted(argParsers.get(name).getUtilityName(), argParsers.get(name).getOptions()));
        }
    }

    public ArgumentParser getArgParser(String name) {
        return argParsers.get(name);
    }

    @Override
    public String get(String argName) {
        if (hasSubCommands) {
            for (String s : subCommands) {
                if (Checks.isNotNull(getArgParser(s).get(argName)))
                    return getArgParser(s).get(argName);
            }
        }
        return getArgParser(name).get(argName);
    }

}
