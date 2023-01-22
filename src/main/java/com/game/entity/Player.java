package com.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name ="player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "title")
    private String title;
    @Column(name = "race")
    @Enumerated(EnumType.STRING)
    private Race race;
    @Column(name = "profession")
    @Enumerated(EnumType.STRING)
    private Profession profession;
    @Column(name = "experience")
    private Integer experience;
    @Column(name = "level")
    private Integer level;
    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "banned")
    private Boolean banned = false;

    public Player(String name, String title, Race race, Profession profession, Date birthday, Integer experience) {

        this.id = id;
        this.setName(name);
        this.setTitle(title);
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.setExperience(experience);
    }

    public Player() {    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.length() > 0 && name.length() <= 12)     { this.name = name; }
        else this.name = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title.length() <= 30) { this.title = title;}
        else this.name = null;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        if(experience >= 0 && experience <= 10000000) {this.experience = experience;}
        else this.experience = null;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthday);

        if(calendar.get(Calendar.YEAR) >= 2000 && calendar.get(Calendar.YEAR) <= 3000) {
            this.birthday = birthday;
        }
        else
            this.birthday = null;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }
}
