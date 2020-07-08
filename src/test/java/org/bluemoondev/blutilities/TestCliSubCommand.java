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
package org.bluemoondev.blutilities;

import org.bluemoondev.blutilities.annotations.Argument;
import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.cli.CommandParser;
import org.bluemoondev.blutilities.cli.ICommand;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> TestCliSubCommand.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command(name = "clisub", subCmds = true, useCli = true)
public class TestCliSubCommand implements ICommand {

    @Argument(name = "sentence", shortcut = "s", desc = "Some sentence", cmd = "create")
    private String sentence;

    @Argument(name = "length", shortcut = "l", desc = "Some int length", required = false, defaultValue = "57",
              cmd = "edit")
    private int length;

    @Argument(name = "test", shortcut = "t", desc = "flag test with no args", hasArgs = false, required = false,
              cmd = "create")
    private boolean test;

    @Override
    public void preRun(String subCmd, CommandParser parser) {
        switch (subCmd) {
            case "create":
                sentence = parser.get("sentence");
                test = Boolean.parseBoolean(parser.get("test"));
            break;
            case "edit":
                length = Integer.parseInt(parser.get("length"));
        }

    }

    public void run(String subCmd) {
        switch (subCmd) {
            case "create":
                System.out.println("Sentence: " + sentence);
                System.out.println("Was flag set? " + test);
            break;
            case "edit":
                System.out.println("Length: " + length);
        }
    }

}
