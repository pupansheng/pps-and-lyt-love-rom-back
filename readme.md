# H5电影同步观看平台 （自己写的 已开源）
+ 说明
  + 此项目由本人业余开发 由于本人与女朋友异地 有时候想要一起看电影 但是苦于没有很棒的平台  很多可以提供同步观看的平台影片资源太少 也很不清晰 所以我决心自己写一个这样的平台 
  + 此平台电影资源是采用java爬虫爬取网上资源的 大部分电影都可以找到 但是请仅限自己观看 请勿传播！
+ 涉及技术  
  + 前端
    采用uni-app开发，和我自己写的一个配合自己写的后端websocket框架的前端sdk。
  + 后端
    我使用netty开发了一个im聊天框架并做成了springboot启动器，配合我自己写的前端sdk,可以很方便集成在项目中集成websocket,项目中有些启动器都是我自己写的所以网上无法直接导入，需要自己导入maven仓管  
    爬取资源使用的工具类是我魔改restTemplate创建的
+ 注意事项
  由于爬取的电影资源一般都是m3u8格式的所以，移动端才可以播放
+ 截图
+ 部署步骤
  1. 首先需要把lib下面的jar包导入自己的maven仓库
  2. 运行sql脚本文件
  3. 更改配置文件 
  4. 运行程序
  5. 运行font下的前端程序（h5方式） 前端程序为uni-app框架写的 怎么运行 请百度 当然前端文件也需要重新写上自己后端地址具体是在commom文件夹下的http.interceptor.js文件和根目录下的main.js 
  6. 以上完毕就可以搭建好自己的电影同步观看平台啦 和你的那个她看电影吧
  7. 用手机夸克浏览器效果最好
  
  
    
      
    
    
  
