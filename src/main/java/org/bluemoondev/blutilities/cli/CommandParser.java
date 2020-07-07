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
package org.bluemoondev.blutilities.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.cli.ArgumentParser.Fallback;
import org.bluemoondev.blutilities.collections.ArrayUtil;

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
    private Map<String, String>         defaultValues;
    private List<String>                subCommands;

    private boolean hasSubCommands;
    private String  name;
    private boolean useCli;
    private int     numArgsRequired;

    public CommandParser() {
        argParsers = new HashMap<>();
        values = new HashMap<>();
        argNames = new HashMap<>();
        subCommands = new ArrayList<>();
        defaultValues = new HashMap<>();
    }

    public void init(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Command.class)) return;
        Command c = clazz.getAnnotation(Command.class);
        if (c.allowNoArgs()) return;
        name = c.name();
        useCli = c.useCli();
        List<OptionWrapper> wrappers = ArgumentUtil.getArguments(clazz);
        if (c.subCmds()) {
            hasSubCommands = true;
            Map<String, List<OptionWrapper>> args = new HashMap<>();
            for (OptionWrapper ow : wrappers) {
                // if(ow.getCmd() == null || ow.getCmd() == "") throw new
                // CommandException("Arguments expected to be mapped to sub commands in " +
                // clazz.getName());
                if (ow.getCmd() != null && ow.getCmd() != "") {
                    String usage = c.name() + " " + ow.getCmd();
                    if (ow.getOption().isRequired())
                        numArgsRequired++;
                    if (args.containsKey(usage))
                        args.get(usage).add(ow);
                    else {
                        subCommands.add(ow.getCmd());
                        args.put(usage, new ArrayList<OptionWrapper>());
                        args.get(usage).add(ow);
                    }
                }
            }

            args.forEach((k, v) -> {
                argParsers.put(k, new ArgumentParser(k, v));
                String[] strs = new String[v.size()];
                for (int i = 0; i < strs.length; i++) {
                    strs[i] = v.get(i).getOption().getLongOpt();
                    defaultValues.put(strs[i], v.get(i).getDefaultValue());
                }
                argNames.put(v.get(0).getCmd(), strs);
            });


        } else {
            String[] strs = new String[wrappers.size()];
            for (int i = 0; i < wrappers.size(); i++) {
                OptionWrapper ow = wrappers.get(i);
                if (ow.getOption().isRequired()) numArgsRequired++;
                strs[i] = ow.getOption().getLongOpt();
                defaultValues.put(strs[i], ow.getDefaultValue());
            }
            argNames.put(c.name(), strs);
            argParsers.put(c.name(), new ArgumentParser(c.name(), wrappers));
        }
    }

    public ArgumentParser getArgParser(String name) {
        return argParsers.get(name);
    }

    public String get(String argName) {
        if (!useCli) return values.get(argName);
        if (hasSubCommands) {
            for (String s : subCommands)
                if (getArgParser(name + " " + s).get(argName) != null)
                    return getArgParser(name + " " + s).get(argName);
        }
        return getArgParser(name).get(argName);
    }

    public boolean parseWithCLI(String[] args, Fallback error) {
        if (!hasSubCommands)
            return getArgParser(name).parse(args, error);
        else {
            for (String sub : subCommands) {
                if (sub.equalsIgnoreCase(args[0])) {
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    return getArgParser(name + " " + sub).parse(newArgs, error);
                }
            }
        }
        return false;
    }

    public boolean parseWithoutCli(String[] args) {
        if (args == null || args.length == 0) return false;
        if (hasSubCommands) {
            if (!subCommands.contains(args[0])) return false;
            for (String sub : subCommands) {
                if (sub.equalsIgnoreCase(args[0])) {
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

                    if (newArgs.length < numArgsRequired) return false;
                    for (int i = 0; i < newArgs.length; i++)
                        values.put(argNames.get(sub)[i], newArgs[i]);
                    if(argNames.get(name).length > newArgs.length) {
                        for(int i = newArgs.length; i < argNames.get(name).length; i++)
                            values.put(argNames.get(name)[i], defaultValues.get(argNames.get(name)[i]));
                    }
                    return true;
                }
            }
        }else {
            if(args.length < numArgsRequired) return false;
            for (int i = 0; i < args.length; i++)
                values.put(argNames.get(name)[i], args[i]);
            if(argNames.get(name).length > args.length) {
                for(int i = args.length; i < argNames.get(name).length; i++)
                    values.put(argNames.get(name)[i], defaultValues.get(argNames.get(name)[i]));
            }
            return true;
        }
        return false;
    }

    public boolean parse(String[] args, boolean canRun, Fallback error) {
        if (!canRun) return false;
        if (useCli) return parseWithCLI(args, error);
        return parseWithoutCli(ArrayUtil.combineArgsWithSpaces(args));
    }

}
