package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import util.HttpRequestUtils;

import java.util.Collection;
import java.util.Map;

public class ListUserController extends AbstractController {
   @Override
    public void doGet(HttpRequest request, HttpResponse response){
       if (!isLogin(request.getHeader("Cookie"))) {
           response.sendRedirect("/user/login.html");
           return;
       }
       Collection<User> users = DataBase.findAll();
       StringBuilder sb = new StringBuilder();
       sb.append("<table border='1'>");
       for (User user : users) {
           sb.append("<tr>");
           sb.append("<td>" + user.getUserId() + "</td>");
           sb.append("<td>" + user.getName() + "</td>");
           sb.append("<td>" + user.getEmail() + "</td>");
           sb.append("</tr>");
       }
       response.forwardBody(sb.toString());
    }

    public boolean isLogin(String line){
        String[] headerTokens = line.split(":");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }
}
