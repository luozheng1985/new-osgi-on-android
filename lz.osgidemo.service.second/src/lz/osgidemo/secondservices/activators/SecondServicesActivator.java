/**
 * Copyright (c) 2011 Zsolt Török.
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0, 
 * available at http://www.eclipse.org/legal/epl-v10.html  
 */
package lz.osgidemo.secondservices.activators;

import java.util.ArrayList;
import java.util.List;

import lz.osgidemo.SecondService.SecondServiceDao;
import lz.osgidemo.SecondService.impl.SecondServiceImpl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class SecondServicesActivator implements BundleActivator {

	private List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();
	
	public void start(BundleContext context) throws Exception {
		System.out.println("SecondServicesActivator Service bundle started.");
		//serviceRegistrations.add(context.registerService(HelloServiceDao.class.getName(), new HelloServiceImpl(), null));
		serviceRegistrations.add(context.registerService(SecondServiceDao.class.getName(), new SecondServiceImpl(), null));
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("SecondServicesActivator Service bundle stopped.");
		for(ServiceRegistration srs : serviceRegistrations){
			srs.unregister();
		}
	}

}
