/*
 * Copyright 2005-2006 Noelios Consulting.
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the "License"). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at http://www.opensource.org/licenses/cddl1.txt If
 * applicable, add the following below this CDDL HEADER, with the fields
 * enclosed by brackets "[]" replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package org.restlet.service;

/**
 * Service providing logging to a component or an application. The default
 * access log format follows the <a href="http://www.w3.org/TR/WD-logfile.html">
 * W3C Extended Log File Format</a> with the following fields used: <br/>
 * <ol>
 * <li>Date (YYYY-MM-DD)</li>
 * <li>Time (HH:MM:SS)</li>
 * <li>Client address (IP)</li>
 * <li>Remote user identifier (see RFC 1413)</li>
 * <li>Server address (IP)</li>
 * <li>Server port</li>
 * <li>Method (GET|POST|...)</li>
 * <li>Resource reference path (including the leading slash)</li>
 * <li>Resource reference query (excluding the leading question mark)</li>
 * <li>Response status code</li>
 * <li>Number of bytes sent</li>
 * <li>Number of bytes received</li>
 * <li>Time to serve the request (in milliseconds)</li>
 * <li>Host reference</li>
 * <li>Client agent name</li>
 * <li>Referrer reference</li>
 * </ol>
 * 
 * For custom access log format, see the syntax to use and the list of available
 * variable names in {@link org.restlet.util.Template}.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class LogService {
    /** Indicates if the service has been enabled. */
    private boolean enabled;

    /** The access logger name. */
    private String accessLoggerName;

    /** The context logger name. */
    private String contextLoggerName;

    /** The format. */
    private String accessLogFormat;

    /** Indicates if the identity check (as specified by RFC1413) is enabled. */
    private boolean identityCheck;

    /**
     * Constructor.
     * 
     * @param enabled
     *            True if the service has been enabled.
     */
    public LogService(boolean enabled) {
        this.accessLoggerName = null;
        this.contextLoggerName = null;
        this.enabled = enabled;
        this.accessLogFormat = null;
        this.identityCheck = false;
    }

    /**
     * Returns the format used.
     * 
     * @return The format used, or null if the default one is used.
     * @see org.restlet.util.Template for format syntax and variables.
     */
    public String getAccessLogFormat() {
        return this.accessLogFormat;
    }

    /**
     * Returns the name of the JDK's logger to use when logging calls.
     * 
     * @return The name of the JDK's logger to use when logging calls.
     */
    public String getAccessLoggerName() {
        return this.accessLoggerName;
    }

    /**
     * Returns the name of the JDK's logger to use when logging context
     * messages.
     * 
     * @return The name of the JDK's logger to use when logging context
     *         messages.
     */
    public String getContextLoggerName() {
        return this.contextLoggerName;
    }

    /**
     * Indicates if the service should be enabled.
     * 
     * @return True if the service should be enabled.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Indicates if the identity check (as specified by RFC1413) is enabled.
     * Default value is false.
     * 
     * @return True if the identity check is enabled.
     */
    public boolean isIdentityCheck() {
        return this.identityCheck;
    }

    /**
     * Sets the format to use when logging calls. The default format matches the
     * one of IIS 6.
     * 
     * @param format
     *            The format to use when loggin calls.
     * @see org.restlet.util.Template for format syntax and variables.
     */
    public void setAccessLogFormat(String format) {
        this.accessLogFormat = format;
    }

    /**
     * Sets the name of the JDK's logger to use when logging calls.
     * 
     * @param name
     *            The name of the JDK's logger to use when logging calls.
     */
    public void setAccessLoggerName(String name) {
        this.accessLoggerName = name;
    }

    /**
     * Sets the name of the JDK's logger to use when logging context messages.
     * 
     * @param name
     *            The name of the JDK's logger to use when logging context
     *            messages.
     */
    public void setContextLoggerName(String name) {
        this.contextLoggerName = name;
    }

    /**
     * Indicates if the service should be enabled.
     * 
     * @param enabled
     *            True if the service should be enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Indicates if the identity check (as specified by RFC1413) is enabled.
     * 
     * @param enabled
     *            True if the identity check is enabled.
     */
    public void setIdentityCheck(boolean enabled) {
        this.identityCheck = enabled;
    }

}
