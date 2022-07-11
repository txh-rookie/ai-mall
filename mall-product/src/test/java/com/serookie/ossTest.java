package com.serookie;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/18
 */
public class ossTest {
    public static void main(String[] args) throws FileNotFoundException {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
//// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//        String accessKeyId = "LTAI5tDpjsbnHhqUqoXJAZpS";
//        String accessKeySecret = "GOFw6HQL7R7ymJkOyjalS0Xijkgwtl";
//// 创建OSSClient实例。
//        OSS oss = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
//        InputStream stream=new FileInputStream("");//传入你的参数
//        oss.putObject("serookie/mall","",stream);//第一个参数为你oss的桶名、第二个为文件名、流
//        //关闭流
//        oss.shutdown();

    }
}
