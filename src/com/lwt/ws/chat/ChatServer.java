package com.lwt.ws.chat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.utils.Base64;

@ServerEndpoint("/ChatServer")
public class ChatServer {
	private static Map<Session, String> users = new HashMap<Session, String>();
	private static BufferedWriter record_w = null;
	@OnOpen
	public void onOpen(Session session) throws Exception {
		String nickname = session.getQueryString();
		nickname = new String(Base64.decode(nickname), "utf8");
		synchronized (users) {
			while(users.containsValue(nickname)){
				String tail = Utils.randomString(4, 10);
				nickname += tail;
			}
			users.put(session, nickname);
		}
		String msg = "<div class='text-center text-primary sysinfo'>系统提示： " + Utils.getTime("", "") + " " + nickname + " 加入聊天</div>";
		for(Session user : users.keySet()){
			user.getBasicRemote().sendText(msg);
		}
		System.out.println("open.. " + users);
	}
	
	@OnMessage
	public String onMessage(Session session, String message) throws IOException {
		String nickname = users.get(session);
		Map<String, String> res = new HashMap<String, String>();
		res.put("nickname", nickname);
		res.put("time", Utils.getTime("", ""));
//		res.put("msg", message);
		res.put("msg", message.replace("\n", "<br>").replace(" ", "&nbsp;"));
		ObjectMapper objMapper = new ObjectMapper();
		String rtn = objMapper.writeValueAsString(res);
//		String record = nickname + ": " + Utils.getTime("", "") + " " + message;
		synchronized (users) {
			//write record file
			if(record_w == null){
				record_w = new BufferedWriter(new FileWriter("e:/mnt/record", true));
			}
			record_w.write(rtn + "\n");
			record_w.flush();
			
			for(Session user : users.keySet()){
				if(!user.equals(session)){
					user.getBasicRemote().sendText(rtn);
				}
			}
		}
//		{"time":"(11-30 23:1:42)","nickname":"小新4651","msg":"聊天内容。。。"}
		rtn = rtn.substring(0, rtn.length()-1) + ",\"me\":\"1\"}";
		return rtn;
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {
		String nickname = users.get(session);
		users.remove(session);
		String msg = "<div class='text-center text-muted sysinfo'>系统提示： " + Utils.getTime("", "") + " " + nickname + " 离开聊天</div>";
		for(Session user : users.keySet()){
			user.getBasicRemote().sendText(msg);
		}
		System.out.println("close.. " + users);
	}
}
