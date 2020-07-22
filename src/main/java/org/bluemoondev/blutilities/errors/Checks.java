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
package org.bluemoondev.blutilities.errors;

import org.bluemoondev.blutilities.Blutil;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> Checks.java<br>
 * <p>
 * Various checks to help avoid errors. To explain with an example, instead of
 * constantly having to check for a <code>NumberFormatException</code> when
 * parsing a <code>String</code>, one can simply do something like
 * {@link Checks#isNumberOfType(Class, String) Checks#isNumberOfType(Class,
 * String)}:
 * 
 * <pre>
 * int num = 0;
 * String s = getUserInput();
 * if (Checks.isNumberOfType(Integer.class, s))
 *     num = Integer.parseInt(s);
 * </pre>
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Checks {

    private static final int NUMBER_ARRAY_SIZE = 6;
    private static final int LOC_BYTE          = 0;
    private static final int LOC_SHORT         = 1;
    private static final int LOC_INT           = 2;
    private static final int LOC_LONG          = 3;
    private static final int LOC_FLOAT         = 4;
    private static final int LOC_DOUBLE        = 5;

    /**
     * Checks if the supplied class type is a sub class of {@link java.lang.Number
     * Number}
     * 
     * @param type The class type to check
     * @return true if the supplied class type is a sub class of
     *         {@link java.lang.Number Number}, false otherwise
     */
    public static boolean isNumber(Class<?> type) {
        return type.getSuperclass() == Number.class;
    }

    /**
     * Checks if the supplied object type is an instance of {@link java.lang.Number
     * Number}
     * 
     * @param type The object type to check
     * @return true if the supplied object type is an instance of
     *         {@link java.lang.Number Number}, false otherwise
     */
    public static boolean isNumber(Object type) {
        return type instanceof Number;
    }

    /**
     * Checks if the given string is in the proper number format of the given class
     * type. If the class type is not a sub class of {@link java.lang.Number
     * Number}, in other words if it is not either
     * <code>Byte.class, Short.class, Integer.class, Long.class, Float.class, or Double.class</code>
     * it will return false
     * 
     * @param type The class type to check if <code>str</code> is the proper format
     *             of
     * @param str  The string to check if it can be parsed as the given number class
     *             type
     * @return True if the checks pass, false otherwise
     * @see {@link org.bluemoondev.blutilities.Blutil#getClassForPrimitive(Class)
     *      Blutil#getClassForPrimitive(Class)} to see about getting the class type
     *      of primitive data types such as
     *      <code>byte, short, int, long, float, and double</code> as well as others
     */
    public static boolean isNumberOfType(Class<?> type, String str) {
        return isNumberOfType(null, type, str);
    }

    /**
     * Checks if the given string is in the proper number format of the given object
     * type. If the object type is not a sub type of {@link java.lang.Number
     * Number}, in other words if it is not an instance of either
     * <code>Byte, Short, Integer, Long, Float, or Double</code>
     * it will return false
     * 
     * @param type The object type to check if <code>str</code> is the proper format
     *             of
     * @param str  The string to check if it can be parsed as the given number
     *             object
     *             type
     * @return True if the checks pass, false otherwise
     */
    public static boolean isNumberOfType(Object type, String str) {
        return isNumberOfType(type, null, str);
    }

    /**
     * Checks if the given string is a proper number format
     * 
     * @param str The string to check
     * @return True if <code>str</code> can be formatted as any of the following
     *         types: <code>Byte, Short, Integer, Long, Float, or Double</code>,
     *         false otherwise
     */
    public static boolean isNumber(String str) {
        double d = 0;
        int in = 0;
        long l = 0;
        float f = 0;
        byte b = 0;
        short s = 0;
        try {
            b = Byte.parseByte(str);
        } catch (NumberFormatException e1) {
            try {
                s = Short.parseShort(str);
            } catch (NumberFormatException e2) {
                try {
                    in = Integer.parseInt(str);
                } catch (NumberFormatException e3) {
                    try {
                        l = Long.parseLong(str);
                    } catch (NumberFormatException e4) {
                        try {
                            f = Float.parseFloat(str);
                        } catch (NumberFormatException e5) {
                            try {
                                d = Double.parseDouble(str);
                            } catch (NumberFormatException e6) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    // Prevent reuse of code in isNumberOfType(Object, String) and
    // isNumberOfType(Class, String)
    private static boolean isNumberOfType(Object o, Class<?> clazz, String str) {
        if (!isNumber(o) && !isNumber(clazz)) return false;
        byte b = 0;
        short s = 0;
        int in = 0;
        long l = 0L;
        float f = 0.0f;
        double d = 0.0;
        boolean[] numType = getNumberTypes(o, clazz);
        try {
            if (numType[LOC_BYTE])
                b = Byte.parseByte(str);
            else if (numType[LOC_SHORT])
                s = Short.parseShort(str);
            else if (numType[LOC_INT])
                in = Integer.parseInt(str);
            else if (numType[LOC_LONG])
                l = Long.parseLong(str);
            else if (numType[LOC_FLOAT])
                f = Float.parseFloat(str);
            else if (numType[LOC_DOUBLE])
                d = Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    private static boolean[] getNumberTypes(Object o, Class<?> clazz) {
        boolean[] ret = new boolean[NUMBER_ARRAY_SIZE];
        ret[LOC_BYTE] = (o instanceof Byte) || clazz == Byte.class;
        ret[LOC_SHORT] = (o instanceof Short) || clazz == Short.class;
        ret[LOC_INT] = (o instanceof Integer) || clazz == Integer.class;
        ret[LOC_LONG] = (o instanceof Long) || clazz == Long.class;
        ret[LOC_FLOAT] = (o instanceof Float) || clazz == Float.class;
        ret[LOC_DOUBLE] = (o instanceof Double) || clazz == Double.class;
        return ret;
    }

    /**
     * More readable version of <code>obj != null</code><br>
     * TODO: <strong>Might handle logging or provide fallback for when <code>obj</code> is
     * indeed null</strong>
     * 
     * @param obj The object to check if it is not null
     * @return true if <code>obj</code> is not null
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
    
    
    public static boolean isBoolean(Class<?> clazz) {
        return clazz == Boolean.class || Blutil.getClassForPrimitive(clazz) == Boolean.class;
    }

}
