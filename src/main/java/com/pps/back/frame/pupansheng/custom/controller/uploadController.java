package com.pps.back.frame.pupansheng.custom.controller;

import com.pps.back.frame.pupansheng.custom.config.FileCosUtil;
import com.pps.back.frame.pupansheng.custom.db.GlobalDb;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URL;

@RestController
public class uploadController {


    @Autowired
    FileCosUtil fileCosUtil;
    @Autowired
    GlobalDb globalDb;

    @PostConstruct
    public void f1(){


    }

    public String getUrl( String bucketName, String key){

        Region region = new Region("ap-chengdu");
        // 生成匿名的请求签名，需要重新初始化一个匿名的 cosClient
        // 初始化用户身份信息, 匿名身份不用传入 SecretId、SecretKey 等密钥信息
        COSCredentials cred = new AnonymousCOSCredentials();
        // 设置 bucket 的区域，COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(region);
       // 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        // bucket 名需包含 appid
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        URL url = cosClient.generatePresignedUrl(req);


        cosClient.shutdown();

        return url.toString();


    }


}
