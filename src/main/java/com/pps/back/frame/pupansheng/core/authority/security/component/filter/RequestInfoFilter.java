package com.pps.back.frame.pupansheng.core.authority.security.component.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author
 * @discription;
 * @time 2020/5/14 14:59
 */
/*
  一定经过的过滤器 请求信息描述
 */
@Slf4j
public class RequestInfoFilter extends OncePerRequestFilter {


    class RepeatedlyReadRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        public RepeatedlyReadRequestWrapper(HttpServletRequest request)
                throws IOException {
            super(request);
            body = readBytes(request.getReader(), "UTF-8");
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() {

             final ByteArrayInputStream bais = new ByteArrayInputStream(body);

            return new ServletInputStream() {

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {

                }

                @Override
                public int read() {
                    return bais.read();
                }
            };
        }

        /**
         * 通过BufferedReader和字符编码集转换成byte数组
         * @param br
         * @param encoding
         * @return
         * @throws IOException
         */
        private byte[] readBytes(BufferedReader br,String encoding) throws IOException{
            String str = null,retStr="";
            while ((str = br.readLine()) != null) {
                retStr += str;
            }
            if (!StringUtils.isEmpty(retStr)) {
                return retStr.getBytes(Charset.forName(encoding));
            }
            return retStr.getBytes(Charset.forName(encoding));
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


      /*  HttpServletRequest requestWrapper = new RepeatedlyReadRequestWrapper(req);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // Get request URL.
            map.put("URL", requestWrapper.getRequestURL());
            map.put("Method", requestWrapper.getMethod());
            map.put("Protocol",requestWrapper.getProtocol());
            map.put("sessionId",requestWrapper.getSession()!=null?requestWrapper.getSession().getId():"无session");
            // 获取header信息
            logger.info("收到网络请求：------------------------------------------------------------------------------------------------------------------------------------------------");
            log.info("http信息：-------------------------------");
            map.forEach((k,v)->{
                log.info("{}:{}",k,v);
            });
            log.info("头信息：-------------------------------");
            List<Map<String,String>> headerList = new ArrayList<>();
            Map<String,String> headerMaps = new HashMap<String,String>();
            for(Enumeration<String> enu = requestWrapper.getHeaderNames(); enu.hasMoreElements();){
                String name = enu.nextElement();
                headerMaps.put(name,req.getHeader(name));
            }
            headerList.add(headerMaps);
          //  map.put("headers", headerList);
            headerList.forEach(p->{

                p.forEach((k,v)->{

                    log.info("{}:{}",k,v);

                });


            });
            //获取parameters信息
            log.info("参数信息：-------------------------------");
            List<Map<String,String>> parameterList = new ArrayList<>();
            Map<String,String> parameterMaps = new HashMap<String,String>();
            for(Enumeration<String> names = requestWrapper.getParameterNames();names.hasMoreElements();){
                String name = names.nextElement();

                String[] paramValues = requestWrapper.getParameterValues(name);
                if (paramValues.length >0) {
                    String paramValue = paramValues[0];
                    if (paramValue.length() != 0) {
                        parameterMaps.put(name, paramValue);
                    }
                }

            }
            parameterList.add(parameterMaps);
           // map.put("parameters", parameterList);
            parameterList.forEach(p->{

                p.forEach((k,v)->{

                    log.info("{}:{}",k,v);

                });


            });
           log.info("--------------------------------------------------------------------------------------------------");






        } catch (Exception ex) {
            ex.printStackTrace();
        }


*/
        logger.info("--------------------------------------------------------------------------------------------------------------------------------------------");
            filterChain.doFilter(req,response);


    }




}

