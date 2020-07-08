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

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> ICommand.java<br>
 * <p>
 * Represents a generic command. In order for a {@link CommandParser
 * CommandParser} to be created for this command, the
 * {@link org.bluemoondev.blutilities.annotations.Command Command} annotation
 * should be present
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public interface ICommand {

    /**
     * This method should be called before executing a command. This is where
     * argument fields should be initialized
     * 
     * @param subCmd The sub command, or null if the
     *               {@link org.bluemoondev.blutilities.annotations.Command#subCmds()
     *               Command#subCmds()} was set to false (the default value)
     * @param parser The {@link CommandParser CommandParser} for this (sub)command
     */
    public void preRun(String subCmd, CommandParser parser);

}
