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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bluemoondev.blutilities.Blutil;
import org.bluemoondev.blutilities.collections.ArrayUtil;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> ArgumentParser.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class ArgumentParser {

    private CommandLine cmd;
    private String      utilityName;
    private Options     options;

    private List<OptionWrapper>        wrappers;
    private Map<String, OptionWrapper> wrapperMap;

    public ArgumentParser(String utilityName, List<OptionWrapper> wrappers) {
        this.utilityName = utilityName;
        wrapperMap = new HashMap<String, OptionWrapper>();
        this.wrappers = wrappers;
        options = new Options();
        for(OptionWrapper ow : wrappers) {
            options.addOption(ow.getOption());
        }
    }

    public boolean parse(String[] args, Fallback error) {
        String[] actualArgs = ArrayUtil.combineArgsWithSpaces(args);

        for (OptionWrapper ow : wrappers) { wrapperMap.put(ow.getOption().getLongOpt(), ow); }
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, actualArgs);
        } catch (ParseException ex) {
            error.onError(utilityName, options);
            return false;
        }
        
        return true;

    }

    public String formatHelp(String cmdNameAndSyntax, Options options) {
        Helper help = new Helper();
        return help.getFormatted(cmdNameAndSyntax, options);
    }

    // TODO Create overloaded methods for int, long, etc
    // get(String name, Class<?> type)
    public String get(String name) {
        if(cmd == null) return null;
        return cmd.getOptionValue(name, wrapperMap.get(name).getDefaultValue());
    }



    @FunctionalInterface
    public interface Fallback {
        public void onError(String name, Options options);
    }
}
