package com.mood.test;


import mood.core.CoreServer;

public class test {
    public static void main(String args[]){
        CoreServer server = new CoreServer(2006);
        server.setControllerBasePackage("mood.example.*");
        try{
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
