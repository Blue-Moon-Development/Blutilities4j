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

import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.cli.Argument;
import org.bluemoondev.blutilities.cli.CommandParser;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> TestCommand.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command(name = "test", useCli = false)
public class TestCommand {

    @Argument(name = "name", shortcut = "n", required = true, desc = "Student's name", cmd = "create")
    private String name;

    @Argument(name = "age", shortcut = "a", defaultValue = "18", desc = "Student's age", cmd = "create")
    private int age;

    @Argument(name = "gpa", shortcut = "g", defaultValue = "3.0", desc = "Student's grade point average", cmd = "edit")
    private double gpa;
    
    
    public void run(String[] args, CommandParser parser) {
        
        switch(args[0]) {
            case "create":
                name = parser.get("name");
                age = Integer.parseInt(parser.get("age"));
                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                break;
            case "edit":
                gpa = Double.parseDouble(parser.get("gpa"));
                System.out.println("GPA: " + gpa);
                break;
                default:
                    name = parser.get("name");
                    age = Integer.parseInt(parser.get("age"));
                    System.out.println("Name: " + name);
                    System.out.println("Age: " + age);
                    gpa = Double.parseDouble(parser.get("gpa"));
                    System.out.println("GPA: " + gpa);
        }
    }

}
