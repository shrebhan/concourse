/*
 * Copyright (c) 2013-2019 Cinchapi Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cinchapi.concourse.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cinchapi.common.base.Verify;
import com.cinchapi.common.logging.Logging;
import com.cinchapi.lib.config.read.Interpreters;

/**
 * A wrapper around the {@code concourse_client.prefs} file that is used to
 * configure a client connection.
 * <p>
 * Instantiate using {@link ConcourseClientPreferences#open(String)}
 * </p>
 * 
 * @author Jeff Nelson
 */
public class ConcourseClientPreferences extends PreferencesHandler {

    static {
        Logging.disable(ConcourseClientPreferences.class);
    }

    // Defaults
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1717;
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private static final String DEFAULT_ENVIRONMENT = "";

    /**
     * An empty char array to return if there is no password defined in the
     * prefs file during a call to {@link #getPasswordExplicit()}.
     */
    protected static final char[] NO_PASSWORD_DEFINED = new char[0]; // visible
                                                                     // for
                                                                     // testing

    /**
     * Return a {@link ConcourseClientPreferences} handler that is sourced from
     * the {@link files}.
     * 
     * @param files
     * @return the handler
     */
    public static ConcourseClientPreferences from(Path... files) {
        Verify.thatArgument(files.length > 0, "Must include at least one file");
        return new ConcourseClientPreferences(files);
    }

    /**
     * Return a {@link ConcourseClientPreferences} handler that is sourced from
     * a concourse_client.prefs file in the current working directory.
     * 
     * @return the handler
     */
    public static ConcourseClientPreferences fromCurrentWorkingDirectory() {
        return from(Paths.get("concourse_client.prefs"));
    }

    /**
     * Return a {@link ConcourseClientPreferences} handler that is sourced from
     * a concourse_client.prefs file in the user's home directory.
     * 
     * @return the handler
     */
    public static ConcourseClientPreferences fromUserHomeDirectory() {
        return from(Paths.get(System.getProperty("user.home") + File.separator
                + "concourse_client.prefs"));
    }

    /**
     * Return a {@link ConcourseClientPreferences} wrapper that is backed by the
     * configuration information in {@code file}.
     * 
     * @param file the absolute path to the preferences file (relative paths
     *            will resolve to the user's home directory)
     * @return the preferences
     * @deprecated use {@link ConcourseServerPreferences#from(Path...)} instead
     */
    @Deprecated
    public static ConcourseClientPreferences open(String file) {
        return from(Paths.get(file));
    }

    /**
     * Construct a new instance.
     * 
     * @param file the absolute path to the preferences file (relative paths
     *            will resolve to the user's home directory)
     */
    private ConcourseClientPreferences(Path... files) {
        super(files);
    }

    /**
     * Return the value associated with the {@code environment} key.
     * 
     * @return the environment
     */
    public String getEnvironment() {
        return getOrDefault("environment", DEFAULT_ENVIRONMENT);
    }

    /**
     * Return the value associated with the {@code host} key.
     * 
     * @return the host
     */
    public String getHost() {
        return getOrDefault("host", DEFAULT_HOST);
    }

    /**
     * Return the value associated with the {@code password} key.
     * <p>
     * <strong>NOTE</strong>: This method returns the password as a char array
     * so that the caller can null out the data immediately after use. This is
     * generally advised to limit the amount of time that the sensitive data
     * remains in memory.
     * </p>
     * 
     * @return the password
     */
    public char[] getPassword() {
        return getOrDefault("password", DEFAULT_PASSWORD).toCharArray();
    }

    /**
     * Return the value associated with the {@code password} key, if it is
     * explicitly defined in the prefs file. Unlike the {@link #getPassword()}
     * method, this one will not return the default password if one is not
     * explicitly defined in the file.
     * <p>
     * <strong>NOTE</strong>: This method returns the password as a char array
     * so that the caller can null out the data immediately after use. This is
     * generally advised to limit the amount of time that the sensitive data
     * remains in memory.
     * </p>
     * 
     * @return the password or an empty character array if it is not defined
     */
    public char[] getPasswordExplicit() {
        String password = get("password");
        if(password != null) {
            return password.toCharArray();
        }
        else {
            return NO_PASSWORD_DEFINED;
        }
    }

    /**
     * Return the value associated with the {@code port} key.
     * 
     * @return the port
     */
    public int getPort() {
        return getOrDefault("port", Interpreters.numberOrNull(), DEFAULT_PORT);
    }

    /**
     * Return the value associated with the {@code username} key.
     * 
     * @return the username
     */
    public String getUsername() {
        return getOrDefault("username", DEFAULT_USERNAME);
    }

    /**
     * Set the value associated with the {@code environment} key.
     * 
     * @param environment
     */
    public void setEnvironment(String environment) {
        set("environment", environment);
    }

    /**
     * Set the value associated with the {@code host} key.
     * 
     * @param host
     */
    public void setHost(String host) {
        set("host", host);
    }

    /**
     * Set the value associated with the {@code password} key.
     * 
     * @param password
     */
    public void setPassword(char[] password) {
        set("password", new String(password));
    }

    /**
     * Set the value associated with the {@code port} key.
     * 
     * @param port
     */
    public void setPort(int port) {
        set("port", port);
    }

    /**
     * Set the value associated with the {@code username} key.
     * 
     * @param username
     */
    public void setUsername(String username) {
        set("username", username);
    }
}