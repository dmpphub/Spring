/**
 * 
 */
package com.vag.pfm.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.vag.pfm.constants.PfmConstants;


/**
 * @author  Gobinath A
 *
 */
public class GlobalAppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285539171683723441L;
	
	static {
		PropertyConfigurator.configure(PfmConstants.REAL_PATH + PfmConstants.LOG4J_PATH);
	}

	/** The Constant exceptionLog. */
	static final Logger exceptionLog = Logger.getLogger("debugLogger");
	
	/** The exception. */
	private Exception exception;

	/** The class name. */
	private String className;

	/** The method name. */
	private String methodName;

	/** The messages. */
	private String messages;

	/** The exception messages. */
	private String exceptionMessages;

	/** The instance id. */
	private long instanceId = System.currentTimeMillis();

	/** The args0. */
	private String args0;

	/** The args1. */
	private String args1;

	/** The args2. */
	private String args2;
	
	public GlobalAppException(Exception e) {
		exception = e;
		exceptionLog.info(exception);
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param inMessage
	 *            the in message
	 */
	public GlobalAppException(String inMessage) {
		messages = inMessage;
		exceptionLog.info(messages);
	}

	/**
	 * Instantiates a new base exception.
	 * 
	 * @param inClassName
	 *            the in class name
	 * @param inMethodName
	 *            the in method name
	 * @param e
	 *            the e
	 */
	public GlobalAppException(String inClassName, String inMethodName, Exception e) {
		exception = e;
		className = inClassName;
		methodName = inMethodName;
		exceptionMessages = getStringFromStackTrace(e);
		exceptionLog.info(className + "**" + methodName + "**"
				+ exceptionMessages);
	}

	/**
	 * Instantiates a new base exception.
	 * 
	 * @param inClassName
	 *            the in class name
	 * @param inMethodName
	 *            the in method name
	 * @param inMessage
	 *            the in message
	 * @param e
	 *            the e
	 */
	public GlobalAppException(String inClassName, String inMethodName,
			String inMessage, Exception e) {
		exception = e;
		className = inClassName;
		methodName = inMethodName;
		messages = inMessage;
		exceptionMessages = getStringFromStackTrace(e);
	}

	/**
	 * Instantiates a new base exception.
	 * 
	 * @param inClassName
	 *            the in class name
	 * @param inMethodName
	 *            the in method name
	 * @param inMessage
	 *            the in message
	 * @param inArgs0
	 *            the in args0
	 * @param inArgs1
	 *            the in args1
	 * @param e
	 *            the e
	 */
	public GlobalAppException(String inClassName, String inMethodName,
			String inMessage, String inArgs0, String inArgs1, Exception e) {
		exception = e;
		className = inClassName;
		methodName = inMethodName;
		messages = inMessage;
		args0 = inArgs0;
		args1 = inArgs1;
		exceptionMessages = getStringFromStackTrace(e);
	}

	/**
	 * Gets the infofrom stack trace.
	 * 
	 * @param e
	 *            the e
	 * @return the infofrom stack trace
	 */
	public static void getInfofromStackTrace(Exception e) {
		StackTraceElement stack[] = e.getStackTrace();

		for (int i = 0; i < stack.length; i++) {
			String filename = stack[i].getFileName();
			if (filename == null) {
				exceptionLog.info("filename is not available");
			}
			String className = stack[i].getClassName();
			exceptionLog.info("className : " + className);
			String methodName = stack[i].getMethodName();
			exceptionLog.info("methodName : " + methodName);
			boolean isNativeMethod = stack[i].isNativeMethod();
			exceptionLog.info("isNativeMethod : " + isNativeMethod);
			int line = stack[i].getLineNumber();
			exceptionLog.info("line : " + line);
		}
	}

	/**
	 * Gets the string from stack trace.
	 * 
	 * @param ex
	 *            the ex
	 * @return the string from stack trace
	 */
	public String getStringFromStackTrace(Throwable ex) {

		if (ex == null) {
			return "";
		}

		StringWriter str = new StringWriter();
		PrintWriter writer = new PrintWriter(str);
		try {
			ex.printStackTrace(writer);
			return str.getBuffer().toString();
		} finally {
			try {
				str.close();
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Gets the deepest throwable.
	 * 
	 * @param t
	 *            the t
	 * @return the deepest throwable
	 */
	public static Throwable getDeepestThrowable(Throwable t) {
		Throwable parent = t;
		Throwable child = t.getCause();
		while (null != child) {
			parent = child;
			child = parent.getCause();
		}
		return parent;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getExceptionMessages() {
		return exceptionMessages;
	}

	public void setExceptionMessages(String exceptionMessages) {
		this.exceptionMessages = exceptionMessages;
	}

	public long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}

	public String getArgs0() {
		return args0;
	}

	public void setArgs0(String args0) {
		this.args0 = args0;
	}

	public String getArgs1() {
		return args1;
	}

	public void setArgs1(String args1) {
		this.args1 = args1;
	}

	public String getArgs2() {
		return args2;
	}

	public void setArgs2(String args2) {
		this.args2 = args2;
	}

	/**
	 * Gets the stack trace as string.
	 *
	 * @param exception
	 *            the exception
	 * @return the stack trace as string
	 */
	public static String getStackTraceAsString(Throwable exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.print(" [ ");
		pw.print(exception.getClass().getName());
		pw.print(" ] ");
		pw.print(exception.getMessage());
		exception.printStackTrace(pw);
		return sw.toString();
	}
	
}
