package cz.hocuspocus.coffeeblog.models.dto;

import java.time.LocalDate;
import java.time.Period;

public class ProfileDTO {

    private long id;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private String interests;

    private String aboutMe;

    private int age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        calculateAge();
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private void calculateAge() {
        if (birthday != null) {
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(birthday, currentDate);
            this.age = period.getYears();
        } else {
            this.age = 0;
        }
    }

}
