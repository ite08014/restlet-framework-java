/*
 * Copyright 2005-2007 Noelios Consulting.
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

package org.restlet;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.util.Template;

/**
 * Final handler of a calls typically created by Finders. Handler instances
 * allow the processing of a call in a thread-safe context. This is different
 * from the Uniform subclasses like Restlet, Filter and Router which can be
 * invoked by multiple threads at the same time. However, as they offer a rather
 * low-level API and its subclass {@link org.restlet.resource.Resource} is often
 * preferred for concrete handlers.<br>
 * <br>
 * This class exposes a different set of handle*() and allow*() Java methods for
 * each type of Uniform method supported by your handler. It has a predefined
 * set for common methods like GET, POST, PUT, DELETE, HEAD and OPTIONS.
 * Extension methods like MOVE or PATCH are automatically supported using Java
 * introspection. The actual dispatching of the call to those methods is
 * dynamically done by the {@link org.restlet.Finder} class.<br>
 * <br>
 * The HEAD method has a default implementation based on the GET method and the
 * OPTIONS method automatically updates the list of allowed methods in the
 * response, as required by the HTTP specification.<br>
 * <br>
 * Also, you can declare which REST methods are allowed by your Handler by
 * overiding the matching allow*() method. By default, allowOptions() returns
 * true, but all other allow*() methods will return false. Therefore, if you
 * want to accept MOVE method calls, just override allowMove() and return
 * true. Again, the invoking Finder will be able to detect this method and know
 * whether or not your Handler should be invoked. It is also used by the
 * handleOptions() method to return the list of allowed methods.
 * 
 * @see org.restlet.Finder
 * @author Jerome Louvel (contact@noelios.com)
 */
public abstract class Handler {
    /** The parent context. */
    private Context context;

    /** The handled request. */
    private Request request;

    /** The returned response. */
    private Response response;

    /**
     * Special constructor used by IoC frameworks. Note that the init() method
     * MUST be invoked right after the creation of the handler in order to keep
     * a behavior consistent with the normal three arguments constructor.
     */
    public Handler() {
    }

    /**
     * Normal constructor.
     * 
     * @param context
     *                The parent context.
     * @param request
     *                The request to handle.
     * @param response
     *                The response to return.
     */
    public Handler(Context context, Request request, Response response) {
        this.context = context;
        this.request = request;
        this.response = response;
    }

    /**
     * Indicates if DELETE calls are allowed. The default value is false.
     * 
     * @return True if the method is allowed.
     */
    public boolean allowDelete() {
        return false;
    }

    /**
     * Indicates if GET calls are allowed. The default value is false.
     * 
     * @return True if the method is allowed.
     */
    public boolean allowGet() {
        return false;
    }

    /**
     * Indicates if HEAD calls are allowed. The default behavior is to call
     * allowGet().
     * 
     * @return True if the method is allowed.
     */
    public boolean allowHead() {
        return allowGet();
    }

    /**
     * Indicates if OPTIONS calls are allowed. The default value is true.
     * 
     * @return True if the method is allowed.
     */
    public boolean allowOptions() {
        return true;
    }

    /**
     * Indicates if POST calls are allowed. The default value is false.
     * 
     * @return True if the method is allowed.
     */
    public boolean allowPost() {
        return false;
    }

    /**
     * Indicates if PUT calls are allowed. The default value is false.
     * 
     * @return True if the method is allowed.
     */
    public boolean allowPut() {
        return false;
    }

    /**
     * Generates a reference based on a template URI. Note that you can leverage
     * all the variables defined in the Template class as they will be resolved
     * using the resource's request and response properties.
     * 
     * @param uriTemplate
     *                The URI template to use for generation.
     * @return The generated reference.
     */
    public Reference generateRef(String uriTemplate) {
        Template tplt = new Template(getLogger(), uriTemplate);
        return new Reference(tplt.format(getRequest(), getResponse()));
    }

    /**
     * Returns the parent application if it exists, or null.
     * 
     * @return The parent application if it exists, or null.
     */
    public Application getApplication() {
        return getContext().getApplication();
    }

    /**
     * Returns the context.
     * 
     * @return The context.
     */
    public Context getContext() {
        if (this.context == null)
            this.context = new Context(getClass().getCanonicalName());
        return this.context;
    }

    /**
     * Returns the logger to use.
     * 
     * @return The logger to use.
     */
    public Logger getLogger() {
        return getContext().getLogger();
    }

    /**
     * Returns the request.
     * 
     * @return the request.
     */
    public Request getRequest() {
        return this.request;
    }

    /**
     * Returns the response.
     * 
     * @return the response.
     */
    public Response getResponse() {
        return this.response;
    }

    /**
     * Handles a DELETE call. The default behavior, to be overriden by
     * subclasses, is to set the status to {@link Status#SERVER_ERROR_INTERNAL}.
     */
    public void handleDelete() {
        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
    }

    /**
     * Handles a GET call. The default behavior, to be overriden by subclasses,
     * is to set the status to {@link Status#SERVER_ERROR_INTERNAL}.
     */
    public void handleGet() {
        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
    }

    /**
     * Handles a HEAD call. The default behavior is to invoke the handleGet()
     * method. This is the expected behavior of the HTTP 1.1 specification for
     * example. Note that the server connectors will take care of never sending
     * back to the client the response entity bodies.
     */
    public void handleHead() {
        handleGet();
    }

    /**
     * Handles an OPTIONS call introspecting the target resource (as provided by
     * the 'findTarget' method). The default implementation is based on the HTTP
     * specification which says that OPTIONS should return the list of allowed
     * methods in the Response headers.
     */
    public void handleOptions() {
        updateAllowedMethods();
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    /**
     * Handles a POST call. The default behavior, to be overriden by subclasses,
     * is to set the status to {@link Status#SERVER_ERROR_INTERNAL}.
     */
    public void handlePost() {
        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
    }

    /**
     * Handles a PUT call. The default behavior, to be overriden by subclasses,
     * is to set the status to {@link Status#SERVER_ERROR_INTERNAL}.
     */
    public void handlePut() {
        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
    }

    /**
     * Initialize the resource with its context. If you override this method,
     * make sure that you don't forget to call super.init() first, otherwise
     * your Resource won't behave properly.
     * 
     * @param context
     *                The parent context.
     * @param request
     *                The request to handle.
     * @param response
     *                The response to return.
     */
    public void init(Context context, Request request, Response response) {
        this.context = context;
        this.request = request;
        this.response = response;
    }

    /**
     * Invokes a method with the given arguments.
     * 
     * @param method
     *                The method to invoke.
     * @param args
     *                The arguments to pass.
     * @return Invocation result.
     */
    private Object invoke(java.lang.reflect.Method method, Object... args) {
        Object result = null;

        if (method != null) {
            try {
                result = method.invoke(this, args);
            } catch (Exception e) {
                getLogger().log(
                        Level.WARNING,
                        "Couldn't invoke the handle method for \"" + method
                                + "\"", e);
            }
        }

        return result;
    }

    /**
     * Sets the parent context.
     * 
     * @param context
     *                The parent context.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Sets the request to handle.
     * 
     * @param request
     *                The request to handle.
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Sets the response to update.
     * 
     * @param response
     *                The response to update.
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * Updates the set of allowed methods on the response.
     */
    protected void updateAllowedMethods() {
        Set<Method> allowedMethods = getResponse().getAllowedMethods();
        for (java.lang.reflect.Method classMethod : getClass().getMethods()) {
            if (classMethod.getName().startsWith("allow")
                    && (classMethod.getParameterTypes().length == 0)) {
                if ((Boolean) invoke(classMethod)) {
                    Method allowedMethod = Method.valueOf(classMethod.getName()
                            .substring(5));
                    allowedMethods.add(allowedMethod);
                }
            }
        }
    }

}
