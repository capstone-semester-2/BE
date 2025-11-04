package capstone.demo.domain.user;

public enum LoginType {
    KAKAO("카카오");

    private final String description;

    LoginType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
