/*
 * Copyright (C) 2011, Neticoa SAS France - Tous droits réservés.
 * Author(s) : Franck Bonin, Neticoa SAS France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codehaus.mojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rmi.CORBA.Util;


/**
 * Goal which Launch an external executable.
 *
 * @author Franck Bonin 
 * @goal launch
 * 
 */
public class LaunchMojo extends AbstractLaunchMojo {

    /**
     * Can be of type <code>&lt;argument&gt;</code> or <code>&lt;classpath&gt;</code> Can be overriden using "cxx.args"
     * env. variable
     * 
     * @parameter
     * @since 0.0.4
     */
	private List arguments;
	protected List getArgsList() {
		return arguments;
	}

    /**
     * Arguments for the executed program
     * 
     * @parameter expression="${launch.args}"
     */
	private String commandArgs;
	protected String getCommandArgs() {
		return commandArgs;
	}

    /**
     * The executable. Can be a full path or a the name executable. In the latter case, the executable must be in the
     * PATH for the execution to work.
     * 
     * @parameter expression="${launch.executable}"
     * @required
     * @since 0.0.4
     */
    private String executable;
	protected String getExecutable() {
		return executable;
	}

    /**
     * Environment variables to pass to the executed program.
     * 
     * @parameter
     * @since 0.0.4
     */
    private Map environmentVariables = new HashMap();
	protected Map getMoreEnvironmentVariables() {
		return environmentVariables;
	}

    /**
     * Exit codes to be resolved as successful execution for non-compliant applications (applications not returning 0
     * for success).
     * 
     * @parameter
     * @since 0.0.4
     */
    private List successCodes;
	protected List getSuccesCode() {
		return successCodes;
	}

    /**
     * The current working directory. Optional. If not specified, basedir will be used.
     * 
     * @parameter expression="${launch.workingdir}"
     * @since 0.0.4
     */
    private File workingDir;
	protected File getWorkingDir() {
		if (null == workingDir) {
			workingDir = new File(basedir.getPath());
		}
		return workingDir;
	}

    /**
     * Os for which command shall be executed
     * os name match is java.lang.System.getProperty("os.name")
     * 
     * @parameter
     * @since 0.0.4
     */
    private List includeOS;
    
    /**
     * Os for which command shall not be executed
     * os name match is java.lang.System.getProperty("os.name")
     *  
     * @parameter
     * @since 0.0.4
     */
    private List excludeOS;
	
	protected boolean isSkip() {
		String sOsName = System.getProperty("os.name");
		if (null == excludeOS && null == includeOS)	{
			return false;
		} else if (null == includeOS) {
			return excludeOS.contains(sOsName);
		} else if (null == excludeOS) {
			return !includeOS.contains(sOsName);
		} else {
			return (excludeOS.contains(sOsName) || !includeOS.contains(sOsName));
		}
	}
	
	/**
     * The Report OutputStreamOut Location.
     * 
     * @parameter expression="${launch.outputStreamOut}" default-value=""
     * @since 0.0.5
     */
    private File outputStreamOut;
    protected OutputStream getOutputStreamOut(){
    	String sOutputStreamOut = new String();
    	if (null != outputStreamOut && !outputStreamOut.toString().isEmpty())
    	{
			if (outputStreamOut.isAbsolute()) {
				sOutputStreamOut = outputStreamOut.getPath();
			} else {
				sOutputStreamOut = basedir.getAbsolutePath() + "/" + outputStreamOut.getPath();
			}
			
			getLog().info( "Launch output location " + sOutputStreamOut );
			 
			OutputStream output = super.getOutputStreamOut();
		    File file = new File(sOutputStreamOut);
		    try {
		    	new File(file.getParent()).mkdirs();
				file.createNewFile();
			    output = new FileOutputStream(file);
			} catch (IOException e) {
				getLog().error( "Launch report redirected to stout since " + sOutputStreamOut + " can't be opened" );
		    }
	    	return output;
    	}
    	else
    	{
    		return super.getOutputStreamOut();
    	}
    }
    
	/**
     * The Report OutputStreamErr Location.
     * 
     * @parameter expression="${launch.outputStreamErr}" default-value=""
     * @since 0.0.5
     */
    private File outputStreamErr;
    protected OutputStream getOutputStreamErr(){
    	String sOutputStreamErr = new String();
    	if (null != outputStreamErr && !outputStreamErr.toString().isEmpty())
    	{
			if (outputStreamErr.isAbsolute()) {
				sOutputStreamErr = outputStreamErr.getPath();
			} else {
				sOutputStreamErr = basedir.getAbsolutePath() + "/" + outputStreamErr.getPath();
			}
			
			getLog().info( "Launch erroutput location " + sOutputStreamErr );
			 
			OutputStream output = super.getOutputStreamErr();
		    File file = new File(sOutputStreamErr);
		    try {
		    	new File(file.getParent()).mkdirs();
				file.createNewFile();
			    output = new FileOutputStream(file);
			} catch (IOException e) {
				getLog().error( "Launch report redirected to stout since " + sOutputStreamErr + " can't be opened" );
		    }
	    	return output;
    	}
    	else
    	{
    		return super.getOutputStreamErr();
    	}
    }
    
	/**
     * The Report InputStream Location.
     * 
     * @parameter expression="${launch.inputStream}" default-value=""
     * @since 0.0.5
     */
    private File inputStream;
    protected InputStream getInputStream(){
    	String sInputStream = new String();
    	if (null != inputStream && !inputStream.toString().isEmpty())
    	{
			if (inputStream.isAbsolute()) {
				sInputStream = inputStream.getPath();
			} else {
				sInputStream = basedir.getAbsolutePath() + "/" + inputStream.getPath();
			}
			
			getLog().info( "Launch input location " + sInputStream );
			 
			InputStream input = super.getInputStream();
		    File file = new File(sInputStream);
		    try {
		    	new File(file.getParent()).mkdirs();
				file.createNewFile();
				input = new FileInputStream(file);
			} catch (IOException e) {
				getLog().error( "Launch report redirected to stout since " + sInputStream + " can't be opened" );
		    }
	    	return input;
    	}
    	else
    	{
    		return super.getInputStream();
    	}
    }

}
