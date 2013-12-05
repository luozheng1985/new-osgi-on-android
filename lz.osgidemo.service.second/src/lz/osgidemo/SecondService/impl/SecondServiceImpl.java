package lz.osgidemo.SecondService.impl;

import lz.osgidemo.SecondService.SecondServiceDao;

public class SecondServiceImpl implements SecondServiceDao {

	private String msg;
	@Override
	public void setCompareMsg(String cmpMsg) {
		// TODO Auto-generated method stub
		msg = cmpMsg;
	}

	@Override
	public boolean isHelloMessage(String message) {
		if(message.equals(msg)){
			return true;
		}
		return false;
	}

}
