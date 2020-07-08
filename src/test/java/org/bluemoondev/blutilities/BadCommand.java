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
package org.bluemoondev.blutilities;

import org.bluemoondev.blutilities.commands.CommandParser;
import org.bluemoondev.blutilities.commands.ICommand;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> BadCommand.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class BadCommand implements ICommand {

    @Override
    public void preRun(String subCmd, CommandParser parser) {
        System.out.println("assigning arguments their values");
    }

}
