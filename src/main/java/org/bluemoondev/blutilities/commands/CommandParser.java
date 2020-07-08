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
import org.bluemoondev.blutilities.cli.ArgumentUtil;
import org.bluemoondev.blutilities.cli.Helper;
import org.bluemoondev.blutilities.cli.OptionImpl;
import org.bluemoondev.blutilities.collections.ArrayUtil;
import org.bluemoondev.blutilities.collections.UnmodifiableBiPair;
import org.bluemoondev.blutilities.errors.Checks;
import org.bluemoondev.blutilities.errors.Errors;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> CommandParser.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class CommandParser {

    private Map<String, ArgumentParser> argParsers;
    private Map<String, String>         values;
    private Map<String, String[]>       argNames;
    private Map<String, Class<?>[]>     argTypes;
    private Map<String, String>         defaultValues;
    private List<String>                subCommands;
    private Map<String, String>         helpMap;

    private boolean hasSubCommands;
    private String  name;
    private boolean useCli;
    private int     numArgsRequired;

    public CommandParser() {
        argParsers = new HashMap<>();
        values = new HashMap<>();
        argNames = new HashMap<>();
        argTypes = new HashMap<>();
        subCommands = new ArrayList<>();
        defaultValues = new HashMap<>();
        helpMap = new HashMap<>();
    }

    public void init(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Command.class)) return;
        Command c = clazz.getAnnotation(Command.class);
        if (c.allowNoArgs()) return;
        name = c.name();
        useCli = c.useCli();
        UnmodifiableBiPair<Options, List<OptionImpl>> pair = ArgumentUtil.getArguments(clazz);
        List<OptionImpl> wrappers = pair.second;
        if (c.subCmds()) {
            hasSubCommands = true;
            Map<String, UnmodifiableBiPair<Options, List<OptionImpl>>> args = new HashMap<>();
            for (OptionImpl ow : wrappers) {
                // if(ow.getCmd() == null || ow.getCmd() == "") throw new
                // CommandException("Arguments expected to be mapped to sub commands in " +
                // clazz.getName());
                if (ow.getCmd() != null && ow.getCmd() != "") {
                    String subCmd = ow.getCmd();
                    if (ow.isRequired())
                        numArgsRequired++;
                    if (args.containsKey(subCmd)) {
                        args.get(subCmd).first.addOption(ow);
                        args.get(subCmd).second.add(ow);
                    }else {
                        subCommands.add(ow.getCmd());
                        args.put(subCmd, new UnmodifiableBiPair<Options, List<OptionImpl>>(new Options(),
                                                                                           new ArrayList<OptionImpl>()));
                        args.get(subCmd).first.addOption(ow);
                        args.get(subCmd).second.add(ow);
                        if (!useCli)
                            helpMap.put(subCmd, name + " " + subCmd);
                    }
                }
            }

            args.forEach((k, v) -> {
                argParsers.put(k, new ArgumentParser(k, v.first, v.second));
                if (useCli) {
                    Helper helper = new Helper();
                    helpMap.put(k, helper
                            .getFormatted(argParsers.get(k).getUtilityName(), argParsers.get(k).getOptions()));
                }
                String[] strs = new String[v.second.size()];
                Class<?>[] types = new Class<?>[wrappers.size()];
                for (int i = 0; i < strs.length; i++) {
                    strs[i] = v.second.get(i).getLongOpt();
                    types[i] = v.second.get(i).getActualType();
                    String key = v.second.get(i).getCmd();
                    if (!useCli)
                        helpMap.put(key, helpMap.get(key) + " " + strs[i]);
                    defaultValues.put(strs[i], v.second.get(i).getDefaultValue());
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
                if (!useCli)
                    helpMap.put(name, helpMap.get(name) + " " + strs[i]);
                defaultValues.put(strs[i], ow.getDefaultValue());
            }
            argNames.put(c.name(), strs);
            argTypes.put(c.name(), types);
            argParsers.put(c.name(), new ArgumentParser(c.name(), pair.first, pair.second));
            if (useCli) {
                Helper helper = new Helper();
                helpMap.put(name, helper
                        .getFormatted(argParsers.get(name).getUtilityName(), argParsers.get(name).getOptions()));
            }
        }
    }

    public ArgumentParser getArgParser(String name) {
        return argParsers.get(name);
    }

    public String get(String argName) {
        if (!useCli) return values.get(argName);
        if (hasSubCommands) {
            for (String s : subCommands)
                if (getArgParser(s).get(argName) != null)
                    return getArgParser(s).get(argName);
        }
        return getArgParser(name).get(argName);
    }

    public Errors parseWithCLI(String[] args) {
        if (!hasSubCommands)
            return getArgParser(name).parse(args);
        else {
            if (!subCommands.contains(args[0])) return Errors.COMMAND_PARSER_INVALID_SUB_COMMAND; // TODO get help
            for (String sub : subCommands) {
                if (sub.equalsIgnoreCase(args[0])) {
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    return getArgParser(sub).parse(newArgs);
                }
            }

        }
        return Errors.FAILURE;
    }

    public Errors parseWithoutCli(String[] args) {
        if (args == null || args.length == 0) return Errors.COMMAND_PARSER_NULL_OR_EMPTY_ARGS;
        if (hasSubCommands) {
            if (!subCommands.contains(args[0])) return Errors.COMMAND_PARSER_INVALID_SUB_COMMAND;
            for (String sub : subCommands) {
                if (sub.equalsIgnoreCase(args[0])) {
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

                    if (newArgs.length < numArgsRequired) return Errors.COMMAND_PARSER_NOT_ENOUGH_ARGS;
                    int len = argNames.get(sub).length < newArgs.length ? argNames.get(sub).length : newArgs.length;
                    for (int i = 0; i < len; i++) {
                        Class<?> type = argTypes.get(sub)[i];
                        if (Checks.isNumber(type))
                            if (!Checks.isNumberOfType(type, newArgs[i])) return Errors.COMMAND_PARSER_NUMBER_EXPECTED;
                        values.put(argNames.get(sub)[i], newArgs[i]);
                    }
                    if (argNames.get(sub).length > newArgs.length) {
                        for (int i = newArgs.length; i < argNames.get(sub).length; i++)
                            values.put(argNames.get(sub)[i], defaultValues.get(argNames.get(sub)[i]));
                    }
                    return Errors.SUCCESS;
                }
            }
        } else {
            if (args.length < numArgsRequired) return Errors.COMMAND_PARSER_NOT_ENOUGH_ARGS;
            int len = argNames.get(name).length < args.length ? argNames.get(name).length : args.length;
            for (int i = 0; i < len; i++) {
                Object type = argTypes.get(name)[i];
                if (Checks.isNumber(type))
                    if (!Checks.isNumberOfType(type, args[i])) return Errors.COMMAND_PARSER_NUMBER_EXPECTED;
                values.put(argNames.get(name)[i], args[i]);
            }
            if (argNames.get(name).length > args.length) {
                for (int i = args.length; i < argNames.get(name).length; i++)
                    values.put(argNames.get(name)[i], defaultValues.get(argNames.get(name)[i]));
            }
            return Errors.SUCCESS;
        }
        return Errors.FAILURE;
    }

    // Instead of fallback, maybe a separate error collector for handling int
    // returns?
    // I.e -> parse(args, true, e -> {
    // if(cmd.usesCli) str help = format cli help
    // if(e == 1) print not enough arguments
    // if(e == 2) expected sub command
    // etc...
    // });
    public void parse(String[] args, boolean canRun, CommandFallback cmdFallback) {
        Errors error = Errors.SUCCESS;
        if (!canRun) error = Errors.COMMAND_PARSER_NO_PERMISSION;
        if (useCli) error = parseWithCLI(args);
        else error = parseWithoutCli(ArrayUtil.combineArgsWithSpaces(args));
        cmdFallback.fallback(error);
    }

    public String getHelp() {
        StringBuilder sb = new StringBuilder();
        helpMap.forEach((k, v) -> {
            sb.append(v).append("\n");
        });
        return sb.toString();
    }

    public String getHelp(String subCmd) {
        if (!argParsers.containsKey(subCmd)) { return getHelp(); }
        return helpMap.get(subCmd);
    }

    @FunctionalInterface
    public interface CommandFallback {
        // TODO Some how allow optional parameters for cmd name and options to get help
        // format for cli?
        public void fallback(Errors error);
    }

}
