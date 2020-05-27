package com.wanghao.task;
public class Person {
	/*id
	任务下达人员id
	项目组id
	项目经理id*/
	private String id;
	private String infocomId;
	private String projectTeam;
	private String projectManager;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfocomId() {
		return infocomId;
	}
	public void setInfocomId(String infocomId) {
		this.infocomId = infocomId;
	}
	public String getProjectTeam() {
		return projectTeam;
	}
	public void setProjectTeam(String projectTeam) {
		this.projectTeam = projectTeam;
	}
	public String getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	public Person(String id, String infocomId, String projectTeam,
				  String projectManager) {
		super();
		this.id = id;
		this.infocomId = infocomId;
		this.projectTeam = projectTeam;
		this.projectManager = projectManager;
	}
	public Person() {
		super();
	}

}
