package com.pps.back.frame.pupansheng.custom.pachong;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

/**
 * @author
 * @discription;
 * @time 2021/1/25 15:27
 */
public class SeleiumTest {


    public static void main(String args[]) throws InterruptedException {


        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);

        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"D:\\dev_softwares\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        //创建无界面浏览器对象
        String t1="https://tx.79da.com/m3u8.php?url=https://meiju3.qfxmj.com/20181209/EhsjhGQ4/index.m3u8";
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get(t1);
        String pageSource = driver.getPageSource();

        WebElement video = driver.findElementByTagName("video");
        String text = video.getText();
        System.out.println(text);


    }

}
