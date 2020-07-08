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
package org.bluemoondev.blutilities.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.bluemoondev.blutilities.Blutil;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> ArrayUtil.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class ArrayUtil {

    /**
     * When arguments are provided such that they look like:
     * <code>{ Hello, this, is, an, argument, named, "john, smith" }</code>, this
     * will
     * convert
     * <code>"john smith"</code> into a single string <code>john smith</code> so
     * that the resulting array looks like
     * <code>{ Hello, this, is, an, argument, named, john smith }</code>
     * 
     * @param args The array to strip quotes into single elements
     * @return The new array
     */
    public static String[] combineArgsWithSpaces(String[] args) {
        List<String> cache = new ArrayList<>();
        List<String> actualArgs = new ArrayList<>();
        boolean newArg = true;
        for (int i = 0; i < args.length; i++) {
            String s = args[i];

            if (!newArg && s.endsWith("\"")) {
                cache.add(s.substring(0, s.length() - 1));
                newArg = true;
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < cache.size() - 1; j++) { sb.append(cache.get(j)).append(" "); }
                sb.append(cache.get(cache.size() - 1));
                actualArgs.add(sb.toString());
                cache.clear();
                continue;
            }

            if (!newArg) {
                cache.add(s);
                continue;
            }

            if (s.startsWith("\"")) {
                cache.add(s.substring(1));
                newArg = false;
                continue;
            }

            actualArgs.add(s);
        }

        return arrayFromList(actualArgs);
    }

    /**
     * Converts the given <code>List</code> to a standard array
     * 
     * @param <T>  the runtime type of the array to contain the collection
     * @param list The <code>List</code> to convert
     * @return The converted array
     */
    public static <T> T[] arrayFromList(List<T> list) {
        T[] arr = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
        arr = list.toArray(arr);
        return arr;
    }
    
    public static <T> String formatArray(T[] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i = 0; i < arr.length - 1; i++) {
            sb.append(arr[i]).append(", ");
        }
        
        sb.append(arr[arr.length - 1]).append(" }");
        return sb.toString();
    }

}
