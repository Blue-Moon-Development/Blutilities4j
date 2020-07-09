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
import org.bluemoondev.blutilities.commands.CommandParser;
import org.bluemoondev.blutilities.commands.ICommand;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> TestCommand.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command(name = "test", useCli = false, subCmds = true)
public class TestCommand implements ICommand {

    @Argument(name = "name", shortcut = "n", desc = "Student's name", cmd = "create")
    private String name;

    @Argument(name = "age", shortcut = "a", required = false, defaultValue = "18", desc = "Student's age",
            cmd = "create")
    private int age;
    @Argument(name = "idk", shortcut = "i", desc = "I dnt even know", cmd = "create")
    private String idk;
    
    

    @Argument(name = "gpa", shortcut = "g", required = false, defaultValue = "3.0",
              desc = "Student's grade point average", cmd = "edit")
    private double gpa;

    public void run(String subCmd) {
        switch (subCmd) {
            case "create":
                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                System.out.println("Something: " + idk);
            break;
            case "edit":
                System.out.println("GPA: " + gpa);
        }
    }

    @Override
    public void preRun(String subCmd, CommandParser parser) {
        switch (subCmd) {
            case "create":
                name = parser.get("name");
                age = Integer.parseInt(parser.get("age"));
                idk = parser.get("idk");
            break;
            case "edit":
                gpa = Double.parseDouble(parser.get("gpa"));
            break;
        }
    }

}
