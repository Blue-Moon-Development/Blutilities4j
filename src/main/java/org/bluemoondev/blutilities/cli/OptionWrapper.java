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

import org.apache.commons.cli.Option;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> OptionWrapper.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class OptionWrapper {

	private Option	option;
	private boolean	allowSpaces;
	private String	defaultValue;

	/**
	 * @param option       The actual <code>Option</code> instance
	 * @param allowSpaces  Should the option's arguments be allowed to have spaces?
	 * @param defaultValue The default value for this option, null if there is no
	 *                     default
	 */
	public OptionWrapper(Option option, boolean allowSpaces, String defaultValue) {
		this.option = option;
		this.allowSpaces = allowSpaces;
		this.defaultValue = defaultValue;
	}

	public OptionWrapper(Option option, boolean allowSpaces) {
		this(option, allowSpaces, null);
	}

	public OptionWrapper(Option option, String defaultValue) {
		this(option, false, defaultValue);
	}

	public OptionWrapper(Option option) {
		this(option, false, null);
	}
	
	public Option getOption() { return this.option; }

	public boolean shouldAllowSpaces() { return this.allowSpaces; }
	
	public String getDefaultValue() { return this.defaultValue; }

}
