package com.hazelcast.hbase;


import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String location;
    private Integer age;
    private Integer balance = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBalance(Integer amount) {
        this.balance = amount;
    }

    public Integer getBalance() {
        return balance;
    }

    public void rechargeMoney(Integer amount) {
        //if(amount >=0)
            this.balance += amount;
    }

    public void transformMoney(Integer amount, User target) {
        if(amount >=0 && this.balance >= amount) {
            this.balance -= amount;
            target.balance += amount;
        }


    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", age=" + age +
                ", balance='" + balance + '\'' +
                '}';
    }

}
