import java.util.UUID;

public class Member {
    private String id;
    private String name;
    private String mobile;

    public Member(String name, String mobile) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}