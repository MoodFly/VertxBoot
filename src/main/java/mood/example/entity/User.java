package mood.example.entity;

import lombok.ToString;

@ToString
public class User {
    private Integer id;
    private String name;
    private String job;
    private String sex;
    public User(Integer id){ this.id=id;}
    public User(Integer id,String name,String job,String sex){
    this.id=id;this.name=name;this.job=job;this.sex=sex;
    }
    public User(String name,String job,String sex){
        this.name=name;this.job=job;this.sex=sex;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getSex() {
        return sex;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return " 姓名："+name+" 工作："+job+" 性別"+sex+"\n";
    }
}
