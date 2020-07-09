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
package org.bluemoondev.blutilities.debug;

import java.util.HashMap;
import java.util.Map;

import org.bluemoondev.blutilities.errors.Checks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> Log.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Log {
    
    private static final Map<Class<?>, Logger> loggers = new HashMap<>();
    
    private static final Logger ERROR = LoggerFactory.getLogger("ErrorLogger");
    
    
    public static void info(Class<?> logger, String format, Object...arguments) {
        if(!Checks.isNotNull(loggers.get(logger))) {
            loggers.put(logger, LoggerFactory.getLogger(logger));
        }
        loggers.get(logger).info(String.format(format, arguments));
    }
    
    public static void debug(Class<?> logger, String format, Object...arguments) {
        if(!Checks.isNotNull(loggers.get(logger))) {
            loggers.put(logger, LoggerFactory.getLogger(logger));
        }
        loggers.get(logger).debug(String.format(format, arguments));
    }
    
    public static void trace(Class<?> logger, String format, Object...arguments) {
        if(!Checks.isNotNull(loggers.get(logger))) {
            loggers.put(logger, LoggerFactory.getLogger(logger));
        }
        loggers.get(logger).trace(String.format(format, arguments));
    }
    
    public static void warn(Class<?> logger, String format, Object...arguments) {
        ERROR.warn("[" + logger.getName() + "] - " + String.format(format, arguments));
    }
    
    public static void warn(Class<?> logger, Throwable t, String format, Object...arguments) {
        ERROR.warn("[" + logger.getName() + "] - " + String.format(format, arguments), t);
    }
    
    public static void warn(Class<?> logger, Throwable t) {
        warn(logger, t, t.getMessage());
    }
    
    public static void error(Class<?> logger, String format, Object...arguments) {
        ERROR.error("[" + logger.getName() + "] - " + String.format(format, arguments));
    }
    
    public static void error(Class<?> logger, Throwable t, String format, Object...arguments) {
        ERROR.error("[" + logger.getName() + "] - " + String.format(format, arguments), t);
    }
    
    public static void error(Class<?> logger, Throwable t) {
        error(logger, t, t.getMessage());
    }
    

}
