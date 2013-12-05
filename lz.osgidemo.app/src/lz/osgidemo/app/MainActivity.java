/**
 * Copyright (c) 2011 Zsolt Török.
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0, 
 * available at http://www.eclipse.org/legal/epl-v10.html  
 */
package lz.osgidemo.app;

import lz.osgidemo.SecondService.SecondServiceDao;
import lz.osgidemo.SecondService.impl.SecondServiceImpl;
import lz.osgidemo.firstService.HelloServiceDao;
import lz.osgidemo.firstService.impl.HelloServiceImpl;
import lz.osgidemo.firstbll.User;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author zsolt
 */
public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "EmbeddedOsgiService";
	
	private Button btn_install, btn_uninstall;
	private EditText edit_message;

	public static Intent createIntent(Context cxt, String msg) {
		Intent it = new Intent(cxt, MainActivity.class);
		it.putExtra("Message", msg);
		return it;
	}

	@Override
	public synchronized void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		startService(new Intent(this, EmbeddedOsgiService.class));
		btn_install = (Button) findViewById(R.id.btn_install);
		btn_uninstall = (Button) findViewById(R.id.btn_uninstall);
		edit_message = (EditText) findViewById(R.id.edit_message);
		btn_install.setOnClickListener(this);
		btn_uninstall.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EmbeddedOsgiService.unTrackerService(HelloServiceImpl.class.getName());
		EmbeddedOsgiService.unTrackerService(SecondServiceImpl.class.getName());
		stopService(new Intent(this, EmbeddedOsgiService.class));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_install:
			Log.d(TAG, "R.id.btn_install");
			//EmbeddedOsgiService.getFelix().getBundleContext().registerService(SecondServiceDao.class.getName(), new SecondServiceImpl(), null);
			EmbeddedOsgiService.trackerServer(HelloServiceDao.class.getName(), new MServiceTrackerCustomizer(HelloServiceDao.class.getName()));
			EmbeddedOsgiService.trackerServer(SecondServiceDao.class.getName(), new MServiceTrackerCustomizer(SecondServiceDao.class.getName()));
			
			String content = "serviceParams ooo ";
			ServiceReference firstRef = EmbeddedOsgiService.getFelix().getBundleContext().getServiceReference(HelloServiceDao.class.getName());
			ServiceReference secondRef = EmbeddedOsgiService.getFelix().getBundleContext().getServiceReference(SecondServiceDao.class.getName());
			if(firstRef != null){
				try {
					HelloServiceDao helloOsgi = (HelloServiceDao) EmbeddedOsgiService.getFelix().getBundleContext().getService(firstRef);
					if(helloOsgi != null){
						helloOsgi.setMessage("AAAAAA");
						if(secondRef != null){
							User user = new User("User name is OSGI");
							helloOsgi.setUser(user);
							SecondServiceDao second = (SecondServiceDao) EmbeddedOsgiService.getFelix().getBundleContext().getService(secondRef);
							second.setCompareMsg(helloOsgi.getMessage());
							boolean f = second.isHelloMessage(content);
							edit_message.setText(f + "==>" + helloOsgi.getMessage());
						}else{
							System.out.println("SecondServiceImpl is null");
						}
					}else{
						System.out.println("HelloServiceImpl is null");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("HelloServiceImpl server is no exist!!!");
			}
			break;
		case R.id.btn_uninstall:
			EmbeddedOsgiService.unTrackerService(HelloServiceImpl.class.getName());
			EmbeddedOsgiService.unTrackerService(SecondServiceImpl.class.getName());
			edit_message.setText("");
			break;
		default:
			break;
		}
	}

	
	
	private class MServiceTrackerCustomizer implements ServiceTrackerCustomizer{

		private String serviceName;
		
		public MServiceTrackerCustomizer(String serviceName) {
			super();
			this.serviceName = serviceName;
		}

		@Override
		public Object addingService(ServiceReference arg0) {
			// TODO Auto-generated method stub
			//ServiceReference ref = EmbeddedOsgiService.getFelix().getBundleContext().getServiceReference(serviceName);
			Object service = EmbeddedOsgiService.getFelix().getBundleContext().getService(arg0);
			Log.v(TAG, "addingService--->" + arg0);
//			if(service instanceof HelloServiceImpl){
//				HelloServiceImpl service2 = (HelloServiceImpl) service;
//				service2.setMessage("firstServiceData");
//				edit_message.setText(service2.getMessage());
//			}else if(service instanceof SecondServiceImpl){
//				
//			}
			return service;
		}

		@Override
		public void modifiedService(ServiceReference arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removedService(ServiceReference arg0, Object arg1) {
			// TODO Auto-generated method stub
			Log.v(TAG, "removedService--->" + arg1);
			EmbeddedOsgiService.getFelix().getBundleContext().ungetService(arg0);
			edit_message.setText("");
		}
	}

}
