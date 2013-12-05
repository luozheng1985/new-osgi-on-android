/**
 * Copyright (c) 2011 Zsolt Török.
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0, 
 * available at http://www.eclipse.org/legal/epl-v10.html  
 */
package lz.osgidemo.firstService;

import lz.osgidemo.firstbll.User;

public interface HelloServiceDao {
	
	String getMessage();
	
	void setMessage(String msg);
	
	void setUser(User user);
}
