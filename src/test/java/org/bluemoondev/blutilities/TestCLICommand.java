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

import org.bluemoondev.blutilities.annotations.Argument;
import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.cli.CommandParser;
import org.bluemoondev.blutilities.cli.ICommand;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> TestCLICommand.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command(name = "cli", useCli = true)
public class TestCLICommand implements ICommand {
    
    @Argument(name = "pi", shortcut = "p", desc = "Mess with math and change Pi")
    private float pi;

    @Argument(name = "sentence", shortcut = "s", desc = "This is a sentence")
    private String sentence;
    
    @Argument(name = "fav", shortcut = "f", desc = "Your favorite number I guess", required = false, defaultValue = "17")
    private int favoriteNumber;

    @Override
    public void preRun(String subCmd, CommandParser parser) {
        pi = Float.parseFloat(parser.get("pi"));
        sentence = parser.get("sentence");
        favoriteNumber = Integer.parseInt(parser.get("fav"));
    }
    
    
    public void run() {
        System.out.println("Pi: " + pi);
        System.out.println("Sentence: " + sentence);
        System.out.println("Favorite Number: " + favoriteNumber);
    }

}
