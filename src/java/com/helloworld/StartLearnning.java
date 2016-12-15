package com.helloworld;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tools.ant.taskdefs.Sleep;
import org.dom4j.Element;
import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.OfflineMessageStore;
import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.RoutingTable;
import org.jivesoftware.openfire.SessionResultFilter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.JiveGlobals;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;






public class StartLearnning implements Plugin,PacketInterceptor{
	private long count1=0;	
	private long count=0;
	private List<JID> allowedUsers;
	private XMPPServer server;
	private String sql = null;
	private DBHelper helper = null;
	private ResultSet ret =null;
	private List<String> list = new ArrayList<>();
	private List<String> onLineList = new ArrayList<>();
	private List<String> allLineList = new ArrayList<>();
	private String id = "";
	private InterceptorManager interceptorManager;   
	private static PluginManager pluginManager;
	private UserManager userManager;
	private PresenceManager presenceManager;
	private OfflineMessageStore offline;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	public StartLearnning(){
		interceptorManager = InterceptorManager.getInstance();
	}
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
        interceptorManager.addInterceptor(this);

        XMPPServer server = XMPPServer.getInstance();
        AuthCheckFilter.addExclude("online/status");
        userManager = server.getUserManager();
        presenceManager = server.getPresenceManager();        
        pluginManager = manager;
		server = XMPPServer.getInstance();
		System.out.println("start>>>>>>>>>>>>");
		sql = "select username from ofuser";
		if(list!=null){
			list.clear();
		}
		helper = new DBHelper(sql);
		try {
			ret = helper.pst.executeQuery();
			while(ret.next()){
				list.add(ret.getString(1)+"@desktop-g51omli");
				System.out.println(ret.getString(1));
			}
			ret.close();
			helper.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
				try {
					Thread.sleep(20000);
//					XMPPServer.getInstance().getSessionManager().sendServerMessage(null, new Date()+" : "+"send message");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getOnline();
				count++;
				if(!allLineList.isEmpty()){
				allLineList.clear();
				}
				allLineList.addAll(list);
				for(int i2 = 0;i2<allLineList.size();i2++){
					System.out.println("allLineList : "+allLineList.get(i2));
				}
				allLineList.removeAll(onLineList);
				String ss = count+" : "+sdf.format(new Date());
				for(int i =0;i<onLineList.size();i++){					
					JID j = new JID(onLineList.get(i));
				    XMPPServer.getInstance().getSessionManager().sendServerMessage(j,"god",ss);
				    System.out.println("onLine : "+onLineList.get(i)+">>>>>"+count);
				}
				System.out.println(allLineList.size());
				OfflineMessageStore offlineMessageStore = new OfflineMessageStore();
				for(int i1 = 0;i1<allLineList.size();i1++){
					JID j = new JID(allLineList.get(i1));
					System.out.println("offLine : "+allLineList.get(i1)+">>>>>"+count);
					Message message1 = new Message();
			        message1.setFrom("father");
			        message1.setTo(j);
			        message1.setSubject("god");
			        message1.setBody(ss);
			        offlineMessageStore.addMessage(message1);
				}
	
				}
			}
		}).start();
		}
	private void getOnline() {
		int sessionCount = XMPPServer.getInstance().getSessionManager().getUserSessionsCount(false);	
		SessionResultFilter filter = SessionResultFilter.createDefaultSessionFilter();	
		filter.setSortOrder(0);	
		filter.setStartIndex(0);		
		filter.setNumResults(sessionCount);
		Collection<ClientSession> sessions = XMPPServer.getInstance().getSessionManager().getSessions(filter);
		StringBuilder sb = new StringBuilder();
		int	nCount = 0;
		if(onLineList!=null) onLineList.clear();
		for (ClientSession sess : sessions) 
		{		
			//String strTmp = URLEncoder.encode(sess.getAddress().toString(), "UTF-8");
			String strTmp = sess.getAddress().getNode().toString();	
			onLineList.add(strTmp+"@desktop-g51omli");
		}
		
	}
	@Override
	public void destroyPlugin(){
		System.out.println("finish>>>>>>>>>>>");
		
	}
	@Override
	public void interceptPacket(Packet packet, Session session,
			boolean incoming, boolean processed) throws PacketRejectedException {
//		  System.out.println("" + packet.toXML());
//		  OfflineMessageStore offlineMessageStore = new OfflineMessageStore();
//		  Packet copyPacket = packet.createCopy();
//		  Message message = (Message) copyPacket;
//		  if(message.getBody().contains("<body>")){
//          offlineMessageStore.addMessage(message);
//		  }
	}

}
