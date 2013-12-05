/**
 * Copyright (c) 2011 Zsolt T枚r枚k.
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0, 
 * available at http://www.eclipse.org/legal/epl-v10.html  
 */
package lz.osgidemo.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import lz.osgidemo.firstservices.activators.HelloServicesActivator;
import lz.osgidemo.secondservices.activators.SecondServicesActivator;

import org.apache.felix.framework.Felix;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author zsolt
 * 
 */
public class EmbeddedOsgiService extends Service {

	private static final String TAG = EmbeddedOsgiService.class.getSimpleName();

	private static Felix felix;
	private static Map<String, ServiceTracker> trackers;

	// private Felix felix;
	// private ServiceTracker tracker;

	public EmbeddedOsgiService() {
	}

	@Override
	public void onCreate() {
		initOsgiFelix();
		Log.d(getClass().getSimpleName(), "onCreate");
		// Thread osgiInitThread = new Thread(new Runnable() {
		// public void run() {
		// Properties config = System.getProperties();
		// config.put("org.osgi.framework.startlevel.beginning", "0");
		// config.put("org.osgi.framework.storage.clean", "onFirstInit");
		//
		// // make sure the OSGi cache dir is set to something sensible
		// File cacheDir = EmbeddedOsgiService.this.getDir("osgi.cache",
		// Context.MODE_PRIVATE);
		// Log.d(getClass().getSimpleName(), "Setting osgi cache location to: "
		// + cacheDir.getAbsolutePath());
		// config.put("org.osgi.framework.storage", cacheDir.getAbsolutePath());
		// config.put("felix.log.level", "1");
		//
		// List<BundleActivator> activators = new ArrayList<BundleActivator>();
		// activators.add(new ServiceInterfaceBundleActivator());
		// activators.add(new ChinaBundleActivator());
		// config.put("felix.systembundle.activators", activators);
		//
		// try {
		// // Create an instance of the framework with our
		// // configuration properties
		// Log.d(EmbeddedOsgiService.class.getSimpleName(),
		// "Starting Felix...");
		// felix = new Felix(config);
		// // Start Felix instance
		// felix.start();
		//
		// tracker = new ServiceTracker(felix.getBundleContext(),
		// felix.getBundleContext().createFilter("(" + Constants.OBJECTCLASS +
		// "=" + HelloService.class.getName() + ")"),
		// new ServiceTrackerCustomizer() {
		// public Object addingService(ServiceReference ref) {
		// final HelloService service = (HelloService)
		// felix.getBundleContext().getService(ref);
		// showNotification("Added " + service.getClass().getSimpleName() + ".",
		// "Service says: '" + service.getMessage() + "'");
		// return service;
		// }
		//
		// public void modifiedService(ServiceReference ref, Object service) {
		// removedService(ref, service);
		// addingService(ref);
		// }
		//
		// public void removedService(ServiceReference ref, Object service) {
		// showNotification("Removed " + service.getClass().getSimpleName() +
		// ".", "");
		// felix.getBundleContext().ungetService(ref);
		// }
		// });
		// tracker.open();
		// } catch (BundleException e) {
		// Log.e(getClass().getSimpleName(), e.getMessage());
		// } catch (InvalidSyntaxException e) {
		// Log.e(getClass().getSimpleName(), e.getMessage());
		// }
		// }
		// });
		// osgiInitThread.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopOsgi();
		// tracker.close();
		// tracker = null;
		// try {
		// felix.stop();
		// felix.waitForStop(5000);
		// } catch (BundleException e) {
		// Log.e(getClass().getSimpleName(), e.getMessage());
		// } catch (InterruptedException e) {
		// Log.e(getClass().getSimpleName(), e.getMessage());
		// }
		// felix = null;
	}

	// private void showNotification(String title, String message) {
	// NotificationManager notificationManager = (NotificationManager)
	// getSystemService(NOTIFICATION_SERVICE);
	// Notification notification = new Notification(R.drawable.icon,
	// "OSGi Service Notification", System.currentTimeMillis());
	// Intent it = MainActivity.createIntent(this, title + " == " + message);
	// PendingIntent contentIntent = PendingIntent.getActivity(this, 0, it, 0);
	// notification.setLatestEventInfo(this, title, message, contentIntent);
	// notificationManager.notify(title.hashCode(), notification);
	// }

	/**
	 * 初始化Felix
	 */
	private void initOsgiFelix() {
		Properties config = System.getProperties();
		config.put("org.osgi.framework.startlevel.beginning", "0");
		config.put("org.osgi.framework.storage.clean", "onFirstInit");
		// make sure the OSGi cache dir is set to something sensible
		File cacheDir = this.getDir("osgi.cache", Context.MODE_PRIVATE);
		Log.d(getClass().getSimpleName(), "Setting osgi cache location to: " + cacheDir.getAbsolutePath());
		config.put("org.osgi.framework.storage", cacheDir.getAbsolutePath());
		config.put("felix.log.level", "1");
		config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, ANDROID_PACKAGES_FOR_EXPORT);
		// all bundles
		List<BundleActivator> activators = new ArrayList<BundleActivator>();
		addActivators(activators);
		config.put("felix.systembundle.activators", activators);
		// start felix
		try {
			felix = new Felix(config);
			felix.start();
			trackers = new HashMap<String, ServiceTracker>();
			Log.d(getClass().getSimpleName(), "initOsgiFelix");
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}
	
	private void addActivators(List<BundleActivator> activators){
		activators.add(new HelloServicesActivator());
		activators.add(new SecondServicesActivator());
//		activators.add(new HelloServiceClientActivator());
//		activators.add(new SecondServiceClientActivator());
	}

	/**
	 * 注册一个服务，也即是一个模块
	 * 
	 * @param serviceName
	 *            ：服务的名称，带包名
	 * @param stc
	 */
	public static void trackerServer(String implName, ServiceTrackerCustomizer stc) {
		try {
			if (trackers != null && trackers.get(implName) == null) {
				ServiceTracker tracker = new ServiceTracker(felix.getBundleContext(),//
						felix.getBundleContext().createFilter("(" + Constants.OBJECTCLASS + "=" + implName + ")"), stc);

				if (tracker != null) {
					tracker.open();
					trackers.put(implName, tracker);
					Log.d(TAG, "trackerServer===" + trackers.size());
				}
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}

	public static Felix getFelix() {
		return felix;
	}

	private void stopOsgi() {
		try {
			if (felix != null) {
				felix.stop();
				felix.waitForStop(5000);
			}
			felix = null;
		} catch (BundleException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		} catch (InterruptedException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}

		if (trackers != null) {
			for (Map.Entry<String, ServiceTracker> entry : trackers.entrySet()) {
				String serverName = entry.getKey();
				ServiceTracker tracker = entry.getValue();
				Log.i(TAG, serverName + "=" + tracker);
				tracker.close();
				tracker = null;
			}
			trackers.clear();
			trackers = null;
		}
	}

	/**
	 * 卸载掉某个服务，停止Tracker
	 * 
	 * @param serviceName
	 *            ：服务名，带包名
	 */
	public static void unTrackerService(String serviceName) {
		if (trackers != null && trackers.get(serviceName) != null) {
			ServiceTracker tracker = trackers.get(serviceName);
			tracker.close();
			tracker = null;
			trackers.remove(serviceName);
			Log.d(TAG, "closeServiceTracker===" + trackers.size());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	// copied from http://code.google.com/p/felix-on-android/
	private static final String ANDROID_PACKAGES_FOR_EXPORT =
	// ANDROID (here starts semicolon as separator -> Why?
	"android, " + //
			"android.app," + //
			"android.content," + //
			"android.database," + //
			"android.database.sqlite," + //
			"android.graphics, " + //
			"android.graphics.drawable, " + //
			"android.graphics.glutils, " + //
			"android.hardware, " + //
			"android.location, " + //
			"android.media, " + //
			"android.net, " + //
			"android.opengl, " + //
			"android.os, " + //
			"android.provider, " + //
			"android.sax, " + //
			"android.speech.recognition, " + //
			"android.telephony, " + //
			"android.telephony.gsm, " + //
			"android.text, " + //
			"android.text.method, " + //
			"android.text.style, " + //
			"android.text.util, " + //
			"android.util, " + //
			"android.view, " + //
			"android.view.animation, " + //
			"android.webkit, " + //
			"android.widget, " + //
			// JAVAx
			"javax.crypto; " + //
			"javax.crypto.interfaces; " + //
			"javax.crypto.spec; " + //
			"javax.microedition.khronos.opengles; " + //
			"javax.net; " + //
			"javax.net.ssl; " + //
			"javax.security.auth; " + //
			"javax.security.auth.callback; " + //
			"javax.security.auth.login; " + //
			"javax.security.auth.x500; " + //
			"javax.security.cert; " + //
			"javax.sound.midi; " + //
			"javax.sound.midi.spi; " + //
			"javax.sound.sampled; " + //
			"javax.sound.sampled.spi; " + //
			"javax.sql; " + //
			"javax.xml.parsers; " + //
			// JUNIT
			"junit.extensions; " + //
			"junit.framework; " + //
			// APACHE
			"org.apache.commons.codec; " + //
			"org.apache.commons.codec.binary; " + //
			"org.apache.commons.codec.language; " + //
			"org.apache.commons.codec.net; " + //
			"org.apache.commons.httpclient; " + //
			"org.apache.commons.httpclient.auth; " + //
			"org.apache.commons.httpclient.cookie; " + //
			"org.apache.commons.httpclient.methods; " + //
			"org.apache.commons.httpclient.methods.multipart; " + //
			"org.apache.commons.httpclient.params; " + //
			"org.apache.commons.httpclient.protocol; " + //
			"org.apache.commons.httpclient.util; " + //

			// OTHERS
			"org.bluez; " + //
			"org.json; " + //
			"org.w3c.dom; " + //
			"org.xml.sax; " + //
			"org.xml.sax.ext; " + //
			"org.xml.sax.helpers; " + //
			"net.neosum.android.view;";
}
