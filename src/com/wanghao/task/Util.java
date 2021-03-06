package com.wanghao.task;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
public class Util {
	//把每一行转为list,每个元素为文件内的一行，类型为String[]
	public static List<String[]> readFile(String file) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			FileReader fr=new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			String line;
			while ((line=br.readLine())!=null) {
				String[] arrs=line.split("!");
				list.add(arrs);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	//带cookie但不返回cookie的请求，用于业务操作，调用了resquest（返回HttpResponse的"实体"的字符串形式）
	public static String resquestForString(Method method,String url,List<NameValuePair> formParams,String cookie) {
		HttpResponse response = Util.resquest(method,url,formParams,null,cookie);
		HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
		String content = null;
		try {
			content = EntityUtils.toString(result);//用httpcore.jar提供的工具类将"实体"转化为字符串
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
		//System.out.println(content);
		return content;
	}
	//通用请求，返回HttpResponse
	public static HttpResponse resquest(Method method,String url,List<NameValuePair> formParams,CookieStore cookieStore,String cookie) {
		HttpResponse response = null;
		try {
			HttpClient client;
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");//将表单参数转化为“实体”
			String paramsStr="";
			HttpRequestBase resquest;
			if(method.equals("get")){
				paramsStr=EntityUtils.toString(entity);
				resquest= new HttpGet(url+paramsStr);    //构建一个get请求
			}
			else {
				resquest= new HttpPost(url+paramsStr);
				((HttpPost) resquest).setEntity(entity); //将“实体“设置到POST请求里
			}

			//判断是否需要返回cookieStore
			if(cookieStore!=null)
				client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();               //构建一个Client
			else
				client = HttpClientBuilder.create().build();
			//判断是否需要带cookie请求
			if(cookie!=null)
				resquest.setHeader("Cookie", cookie);
			resquest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			resquest.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
			response = client.execute(resquest);//提交POST请求

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
}
