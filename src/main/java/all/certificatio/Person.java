package all.certificatio;

public class Person {
    private String name;
    private String id;
    private String username;

    public Person(){

    }
public Person(String name, String id){
    this.name=name;
    this.id=id;
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}