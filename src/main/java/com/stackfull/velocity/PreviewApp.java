// © Copyright 2015 Paul Thomas <paul@stackfull.com>. All Rights Reserved.
//
package com.stackfull.velocity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;


@EnableAutoConfiguration
@RestController
public class PreviewApp {

  private static final String LOG_TAG = "api";

  @Autowired VelocityEngine velocityEngine;
  @Autowired ObjectMapper objectMapper;

  @RequestMapping(value = "/api/v1/convert", method = RequestMethod.POST)
  public String convert(@RequestParam("data") String data,
                        @RequestParam("template") String template) {

    TypeReference<HashMap<String,Object>> typeRef
        = new TypeReference<HashMap<String,Object>>() {};
    HashMap<String,Object> o = null;
    try {
      o = objectMapper.readValue(data, typeRef);
    } catch (IOException e) {
      e.printStackTrace();
    }
    VelocityContext ctx = new VelocityContext(o);
    StringWriter writer = new StringWriter();
    velocityEngine.evaluate(ctx, writer, LOG_TAG, template);
    return writer.toString();
  }

  public static void main(String[] args) {
    SpringApplication.run(PreviewApp.class, args);
  }
}
