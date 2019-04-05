package utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Student")
public class Student {
	 //姓名
	@XStreamAlias("Name")
    private String name;
    //年龄
	@XStreamAlias("Age")
    private String age;
    //住址
	@XStreamAlias("Address")
    private String address;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    @Override
    public String toString() {
        return "Student [name=" + name + ", age=" + age + ", address="
                + address + "]";
    }

}
