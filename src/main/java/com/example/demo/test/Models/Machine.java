package com.example.demo.test.Models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Id;

@Getter
@Setter

@Entity

public class Machine {

    @NonNull
    String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machineId", nullable = false)
    private Long machineId;

    //@NonNull
    //String ip;
    // @NonNull
    //String location;

    @ManyToMany
    @JoinTable(
            name = "MachineAndTask",
            joinColumns = @JoinColumn(name = "machineId"),
            inverseJoinColumns = @JoinColumn(name = "taskId"))
    private Set<Task> allTasks;

    @ManyToMany(mappedBy = "machineByUser")
    private Set<User> userList = new HashSet<User>();

    public Machine() {
    }

    public Machine(@NonNull String name, @NonNull String ip, @NonNull String location) {

        this.name = name;
       //  this.ip = ip;
       //  location = location;
        allTasks = new HashSet<Task>();

    }

    public Set<User> getUserList() {

        return userList;
    }



}
