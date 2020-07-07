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
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Helper extends HelpFormatter {

    public String getFormatted(String cmdLineSyntax, Options options) {
        StringBuilder sb = new StringBuilder();
        sb.append(getUsage(cmdLineSyntax, options));
        sb.append("\n");
        sb.append(getOptions(cmdLineSyntax, options));
        return sb.toString();
    }

    public String getOptions(String cmdLineSyntax, Options options) {
        StringBuffer sb = new StringBuffer();
        return renderOptions(sb, getWidth(), options, getLeftPadding(), getDescPadding()).toString();
    }

    public String getUsage(String cmdLineSyntax, Options options) {
        int argPos = cmdLineSyntax.indexOf(' ') + 1;
        StringBuffer buff = new StringBuffer();
        return renderWrappedText(buff, getWidth(), getSyntaxPrefix().length() + argPos, getSyntaxPrefix()
                                                                                        + cmdLineSyntax).toString();
    }

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

}
