package com.pps.back.frame.pupansheng.core.mvc;


import com.pps.back.frame.pupansheng.core.mvc.annotion.CanOtherUrlRequest;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.MatchableHandlerMapping;
import org.springframework.web.servlet.handler.RequestMatchResult;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author
 * @discription;
 * @time 2020/12/1 9:30
 */
@Component
public class PpsRequestMapingHandlerMapping<T> extends RequestMappingInfoHandlerMapping implements MatchableHandlerMapping, EmbeddedValueResolverAware,PriorityOrdered{
    private boolean useSuffixPatternMatch = true;
    private boolean useRegisteredSuffixPatternMatch = false;
    private boolean useTrailingSlashMatch = true;
    private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
    @Nullable
    private StringValueResolver embeddedValueResolver;
    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

    public PpsRequestMapingHandlerMapping() {
    }

    public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
        this.useSuffixPatternMatch = useSuffixPatternMatch;
    }

    public void setUseRegisteredSuffixPatternMatch(boolean useRegisteredSuffixPatternMatch) {
        this.useRegisteredSuffixPatternMatch = useRegisteredSuffixPatternMatch;
        this.useSuffixPatternMatch = useRegisteredSuffixPatternMatch || this.useSuffixPatternMatch;
    }

    public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
        this.useTrailingSlashMatch = useTrailingSlashMatch;
    }

    public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        Assert.notNull(contentNegotiationManager, "ContentNegotiationManager must not be null");
        this.contentNegotiationManager = contentNegotiationManager;
    }

    public ContentNegotiationManager getContentNegotiationManager() {
        return this.contentNegotiationManager;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    @Override
    public void afterPropertiesSet() {
        this.config = new RequestMappingInfo.BuilderConfiguration();
        this.config.setUrlPathHelper(this.getUrlPathHelper());
        this.config.setPathMatcher(this.getPathMatcher());
        this.config.setSuffixPatternMatch(this.useSuffixPatternMatch);
        this.config.setTrailingSlashMatch(this.useTrailingSlashMatch);
        this.config.setRegisteredSuffixPatternMatch(this.useRegisteredSuffixPatternMatch);
        this.config.setContentNegotiationManager(this.getContentNegotiationManager());
        super.afterPropertiesSet();
    }

    public boolean useSuffixPatternMatch() {
        return this.useSuffixPatternMatch;
    }

    public boolean useRegisteredSuffixPatternMatch() {
        return this.useRegisteredSuffixPatternMatch;
    }

    public boolean useTrailingSlashMatch() {
        return this.useTrailingSlashMatch;
    }

    @Nullable
    public List<String> getFileExtensions() {
        return this.config.getFileExtensions();
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) || AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class);
    }

    @Override
    @Nullable
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        if(!method.isAnnotationPresent(CanOtherUrlRequest.class)){
            return  null;
        }
        RequestMappingInfo info = this.createRequestMappingInfo(method);
        return info;
    }

    @Nullable
    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = (RequestMapping)AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        RequestCondition<?> condition = element instanceof Class ? this.getCustomTypeCondition((Class)element) : this.getCustomMethodCondition((Method)element);
        return requestMapping != null ? this.createRequestMappingInfo(requestMapping, (Method)element) : null;
    }

    @Nullable
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return null;
    }

    @Nullable
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return null;
    }

    protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, @Nullable RequestCondition<?> customCondition) {

        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(this.resolveEmbeddedValuesInPatterns(requestMapping.path())).methods(requestMapping.method()).params(requestMapping.params()).headers(requestMapping.headers()).consumes(requestMapping.consumes()).produces(requestMapping.produces()).mappingName(requestMapping.name());
        if (customCondition != null) {
            builder.customCondition(customCondition);
        }
        return builder.options(this.config).build();
    }
    protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, Method method) {
        CanOtherUrlRequest annotation = method.getAnnotation(CanOtherUrlRequest.class);
        String[] value = annotation.value();
        String[] path = requestMapping.path();
        List<String> newPath=new ArrayList<>();
        for (int i = 0; i <path.length ; i++) {
            String suffix=path[i];
            for (int j = 0; j < value.length; j++) {
             String prefix=value[j];
             if(prefix.endsWith("/")&&suffix.startsWith("/")){
                 newPath.add(prefix+suffix.substring(1,suffix.length()));
             }else {
                 newPath.add(prefix+suffix);
             }

            }
        }
        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(this.resolveEmbeddedValuesInPatterns(newPath.toArray(new String[0]))).methods(requestMapping.method()).params(requestMapping.params()).headers(requestMapping.headers()).consumes(requestMapping.consumes()).produces(requestMapping.produces()).mappingName(requestMapping.name());
        return builder.options(this.config).build();
    }
    protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
        if (this.embeddedValueResolver == null) {
            return patterns;
        } else {
            String[] resolvedPatterns = new String[patterns.length];

            for(int i = 0; i < patterns.length; ++i) {
                resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
            }

            return resolvedPatterns;
        }
    }

    @Override
    public RequestMatchResult match(HttpServletRequest request, String pattern) {
        RequestMappingInfo info = RequestMappingInfo.paths(new String[]{pattern}).options(this.config).build();
        RequestMappingInfo matchingInfo = info.getMatchingCondition(request);
        if (matchingInfo == null) {
            return null;
        } else {
            Set<String> patterns = matchingInfo.getPatternsCondition().getPatterns();
            String lookupPath = this.getUrlPathHelper().getLookupPathForRequest(request);
            return new RequestMatchResult((String)patterns.iterator().next(), lookupPath, this.getPathMatcher());
        }
    }

    protected CorsConfiguration initCorsConfiguration(Object handler, Method method, RequestMappingInfo mappingInfo) {
        HandlerMethod handlerMethod = this.createHandlerMethod(handler, method);
        Class<?> beanType = handlerMethod.getBeanType();
        CrossOrigin typeAnnotation = (CrossOrigin)AnnotatedElementUtils.findMergedAnnotation(beanType, CrossOrigin.class);
        CrossOrigin methodAnnotation = (CrossOrigin)AnnotatedElementUtils.findMergedAnnotation(method, CrossOrigin.class);
        if (typeAnnotation == null && methodAnnotation == null) {
            return null;
        } else {
            CorsConfiguration config = new CorsConfiguration();
            this.updateCorsConfig(config, typeAnnotation);
            this.updateCorsConfig(config, methodAnnotation);
            if (CollectionUtils.isEmpty(config.getAllowedMethods())) {
                Iterator var9 = mappingInfo.getMethodsCondition().getMethods().iterator();

                while(var9.hasNext()) {
                    RequestMethod allowedMethod = (RequestMethod)var9.next();
                    config.addAllowedMethod(allowedMethod.name());
                }
            }

            return config.applyPermitDefaultValues();
        }
    }

    private void updateCorsConfig(CorsConfiguration config, @Nullable CrossOrigin annotation) {
        if (annotation != null) {
            String[] var3 = annotation.origins();
            int var4 = var3.length;

            int var5;
            String header;
            for(var5 = 0; var5 < var4; ++var5) {
                header = var3[var5];
                config.addAllowedOrigin(this.resolveCorsAnnotationValue(header));
            }

            RequestMethod[] var7 = annotation.methods();
            var4 = var7.length;

            for(var5 = 0; var5 < var4; ++var5) {
                RequestMethod method = var7[var5];
                config.addAllowedMethod(method.name());
            }

            var3 = annotation.allowedHeaders();
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                header = var3[var5];
                config.addAllowedHeader(this.resolveCorsAnnotationValue(header));
            }

            var3 = annotation.exposedHeaders();
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                header = var3[var5];
                config.addExposedHeader(this.resolveCorsAnnotationValue(header));
            }

            String allowCredentials = this.resolveCorsAnnotationValue(annotation.allowCredentials());
            if ("true".equalsIgnoreCase(allowCredentials)) {
                config.setAllowCredentials(true);
            } else if ("false".equalsIgnoreCase(allowCredentials)) {
                config.setAllowCredentials(false);
            } else if (!allowCredentials.isEmpty()) {
                throw new IllegalStateException("@CrossOrigin's allowCredentials value must be \"true\", \"false\", or an empty string (\"\"): current value is [" + allowCredentials + "]");
            }

            if (annotation.maxAge() >= 0L && config.getMaxAge() == null) {
                config.setMaxAge(annotation.maxAge());
            }

        }
    }

    private String resolveCorsAnnotationValue(String value) {
        if (this.embeddedValueResolver != null) {
            String resolved = this.embeddedValueResolver.resolveStringValue(value);
            return resolved != null ? resolved : "";
        } else {
            return value;
        }
    }



}
