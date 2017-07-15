package snippet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class GsonBuilderBeanOne {
	@Expose
	private String username;
	@Expose(serialize = false, deserialize = true)
	private String password;
	@Expose(serialize = true, deserialize = false)
	private String school;
	private String classroom;
	@Expose(serialize = false)
	private String sex;

	public GsonBuilderBeanOne(String username, String password, String school, String classroom, String sex) {
		super();
		this.username = username;
		this.password = password;
		this.school = school;
		this.classroom = classroom;
		this.sex = sex;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getSchool() {
		return school;
	}

	public String getClassroom() {
		return classroom;
	}

	public String getSex() {
		return sex;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {

		String resultString = "";
		resultString += "username:" + username + "\npassword:" + password + "\nschool:" + school + "\nclassroom:"
				+ classroom + "\nsex:" + sex + "\n";

		return resultString;
	}

	public static void main(String[] args) {
		Gson gson2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		String showString = "";
		String jsonString = "{'username':'144','password':'123','school':'华软','classroom':'软工五班','sex':'男'}";

		showString += "json:" + jsonString + "\n解析后的数据：\n";
		System.out.println(showString);
		GsonBuilderBeanOne beanOne = gson2.fromJson(jsonString, GsonBuilderBeanOne.class);
		String str = gson2.toJson(beanOne);

		System.out.println(str);
	}

}