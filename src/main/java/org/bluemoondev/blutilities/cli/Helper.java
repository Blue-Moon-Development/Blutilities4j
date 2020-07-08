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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> Helper.java<br>
 * <p>
 * An extension of Apache Commons CLI's
 * {@link org.apache.commons.cli.HelpFormatter HelpFormatter} but provides
 * methods to retrieve the formatted help text rather than simply printing it
 * out only
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Helper extends HelpFormatter {

    public String getFormatted(String cmdLineSyntax, Options options, String header, String footer) {
        StringBuilder sb = new StringBuilder();

        if (header != null && header.trim().length() > 0)
            sb.append(header).append("\n");

        sb.append(getUsage(cmdLineSyntax, options));
        sb.append("\n");
        sb.append(getOptions(cmdLineSyntax, options));

        if (footer != null && footer.trim().length() > 0)
            sb.append("\n").append(footer);

        return sb.toString();
    }

    public String getFormatted(String cmd, Options options) {
        return getFormatted(cmd, options, null, null);
    }

    public String getOptions(String cmdLineSyntax, Options options) {
        StringBuffer sb = new StringBuffer();
        return renderOptions(sb, getWidth(), options, getLeftPadding(), getDescPadding()).toString();
    }

    // public String getUsage(String cmdLineSyntax, Options options) {
    // int argPos = cmdLineSyntax.indexOf(' ') + 1;
    // StringBuffer buff = new StringBuffer();
    // return renderWrappedText(buff, getWidth(), getSyntaxPrefix().length() +
    // argPos, getSyntaxPrefix()
    // + cmdLineSyntax).toString();
    // }

    private Appendable renderWrappedTextBlock(StringBuffer sb, int width, int nextLineTabStop, String text) {
        try {
            BufferedReader in = new BufferedReader(new StringReader(text));
            String line;
            boolean firstLine = true;
            while ((line = in.readLine()) != null) {
                if (!firstLine) {
                    sb.append(getNewLine());
                } else {
                    firstLine = false;
                }
                renderWrappedText(sb, width, nextLineTabStop, line);
            }
        } catch (IOException e) {
        }

        return sb;
    }

    public String getUsage(String app, Options options) {
        // Initialize the string buffer
        StringBuffer buff = new StringBuffer(getSyntaxPrefix()).append(app).append(" ");

        // create a list for processed option groups
        Collection<OptionGroup> processedGroups = new ArrayList<OptionGroup>();

        List<Option> optList = new ArrayList<Option>(options.getOptions());
        if (getOptionComparator() != null) { Collections.sort(optList, getOptionComparator()); }
        // iterate over the options
        for (Iterator<Option> it = optList.iterator(); it.hasNext();) {
            // get the next Option
            Option option = it.next();

            // check if the option is part of an OptionGroup
            OptionGroup group = options.getOptionGroup(option);

            // if the option is part of a group
            if (group != null) {
                // and if the group has not already been processed
                if (!processedGroups.contains(group)) {
                    // add the group to the processed list
                    processedGroups.add(group);

                    // add the usage clause
                    appendOptionGroup(buff, group);
                }

                // otherwise the option was displayed in the group
                // previously so ignore it.
            }

            // if the Option is not part of an OptionGroup
            else {
                appendOption(buff, option, option.isRequired());
            }

            if (it.hasNext()) { buff.append(" "); }
        }

        return buff.toString();
        // call printWrapped
        // printWrapped(pw, width, buff.toString().indexOf(' ') + 1, buff.toString());
    }

    private void appendOptionGroup(StringBuffer buff, OptionGroup group) {
        if (!group.isRequired()) { buff.append("["); }

        List<Option> optList = new ArrayList<Option>(group.getOptions());
        if (getOptionComparator() != null) { Collections.sort(optList, getOptionComparator()); }
        // for each option in the OptionGroup
        for (Iterator<Option> it = optList.iterator(); it.hasNext();) {
            // whether the option is required or not is handled at group level
            appendOption(buff, it.next(), true);

            if (it.hasNext()) { buff.append(" | "); }
        }

        if (!group.isRequired()) { buff.append("]"); }
    }

    private void appendOption(StringBuffer buff, Option option, boolean required) {
        if (!required) { buff.append("["); }

        if (option.getOpt() != null) {
            buff.append("-").append(option.getOpt());
        } else {
            buff.append("--").append(option.getLongOpt());
        }

        // if the Option has a value and a non blank argname
        if (option.hasArg() && (option.getArgName() == null || option.getArgName().length() != 0)) {
            buff.append(option.getOpt() == null ? getLongOptSeparator() : " ");
            buff.append("<").append(option.getArgName() != null ? option.getArgName() : getArgName()).append(">");
        }

        // if the Option is not a required option
        if (!required) { buff.append("]"); }
    }

}
