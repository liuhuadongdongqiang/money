package com.cn.mvc.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

//@RestController
@Controller
public class test_Request_controller {

    @RequestMapping("/test_requestbody")
    public String test_requestbody(@RequestBody String requestbody){
        System.out.println("requestbody:"+requestbody);
        return "success";
    }

    @RequestMapping("/test_requestEntity")
    public String test_requestEntity(RequestEntity<String> requestEntity){
        System.out.println(requestEntity.getHeaders());
        System.out.println(requestEntity.getBody());
        return "success";

    }

    @RequestMapping("/test_response")
    public void  test_response(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.getWriter().println("hello,response");
    }


    @RequestMapping("/testAjax")
    @ResponseBody
    public String testAjax(String username,String password){
        System.out.println("username:"+username+",password:"+password);
        return "hello,ajax";
    }

    @RequestMapping("/test_down")
    public ResponseEntity<byte[]> test_down(HttpSession session) throws IOException {

//        获取servlercontext对象
        ServletContext servletContext = session.getServletContext();
//        获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/static/img/1.jpg");
//        创建输入流
        FileInputStream fileInputStream = new FileInputStream(realPath);
//        创建字节数组
        byte[] bytes = new byte[fileInputStream.available()];
//        将流读到字节数组中
        fileInputStream.read(bytes);
//        创建httpheader对象设置响应头信息
        MultiValueMap<String,String>  headers= new HttpHeaders();
//        设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition","attachment;filename=1.jpg");
//        设置响应状态码
        HttpStatus httpStatus = HttpStatus.OK;
//        创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, httpStatus);
        fileInputStream.close();
        return responseEntity;

    }

    @RequestMapping("test_up")
    public String test_up(MultipartFile photo,HttpSession session) throws IOException {

//        获取文件名称呼
        String originalFilename = photo.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String name = uuid+substring;
//        找到存放路径
        ServletContext servletContext = session.getServletContext();
        String photopath = servletContext.getRealPath("photo");
        //如果不存在这个路径就创建
        File file = new File(photopath);
        if (!file.exists()){
            file.mkdir();
        }
//        确定最终路径
        String finalPath = photopath+File.separator+name;
        photo.transferTo(new File(finalPath));

//        返回视图
        return "success";
    }


    @RequestMapping("/test_error")
    public String test_error(){
        System.out.println(1/0);
        return "success";

    }

}
