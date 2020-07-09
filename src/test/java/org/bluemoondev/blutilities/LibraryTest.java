/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.bluemoondev.blutilities;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.bluemoondev.blutilities.commands.CommandHandler;
import org.bluemoondev.blutilities.debug.Log;
import org.bluemoondev.blutilities.errors.Errors;
import org.bluemoondev.blutilities.errors.exceptions.CommandException;

public class LibraryTest {

    public void testSomeLibraryMethod() {
        assertTrue("someLibraryMethod should return 'true'", Blutil.justATest("true"));
    }

    public static void main(String[] args) {
        // new LibraryTest().testArgParser();
        // new LibraryTest().testCliArgParser();
        // new LibraryTest().testCliSubArgParser();
        for (Errors e : Errors.values()) { System.out.println(e); }

        TestCLICommand cmd = new TestCLICommand();
        // ICommand cmd = new BadCommand();
        CommandHandler handler = new CommandHandler();
        handler.addCommand(cmd);
        String msg = "-p 3.1415 -s \"So this is how its going to be, eh?\" some extra stuff -f 21 then the number";
        String[] passedArgs = msg.split(" ");
        handler.execute("cli", passedArgs, true, (s, a) -> {
            cmd.run();
        });

        TestCommand testCmd = new TestCommand();
        handler.addCommand(testCmd);
        msg = "create \"John Smith\" somethingidkwhat 27";
        passedArgs = msg.split(" ");
        Errors e = handler.execute("test", passedArgs, true, (s, a) -> {
            testCmd.run(s);
        });
        
        if(e.getCode() != 0) System.err.println(e);
        
        
        
        File f = new File("./test.txt");
        try {
            @SuppressWarnings("resource")
            BufferedReader reader = new BufferedReader(new FileReader(f));
        } catch (IOException ex) {
            Log.error(LibraryTest.class, ex);
        }
        
        Log.info(LibraryTest.class, "The commands returned an error code %s and stuff yo", e.toString());
        Log.error(LibraryTest.class, new CommandException(Errors.COMMAND_PARSER_CLI_FAILURE, "just testing stuff"));
    }

}
