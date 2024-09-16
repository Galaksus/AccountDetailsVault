package com.my.arkku.data;

public class Credential {
    private int id;
    private int categoryId;
    private String email;
    private String username;
    private String password;
    private String additionalInformation;

    public Credential(int id, int categoryId, String email, String username, String password, String additionalInformation) {
        this.id = id;
        this.categoryId = categoryId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.additionalInformation = additionalInformation;
    }

    // Getters and toString() method for easy logging
    public int getId() { return id; }
    public int getCategoryId() { return categoryId; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getAdditionalInformation() { return additionalInformation; }

    @Override
    public String toString() {
        return "Credential{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                '}';
    }
}
