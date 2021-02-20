package com.pps.back.frame.pupansheng.core.http.utiil;

import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author
 * @discription;
 * @time 2021/1/21 14:52
 */
@Slf4j
public class PpsHttpUtilExcute {

    private RestTemplate restTemplate;
    private static final String POST="post";
    private static final String GET="get";

    private boolean async;

    private boolean strict;

    private Object[] params;

    private String url;
    /*
      是否在解析url参数的时候添加？号
     */
    private boolean addW;
    /*
     自定义请求头
     */
    private Consumer<HttpHeaders> httpHeadersConsumer;
    /*
     自定义请求参数
     */
    private Consumer<ClientHttpRequest> clientHttpRequestConsumer;
    /*
     自定义错误捕捉
     */
    private Consumer<Throwable> catchCallback;

    /**
     * post请求遇到重定向时的策略 若为空则自己处理 不为空则应该返回一个新的主url
     *
     */
    private BiFunction<ClientHttpResponse,String,String> _3xxStrategy=(r,l)->{

        URI location = r.getHeaders().getLocation();
        String host = location.getHost();
        if(host==null||"".equals(host)){
            throw new RuntimeException("host为空：系统自定义重定向策略失败！请手动设置");
        }
        String path = location.getPath();

        String query = location.getQuery();
        //解析条件
        String s=query;
        if(path!=null||!"".equals(path)) {
            String pa = "";
            String qa = "";
            boolean paT = true;
            List<String> list = new ArrayList<>();

            for (int i = 0; i < s.length(); i++) {
                String sc = s.substring(i, i + 1);
                if (paT) {
                    if ("=".equals(sc)) {
                        list.add(pa);
                        paT = false;
                        pa = "";
                        qa = "";
                    } else {
                        pa += sc;
                    }
                } else {
                    if ("&".equals(sc)) {
                        list.add(qa);
                        paT = true;
                        pa = "";
                        qa = "";
                    } else {
                        qa += sc;
                    }
                }
            }
            list.add(qa);
            Map<Object, Object> param = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                param.put(list.get(i), list.get(++i));
            }
            param.forEach((k, v) -> {
                String env = URLDecoder.decode((String) v);
                param.put(k, env);
            });
            String query2 = UrlParamGenarete.urlParamByMap(param, false);
            String scheme = location.getScheme();
            l=scheme+"://"+host+path+"?"+query2;
        }

        return l;
    };


    private MediaType mediaType=MediaType.APPLICATION_FORM_URLENCODED;

    /**
     * @param asyn
     */
    public PpsHttpUtilExcute(boolean asyn,RestTemplate restTemplate) {
        this.async = async;
        this.restTemplate=restTemplate;
    }

    /**
     *
     * @param params 第一个参数为附加在url上的参数  后面的为在方法体中 类型只能为map或者实体类
     * @return
     */
    public PpsHttpUtilExcute setParam(Object... params) {
        this.params = params;
        return this;
    }

    public PpsHttpUtilExcute setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 当把第一个参数转化为url参数携带后 是否需要在其前面添加？号
     * @param w
     * @return
     */
    public PpsHttpUtilExcute setAddW(boolean w) {
        this.addW = w;
        return this;
    }

    /**
     * 是否严格模式  严格模式：请求不是200响应就会抛出错误
     * @param strict
     * @return
     */
    public PpsHttpUtilExcute setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    /**
     * 设置自定义请求头
     * @param httpHeadersConsumer
     * @return
     */
    public PpsHttpUtilExcute setHttpHeadersConsumer(Consumer<HttpHeaders> httpHeadersConsumer) {
        this.httpHeadersConsumer = httpHeadersConsumer;
        return this;
    }

    /**
     * 设置自定义请求构造
     * @param clientHttpRequestConsumer
     * @return
     */
    public PpsHttpUtilExcute setClientHttpRequestConsumer(Consumer<ClientHttpRequest> clientHttpRequestConsumer) {
        this.clientHttpRequestConsumer = clientHttpRequestConsumer;
        return this;
    }

    /**
     * 设置自定义错误捕捉
     * @param catchCallback
     * @return
     */
    public PpsHttpUtilExcute setCatchCallback(Consumer<Throwable> catchCallback) {
        this.catchCallback = catchCallback;
        return this;
    }

    /**
     * 默认重定向策略处理
     */
    private boolean auto3xxStrategy=true;

    public PpsHttpUtilExcute setAuto3xxStrategy(boolean auto3xxStrategy) {
        this.auto3xxStrategy = auto3xxStrategy;
        return this;
    }

    /**
     * 自定义重定向时的策略
     * @param _3xxStrategy
     */
    public PpsHttpUtilExcute set_3xxStrategy(BiFunction<ClientHttpResponse, String, String> _3xxStrategy) {
        this._3xxStrategy = _3xxStrategy;
        return this;
    }

    private void check() {
        if (url == null) {
            throw new RuntimeException("url 不能为空！");
        }
    }

    public void postJson(Consumer<ClientHttpResponse> callback) {
        mediaType=MediaType.APPLICATION_JSON;
        submit(POST, callback);
    }

    public void postXWFrom(Consumer<ClientHttpResponse> callback) {

        submit(POST, callback);
    }

    public void get(Consumer<ClientHttpResponse> callback) {
        submit(GET, callback);
    }

    /**
     * @param method
     * @param callback
     */
    private void submit(String method, Consumer<ClientHttpResponse> callback) {

        check();


        HttpRequstOperation defaultOperation = HttpStrategyFactory.getDefaultOperation();

        if (catchCallback == null) {
            catchCallback = (e) -> {
                e.printStackTrace();
                throw new RuntimeException(e);
            };
        }
        //todo 解析携带参数
        Map requestParamBody = new HashMap();
        Map requestUrlPram = new HashMap();
        //todo 参数解析
        int[] count = new int[1];
        if(params!=null)
        for (Object ab : params) {
            if (ab instanceof Number || ab instanceof String) {
                throw new RuntimeException("警告：" + ab + "请求参数为基本类型和字符串类型 不符合规范 必须为自定义类和map类型");
            }
            if (ab instanceof Map) {
                if (count[0] == 0) {
                    requestUrlPram.putAll((Map) ab);
                } else {
                    requestParamBody.putAll((Map) ab);
                }

            } else {
                if (ab == null) {
                    count[0]++;
                    continue;
                }
                Class<?> aClass = ab.getClass();
                Field[] declaredFields = aClass.getDeclaredFields();
                Arrays.stream(declaredFields).forEach(f -> {
                    f.setAccessible(true);
                    String name = f.getName();
                    try {
                        Object o = f.get(ab);
                        if (o != null) {
                            if (count[0] == 0) {
                                requestUrlPram.put(name, o);
                            } else {
                                requestParamBody.put(name, o);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("解析请求参数错误：" + e.getMessage());
                    }
                });
                count[0]++;
            }
        }
        if (clientHttpRequestConsumer == null) {
            clientHttpRequestConsumer = (r) -> {
                MediaType contentType = r.getHeaders().getContentType();
                HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
                if (operation != null) {
                    operation.doWithRequest(r, requestParamBody);
                }
            };
        }
        //请求头设置
        HttpHeaders headers = new HttpHeaders();
        if(httpHeadersConsumer==null){
            httpHeadersConsumer=(h)->{
                defaultOperation.defaultHeaderOperation(h);
            };
        }else {
            defaultOperation.defaultHeaderOperation(headers);
        }
        headers.setContentType(mediaType);
        httpHeadersConsumer.accept(headers);

        //todo 准备网络请求
        String httpUrl = url + UrlParamGenarete.urlParamByMap(requestUrlPram, addW);
        org.springframework.http.HttpMethod httpMethod = "post".equals(method) ? HttpMethod.POST : HttpMethod.GET;
        Consumer<ClientHttpRequest> finalClientHttpRequestConsumer = clientHttpRequestConsumer;
        Consumer<Throwable> finalCatchCallback = catchCallback;
        if (async) {

            PpsAsyncUtil.submit(() -> {
                return restTemplate.execute(httpUrl, httpMethod, new RequestCallback() {
                    @Override
                    public void doWithRequest(ClientHttpRequest request) throws IOException {
                        request.getHeaders().addAll(headers);
                        finalClientHttpRequestConsumer.accept(request);
                    }
                }, new ResponseExtractor<Object>() {
                    @Override
                    public Object extractData(ClientHttpResponse response) throws IOException {
                        //TODO 结果解析
                        if (strict&&response.getStatusCode() != HttpStatus.OK) {
                            throw new RuntimeException(httpUrl + ":请求失败！");
                        }
                        if(response.getStatusCode().is3xxRedirection()&&auto3xxStrategy){
                            URI location1 = response.getHeaders().getLocation();
                            if(_3xxStrategy==null) {
                                log.warn("请注意：网址被定向！请自定义处理》》》》》》》》》》》》》》》》》》》》》》》》》:" + location1.toString());
                            }else {
                                AtomicReference<ClientHttpResponse> t = new AtomicReference<>();
                                String newUrl=_3xxStrategy.apply(response,location1.toString());
                                log.warn("请注意：网址被定向！新网址为："+newUrl);
                                PpsHttpUtil.createSyncClient().setUrl(newUrl).get((r)->{
                                    t.set(r);
                                });
                                return  t.get();
                            }
                        }
                        return response;
                    }
                });
            }, (t) -> {
                ClientHttpResponse response = (ClientHttpResponse) t;
                callback.accept(response);
            }, (e) -> {
                finalCatchCallback.accept(e);
            });
        } else {

            ClientHttpResponse response = (ClientHttpResponse) restTemplate.execute(httpUrl, httpMethod, new RequestCallback() {
                @Override
                public void doWithRequest(ClientHttpRequest request) throws IOException {
                    request.getHeaders().addAll(headers);
                    finalClientHttpRequestConsumer.accept(request);
                }
            }, new ResponseExtractor<Object>() {
                @Override
                public Object extractData(ClientHttpResponse response) throws IOException {
                    //TODO 结果解析
                    if (strict&&response.getStatusCode() != HttpStatus.OK) {
                        throw new RuntimeException(httpUrl + ":请求失败！");
                    }
                    if(response.getStatusCode().is3xxRedirection()&&auto3xxStrategy){
                        URI location1 = response.getHeaders().getLocation();
                        if(_3xxStrategy==null) {
                            log.warn("请注意：网址被定向！请自定义处理》》》》》》》》》》》》》》》》》》》》》》》》》:" + location1.toString());
                        }else {
                            AtomicReference<ClientHttpResponse> t = new AtomicReference<>();
                            String newUrl=_3xxStrategy.apply(response,location1.toString());
                            log.warn("请注意：网址被定向！新网址为："+newUrl);
                            PpsHttpUtil.createSyncClient().setUrl(newUrl).get((r)->{
                                t.set(r);
                            });
                            return  t.get();
                        }
                    }
                    return response;
                }
            });
            try {
                callback.accept(response);
            } catch (Exception e) {
                finalCatchCallback.accept(e);
            }
        }
    }

}