// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.*;
import com.google.appengine.api.datastore.*;
import java.util.*;
import com.google.sps.data.*;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    private ArrayList<String> messages = new ArrayList<>();


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //Query
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Comment");

    PreparedQuery results = datastore.prepare(query);
    ArrayList<Comment> comments = new ArrayList<Comment>();
    for (Entity entity : results.asIterable()) {
        long id = entity.getKey().getId();
        String comment = (String) entity.getProperty("text");
        Comment msg = new Comment(id, comment);
        comments.add(msg);
    }

    // Send the JSON as the response
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
     //Datastore
    String commentText = request.getParameter("comment-input");

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", commentText);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}	

