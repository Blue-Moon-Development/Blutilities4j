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
import org.bluemoondev.blutilities.collections.ArrayUtil;
import org.bluemoondev.blutilities.collections.UnmodifiableBiPair;
import org.bluemoondev.blutilities.errors.Checks;
import org.bluemoondev.blutilities.errors.Errors;
import org.bluemoondev.blutilities.errors.exceptions.CommandException;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> StandardCommandParser.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class StandardCommandParser extends CommandParser {

    private Map<String, String> values;

    public StandardCommandParser(Class<?> clazz, Command cmd) {
        super(clazz, cmd);
    }

    //TODO Sort arrays so that the required arguments are first
    @Override
    protected void init(UnmodifiableBiPair<Options, List<OptionImpl>> pair) {
        values = new HashMap<>();
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
                    helpMap.put(subCmd, name + " " + subCmd);
                }
            }

            args.forEach((k, v) -> {
                String[] strs = new String[v.second.size()];
                Class<?>[] types = new Class<?>[wrappers.size()];
                for (int i = 0; i < strs.length; i++) {
                    strs[i] = v.second.get(i).getLongOpt();
                    types[i] = v.second.get(i).getActualType();
                    System.out.println(strs[i] + "  ---->   " + types[i]);
                    String key = v.second.get(i).getCmd();
                    helpMap.put(key, helpMap.get(key) + " " + strs[i]);
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
                helpMap.put(name, helpMap.get(name) + " " + strs[i]);
                defaultValues.put(strs[i], ow.getDefaultValue());
            }
            argNames.put(name, strs);
            argTypes.put(name, types);
        }
    }

    @Override
    public void parse(String[] args, boolean canRun, CommandFallback cmdFallback) {
        Errors error = Errors.SUCCESS;
        String[] actualArgs = null;
        if (!canRun) error = Errors.COMMAND_PARSER_NO_PERMISSION;
        else if (args == null || args.length == 0) error = Errors.COMMAND_PARSER_NULL_OR_EMPTY_ARGS;
        else if (hasSubCommands) {
            actualArgs = ArrayUtil.combineArgsWithSpaces(args);
            if (!subCommands.contains(args[0])) error = Errors.COMMAND_PARSER_INVALID_SUB_COMMAND;
            else for (String sub : subCommands) {
                if (sub.equalsIgnoreCase(args[0])) {
                    String[] newArgs = Arrays.copyOfRange(actualArgs, 1, actualArgs.length);

                    if (newArgs.length < numArgsRequired) error = Errors.COMMAND_PARSER_NOT_ENOUGH_ARGS;
                    else {
                        int len = argNames.get(sub).length < newArgs.length ? argNames.get(sub).length : newArgs.length;
                        System.out.println(ArrayUtil.formatArray(argTypes.get(sub)));
                        System.out.println(ArrayUtil.formatArray(argNames.get(sub)));
                        for (int i = 0; i < len; i++) {
                            Class<?> type = argTypes.get(sub)[i];
                            if (Checks.isNumber(type))
                                if (!Checks.isNumberOfType(type, newArgs[i])) {
                                    error = Errors.COMMAND_PARSER_NUMBER_EXPECTED;
                                    break;
                                }
                            values.put(sub + argNames.get(sub)[i], newArgs[i]);
                        }
                        if (error == Errors.SUCCESS && argNames.get(sub).length > newArgs.length) {
                            for (int i = newArgs.length; i < argNames.get(sub).length; i++)
                                values.put(sub + argNames.get(sub)[i], defaultValues.get(sub + argNames.get(sub)[i]));
                        }
                    }
                }
            }
        } else {
            actualArgs = ArrayUtil.combineArgsWithSpaces(args);
            if (actualArgs.length < numArgsRequired) error = Errors.COMMAND_PARSER_NOT_ENOUGH_ARGS;
            else {
                int len = argNames.get(name).length < actualArgs.length ? argNames.get(name).length : actualArgs.length;
                for (int i = 0; i < len; i++) {
                    Object type = argTypes.get(name + argNames.get(name)[i])[i];
                    if (Checks.isNumber(type))
                        if (!Checks.isNumberOfType(type, actualArgs[i])) {
                            error = Errors.COMMAND_PARSER_NUMBER_EXPECTED;
                            break;
                        }
                    values.put(argNames.get(name)[i], actualArgs[i]);
                }
                if (error == Errors.SUCCESS && argNames.get(name).length > actualArgs.length) {
                    for (int i = actualArgs.length; i < argNames.get(name).length; i++)
                        values.put(argNames.get(name)[i], defaultValues.get(argNames.get(name)[i]));
                }
            }
        }

        cmdFallback.fallback(error);
    }

    @Override
    public String get(String argName) {
        if (hasSubCommands) {
            for (String s : subCommands) { if (values.get(s + argName) != null)
                return values.get(s + argName); }
        }
        return values.get(argName);
    }

}
