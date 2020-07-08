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
package org.bluemoondev.blutilities.errors;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> Errors.java<br>
 * <p>
 * A set of error codes. Useful when the exact type of error is needed to be
 * known, rather than just the exception or whether a return type was true or
 * false
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Errors {

    public static final int SUCCESS  = 0x0;
    public static final int NO_ERROR = SUCCESS;

    public static final int UNKNOWN = 0x1, UNSPECIFIED = UNKNOWN, FAILURE = UNKNOWN;

    public static final int COMMAND_PARSER_NOT_ENOUGH_ARGS     = 0x2;
    public static final int COMMAND_PARSER_NUMBER_EXPECTED     = 0x3;
    public static final int COMMAND_PARSER_NULL_OR_EMPTY_ARGS  = 0x4;
    public static final int COMMAND_PARSER_INVALID_SUB_COMMAND = 0x5;
    public static final int COMMAND_PARSER_CLI_FAILURE         = 0x6;
    public static final int COMMAND_PARSER_NO_PERMISSION       = 0x7;

}
