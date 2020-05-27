package com.wanghao.task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import com.alibaba.fastjson.JSONObject;
public class Service {
	public Map<String, Person> map=new HashMap<>();
	Log log=LogFactory.getLog(Service.class);
	public Service(Map<String, Person> map) {
		this.map = map;
	}
	public void check(String username,String password,String taskId){
		String adminCookie=getCookie(username, password);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		String s=Util.resquestForString(Method.post,"http://localhost/vmt/task/getTaskById/"+taskId, formParams, adminCookie);
		JSONObject task=JSONObject.parseObject(s).getJSONObject("extend").getJSONObject("task");
		List<NameValuePair> formParams1 = new ArrayList<NameValuePair>();
		formParams1.add(new BasicNameValuePair("taskId", taskId));
		formParams1.add(new BasicNameValuePair("taskTitle", task.getString("title")));
		formParams1.add(new BasicNameValuePair("content", task.getString("content")));
		formParams1.add(new BasicNameValuePair("startTime", task.getString("startTime")));
		formParams1.add(new BasicNameValuePair("endTime", task.getString("endTime")));
		formParams1.add(new BasicNameValuePair("dispatchTime", task.getString("endTime")));
		formParams1.add(new BasicNameValuePair("taskStatus", "3"));
		formParams1.add(new BasicNameValuePair("projectTeam", task.getString("projectTeamId")));
		formParams1.add(new BasicNameValuePair("projectManager", task.getString("managerId")));
		formParams1.add(new BasicNameValuePair("attachment", ""));
		formParams1.add(new BasicNameValuePair("taskType", "0"));
		formParams1.add(new BasicNameValuePair("sendTimestamp", task.getString("sendTimestamp")));
		formParams1.add(new BasicNameValuePair("feedbackTime", task.getString("feedbackTime")));
		formParams1.add(new BasicNameValuePair("useTime", task.getString("useTime")));
		//2.2保存
		String s1=Util.resquestForString(Method.post,"http://localhost/vmt/task/updateTask",formParams1,adminCookie);
		JSONObject result=JSONObject.parseObject(s1);
		if(result.getString("code").equals("100"))
			log.info("审核成功 "+taskId);

		else
			log.info("审核失败");
	}
	public void finish(String username,String password,String taskId,String[] task){
		//1.登录admin获取 cookie
		String adminCookie=getCookie(username, password);
		//2封装参数
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		formParams.add(new BasicNameValuePair("taskId", taskId));
		formParams.add(new BasicNameValuePair("staffId", map.get(task[6]).getId()));
		formParams.add(new BasicNameValuePair("taskStatus", "2"));
		formParams.add(new BasicNameValuePair("feedbackContent", "已完成"));
		formParams.add(new BasicNameValuePair("feedbackFiles", ""));
		formParams.add(new BasicNameValuePair("taskType", "0"));
		formParams.add(new BasicNameValuePair("useTime", task[4]));
		formParams.add(new BasicNameValuePair("feedBackTime", ""));
		//3分派
		String s=Util.resquestForString(Method.post,"http://localhost/vmt/task/updateTaskByVendorUser",formParams,adminCookie);
		JSONObject result=JSONObject.parseObject(s);
		if(result.getString("code").equals("100"))
			log.info("分派成功 "+task[6]+" "+task[1]+" "+task[2]+" "+task[3]);
		else
			log.info("分派失败");
	}
	public String getId(String username,String password,String[] task){
		//1.获取cookie
		String adminCookie=getCookie(username, password);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		formParams.add(new BasicNameValuePair("pn", "1"));
		formParams.add(new BasicNameValuePair("managerId", map.get(task[5]).getProjectManager()));
		formParams.add(new BasicNameValuePair("taskTitle", ""));
		formParams.add(new BasicNameValuePair("projectTeam", ""));
		formParams.add(new BasicNameValuePair("staffId", ""));
		formParams.add(new BasicNameValuePair("startTime", "1990-01-01 00:00:00"));
		formParams.add(new BasicNameValuePair("endTime", "2200-01-01 00:00:00"));
		formParams.add(new BasicNameValuePair("taskStatus", "1"));
		String s=Util.resquestForString(Method.get,"http://localhost/vmt/task/getVendorTaskInfo?", formParams, adminCookie);
		JSONObject newTask=JSONObject.parseObject(s).getJSONObject("extend").getJSONObject("pmTask" ).getJSONArray("list").getJSONObject(0);
		JSONObject result=JSONObject.parseObject(s);
		if(result.getString("code").equals("100"))
			log.info("获取id成功，id："+newTask.getString("id"));

		else
			log.info("获取id失败，id："+newTask.getString("id"));
		return newTask.getString("id");
	}
	public void add(String[] task,String date){
		//1.登录admin获取 cookie
		String adminCookie=getCookie("linpeng", "Admin@123");
		//2封装参数
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		formParams.add(new BasicNameValuePair("taskTitle", task[0]));
		formParams.add(new BasicNameValuePair("content", task[1]));
		formParams.add(new BasicNameValuePair("startTime", date+" "+task[2]));
		formParams.add(new BasicNameValuePair("endTime", date+" "+task[3]));
		formParams.add(new BasicNameValuePair("taskStatus", "1"));
		formParams.add(new BasicNameValuePair("taskType", "0"));
		formParams.add(new BasicNameValuePair("infocomId", map.get(task[5]).getInfocomId()));
		formParams.add(new BasicNameValuePair("projectTeam", map.get(task[5]).getProjectTeam()));
		formParams.add(new BasicNameValuePair("projectManager", map.get(task[5]).getProjectManager()));
		formParams.add(new BasicNameValuePair("attachment", ""));
		formParams.add(new BasicNameValuePair("useTime", ""));
		formParams.add(new BasicNameValuePair("sendTimestamp", ""));
		//3.保存
		String s=Util.resquestForString(Method.post,"http://localhost/vmt/task/saveTask",formParams,adminCookie);
		JSONObject result=JSONObject.parseObject(s);
		if(result.getString("code").equals("100"))
			log.info("添加成功 "+task[6]+" "+task[1]+" "+task[2]+" "+task[3]);

		else
			log.info("添加失败");
	}
	//用于登录返回cookie，调用了post
	public String getCookie(String username,String password) {
		String cookie="";
		CookieStore cookieStore=new BasicCookieStore();
		//构建表单参数
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		formParams.add(new BasicNameValuePair("userName", username));
		formParams.add(new BasicNameValuePair("password", password));
		Util.resquest(Method.post,"http://localhost/vmt/login/doLogin",formParams,cookieStore,null);
		List<Cookie> cookies=cookieStore.getCookies();
		for(Cookie cook:cookies){
			cookie=cook.getName()+"="+cook.getValue();
			log.info(username+"登录成功，cookie:"+cookie);
		}
		return cookie;
	}
}
