/*
 * Copyright (C) 2020 Blue Moon Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bluemoondev.blutilities.cli;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.bluemoondev.blutilities.Blutil;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> ArgumentUtil.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class ArgumentUtil {
	
	//TODO Deal with per command options and spaces
	public static Options getOptions(List<OptionWrapper> wrappers) {
		Options options = new Options();
		for(OptionWrapper ow : wrappers) {
			options.addOption(ow.getOption());
		}
		
		return options;
	}
	
	public static List<OptionWrapper> getArguments(Class<?> clazz){
		List<OptionWrapper> opts = new ArrayList<>();
		for(Field f : clazz.getDeclaredFields()) {
			if(f.isAnnotationPresent(Argument.class)) {
				Argument a = f.getAnnotation(Argument.class);
				Option o = new Option(a.shortcut(), a.name(), a.hasArgs(), a.desc());
				o.setRequired(a.required());
				o.setType(Blutil.getClassForPrimitive(f.getType()));
				o.setArgName(f.getType().getSimpleName());
				opts.add(new OptionWrapper(o, a.allowSpaces(), a.defaultValue()));
			}
		}
		
		return opts;
	}
	
	
	

}
