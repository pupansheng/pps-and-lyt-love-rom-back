package com.pps.back.frame.pupansheng.custom.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @discription;
 * @time 2020/12/24 12:45
 */

public class FileCosUtil {

    @Autowired
    Environment environment;

    COSClient cosClient;

    Region region;

    String secretId;

    String secretKey;

    COSCredentials cred;

    ClientConfig clientConfig;

    Bucket defalutBucketElement;

    String appId;


    @PostConstruct
    public void t1(){

        // 1 初始化用户身份信息（secretId, secretKey）。
        secretId = environment.getProperty("SecretId");
        secretKey = environment.getProperty("SecretKey");
        appId=environment.getProperty("appid");
        cred = new BasicCOSCredentials(secretId, secretKey);
        region = new Region("ap-chengdu");
        clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);

        //获得默认桶
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket bucketElement : buckets) {
            defalutBucketElement=bucketElement;
            break;
        }
    }

    /**
     * 创建桶
     * @param
     * @return
     */
    public String CreateBuckt(String bucket2){
        String bucket = bucket2+"-"+appId; //存储桶名称，格式：BucketName-APPID
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
        // 设置 bucket 的权限为 Private(私有读写), 其他可选有公有读私有写, 公有读写
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicReadWrite);
        try{
            Bucket bucketResult = cosClient.createBucket(createBucketRequest);
            return    bucketResult.getName();
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
        throw  new RuntimeException("创建失败！");
    }

    public String getUrl(String key){
        if(defalutBucketElement==null){
            throw  new RuntimeException("未获得默认的桶");
        }
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(defalutBucketElement.getName(), key, HttpMethodName.GET);
        URL url = cosClient.generatePresignedUrl(req);
        return url.toString();

    }
    public String getUrl( String bucketName, String key){

        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        URL url = cosClient.generatePresignedUrl(req);
        return url.toString();

    }

    public List<String> getUrl( String bucketName, List<String> keys){
         List<String> urls=new ArrayList<>();
         keys.stream().forEach(u->{
             GeneratePresignedUrlRequest req =
                     new GeneratePresignedUrlRequest(bucketName, u, HttpMethodName.GET);
             URL url = cosClient.generatePresignedUrl(req);
             urls.add(url.toString());
         });

        return urls;

    }
    /**
     * 查询正在分块上传的块
     */
    public  MultipartUploadListing searchChunck(){
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = defalutBucketElement.getName();
        ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(bucketName);
        listMultipartUploadsRequest.setDelimiter("/");
        listMultipartUploadsRequest.setMaxUploads(100);
        listMultipartUploadsRequest.setPrefix("");
        listMultipartUploadsRequest.setEncodingType("url");
        MultipartUploadListing multipartUploadListing = cosClient.listMultipartUploads(listMultipartUploadsRequest);
        return  multipartUploadListing;

    }

    public  List<PartETag> searchChunckComplete(String key,String uploadId){
        // ListPart 用于在 complete 分块上传前或者 abort 分块上传前获取 uploadId 对应的已上传的分块信息, 可以用来构造 partEtags
        List<PartETag> partETags = new ArrayList<PartETag>();
        String bucketName =defalutBucketElement.getName();
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, key, uploadId);
        PartListing partListing = null;
        do {
            partListing = cosClient.listParts(listPartsRequest);
            for (PartSummary partSummary : partListing.getParts()) {
                partETags.add(new PartETag(partSummary.getPartNumber(), partSummary.getETag()));
            }
            listPartsRequest.setPartNumberMarker(partListing.getNextPartNumberMarker());
        } while (partListing.isTruncated());

        return  partETags;
    }


    public String initUploadChunck(String key){

        // Bucket的命名格式为 BucketName-APPID
        String bucketName=defalutBucketElement.getName();
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult initResponse = cosClient.initiateMultipartUpload(initRequest);
        String  uploadId = initResponse.getUploadId();
        return  uploadId;
    }



    public UploadPartResult uploadFileChunck(String  uploadId,Integer partNumber,String key,int size,InputStream inputStream){
        // 上传分块, 最多10000个分块, 分块大小支持为1M - 5G。
        // 分块大小设置为4M。如果总计 n 个分块, 则 1 ~ n-1 的分块大小一致，最后一块小于等于前面的分块大小。
        // partStream 代表 part 数据的输入流, 流长度为 partSize
        UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(defalutBucketElement.getName()).
                withUploadId(uploadId).withKey(key).withPartNumber(partNumber).withPartSize(size).
                withInputStream(inputStream);
        UploadPartResult uploadPartResult = cosClient.uploadPart(uploadRequest);
        return  uploadPartResult;

    }


    public CompleteMultipartUploadResult compeleteChunckUpload(String key,String uploadId,List<PartETag> partetag){
        // complete 完成分块上传.
        String bucketName=defalutBucketElement.getName();
        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId,partetag);
        CompleteMultipartUploadResult result = cosClient.completeMultipartUpload(compRequest);
        return  result;
    }



    public void  cancerUpload(String key,String uploadId){

        String bucketName = defalutBucketElement.getName();
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucketName, key, uploadId);
        cosClient.abortMultipartUpload(abortMultipartUploadRequest);

    }


}
