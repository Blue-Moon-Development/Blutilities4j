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
 * <strong>File:</strong> OptionImpl.java<br>
 * <p>
 * Fixes a few bugs from Apache Commons CLI Option class
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class OptionImpl extends Option {

    private Class<?> actualType;
    private String   defaultValue;
    private String   cmd;
    private String   regex;

    public OptionImpl(String opt, String longOpt, boolean hasArg, String description) throws IllegalArgumentException {
        super(opt, longOpt, hasArg, description);
        actualType = String.class;
    }
    
    public OptionImpl setOptional(boolean optional) {
        setRequired(!optional);
        return this;
    }
    
    public OptionImpl setArgTypeName(String argTypeName) {
        setArgName(argTypeName);
        return this;
    }

    public OptionImpl setActualType(Class<?> type) {
        actualType = type;
        return this;
    }

    public OptionImpl setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public OptionImpl attachToCmd(String cmd) {
        this.cmd = cmd;
        return this;
    }

    public OptionImpl setRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public Class<?> getActualType() { return actualType; }

    public String getDefaultValue() { return this.defaultValue; }

    public String getCmd() { return this.cmd; }

    public String getRegex() { return this.regex; }

}
