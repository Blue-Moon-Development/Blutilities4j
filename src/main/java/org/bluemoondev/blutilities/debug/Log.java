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
package org.bluemoondev.blutilities.debug;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bluemoondev.blutilities.errors.Checks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

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

    private static final Map<String, Log>      logs    = new HashMap<>();
    private static final Map<Class<?>, Logger> loggers = new HashMap<>();
    private static final Map<String, Marker>   markers = new HashMap<>();

    private static ErrorConsumer errorConsumer;

    private static final String ERROR_LOG_NAME = "ErrorLogger";

    private final String projectName;

    private final Class<?> clazz;

    private Log(String projectName, Class<?> clazz) {
        this.projectName = projectName;
        this.clazz = clazz;
        if (!markers.containsKey(projectName))
            markers.put(projectName, MarkerFactory.getMarker(projectName));
    }

    public void info(String format, Object... args) {
        if (!loggers.containsKey(clazz))
            loggers.put(clazz, LoggerFactory.getLogger(clazz));
        loggers.get(clazz).info(markers.get(projectName), String.format(format, args));
    }

    public void debug(String format, Object... args) {
        if (!loggers.containsKey(clazz))
            loggers.put(clazz, LoggerFactory.getLogger(clazz));
        loggers.get(clazz).debug(markers.get(projectName), String.format(format, args));
    }

    public void trace(String format, Object... args) {
        if (!loggers.containsKey(clazz))
            loggers.put(clazz, LoggerFactory.getLogger(clazz));
        loggers.get(clazz).trace(markers.get(projectName), String.format(format, args));
    }

    public void warn(String format, Object... args) {
        LoggerFactory.getLogger(ERROR_LOG_NAME).warn(markers.get(projectName),
                                                     getClassFormat(clazz) + String.format(format, args));
    }

    public void warn(Throwable t, String format, Object... args) {
        LoggerFactory.getLogger(ERROR_LOG_NAME).warn(markers.get(projectName),
                                                     getClassFormat(clazz) + String.format(format, args), t);
    }

    public void warn(Throwable t) {
        warn(t, t.getMessage());
    }

    public void error(String format, Object... args) {
        LoggerFactory.getLogger(ERROR_LOG_NAME).error(markers.get(projectName),
                                                      getClassFormat(clazz) + String.format(format, args));
        if (Checks.isNotNull(errorConsumer))
            errorConsumer.consume();
    }

    public void error(Throwable t, String format, Object... args) {
        LoggerFactory.getLogger(ERROR_LOG_NAME).error(markers.get(projectName),
                                                      getClassFormat(clazz) + String.format(format, args), t);
        if (Checks.isNotNull(errorConsumer))
            errorConsumer.consume();
    }

    public void error(Throwable t) {
        error(t, t.getMessage());
    }

    private String getClassFormat(Class<?> clazz) {
        return "[" + clazz.getName() + "] - ";
    }

    public static void setOnErrorAction(ErrorConsumer consumer) { errorConsumer = consumer; }

    /**
     * Sets the logback configuration file, if this isn't called then the logger
     * will use the default Blutilities4j configuration instead
     * 
     * @param configPath The configuration file to be read in as an input stream
     *                   (i.e it should be on the classpath)
     * @throws JoranException If the configuration could not be executed
     * @throws IOException    If the configuration file could not be found
     */
    public static void setup(String configPath) throws JoranException, IOException {
//        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, configPath);
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();
        JoranConfigurator conf = new JoranConfigurator();
        conf.setContext(lc);
        InputStream confStream = Log.class.getResourceAsStream(configPath);
        conf.doConfigure(confStream);
        confStream.close();
    }

    public static Log get(String projectName, Class<?> clazz) {
        if (!logs.containsKey(projectName))
            logs.put(projectName, new Log(projectName, clazz));
        return logs.get(projectName);
    }

    @FunctionalInterface
    public interface ErrorConsumer {
        public void consume();
    }

}
