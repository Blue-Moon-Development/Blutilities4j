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
	private String utilityName;

	private Map<String, OptionWrapper> wrapperMap;

	public ArgumentParser(String utilityName) {
		this.utilityName = utilityName;
		wrapperMap = new HashMap<String, OptionWrapper>();
	}

	public void parse(Class<?> clazz, String[] args, Fallback error) {
		List<OptionWrapper> wrappers = ArgumentUtil.getArguments(clazz);
		Options options = ArgumentUtil.getOptions(wrappers);

		String[] actualArgs = combineArgsWithSpaces(args);

		// TODO: Might need to be long name instead of arg name
		for (OptionWrapper ow : wrappers) { wrapperMap.put(ow.getOption().getLongOpt(), ow); }

		CommandLineParser parser = new DefaultParser();
		try {
			cmd = parser.parse(options, actualArgs);
		} catch (ParseException ex) {
			error.onError(utilityName, options);
		}

	}

	public void formatHelp(String utilityName, Options options) {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(utilityName, options);
	}

	// TODO Create overloaded methods for int, long, etc
	// get(String name, Class<?> type)
	public String get(String name) {
		return cmd.getOptionValue(name, wrapperMap.get(name).getDefaultValue());
	}

	private String[] combineArgsWithSpaces(String[] args) {
		List<String> cache = new ArrayList<>();
		List<String> actualArgs = new ArrayList<>();
		boolean newArg = true;
		for (int i = 0; i < args.length; i++) {
			String s = args[i];

			if (!newArg && s.endsWith("\"")) {
				cache.add(s.substring(0, s.length() - 1));
				newArg = true;
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < cache.size() - 1; j++) { sb.append(cache.get(j)).append(" "); }
				sb.append(cache.get(cache.size() - 1));
				actualArgs.add(sb.toString());
				cache.clear();
				continue;
			}

			if (!newArg) {
				cache.add(s);
				continue;
			}

			if (s.startsWith("\"")) {
				cache.add(s.substring(1));
				newArg = false;
				continue;
			}

			actualArgs.add(s);
		}

		return Blutil.arrayFromList(actualArgs);
	}

	@FunctionalInterface
	public interface Fallback {
		public void onError(String name, Options options);
	}
}
