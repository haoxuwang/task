package com.wanghao.task;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public class Main {
    public static void main(String[] args) {
    	Map<String, Person> map=new HashMap<>();
    	map.put("张三", new Person("70", "49", "73", "70"));
    	map.put("李四", new Person("1094", "49", "73", "70"));
    	Service service=new Service(map);
    	List<String[]> rebei=Util.readFile("C:/Users/Administrator/Desktop/666.txt");
    	Iterator<String[]> iterator= rebei.iterator();
    	while (iterator.hasNext()) {
    		String[] task=(String[]) iterator.next();
    		service.add(task, "2020-05-18");
			String taskId=service.getId("admin","123456",task);
			service.finish("admin","123456", taskId, task);
			service.check("admin", "123456", taskId);
		}
    }
    
}
