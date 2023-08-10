package com.example.tracking_app;

public class MainModel {
   String name,email,password;
   Integer manager_id;
   MainModel()
   {

   }
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Integer getManager_id() {
      return manager_id;
   }

   public void setManager_id(Integer manager_id) {
      this.manager_id = manager_id;
   }
}
