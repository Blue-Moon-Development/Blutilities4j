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
public enum Errors {

    SUCCESS ( "No error" ),
    FAILURE ( "Failure" );

    public static final Errors NO_ERROR    = SUCCESS;
    public static final Errors UNKNOWN     = FAILURE;
    public static final Errors UNSPECIFIED = FAILURE;

    final int    code;
    final String msg;

    Errors(String msg) {
        this.code = CodeGenerator.getNextCode();
        this.msg = msg;
    }

    public int getCode() { return this.code; }

    public String getMsg() { return this.msg; }

    @Override
    public String toString() {
        // 0x%08X - standard 0x00000000 format
        // Using 2 while still not many errors
        return "Error Code: " + String.format("0x%02X", code) + " (" + msg + ")";
    }

    private static class CodeGenerator {
        private static int code = 0;

        public static int getNextCode() { return code++; }
    }
}
