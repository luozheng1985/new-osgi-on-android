/**
 * Copyright (c) 2011 Zsolt Török.
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0, 
 * available at http://www.eclipse.org/legal/epl-v10.html  
 */
package lz.osgidemo.firstService.impl;

import lz.osgidemo.firstService.HelloServiceDao;
import lz.osgidemo.firstbll.User;

public class HelloServiceImpl implements HelloServiceDao{

	private String message;
	
	@Override
	public void setMessage(String msg) {
		message = msg;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

	@Override
	public void setUser(User user) {
		// TODO Auto-generated method stub
		System.out.println("##################" + user);
	}
	
}
