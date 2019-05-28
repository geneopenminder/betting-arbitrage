package com.isfootball.pool;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.internal.HttpClientFactory;
import java.lang.reflect.Field;
import org.apache.http.impl.client.CloseableHttpClient;

public class HttpParamsSetter {

    @SuppressWarnings("deprecation")
    public static void setSoTimeout(int soTimeout) {
        HttpClientFactory factory = getStaticValue(HttpCommandExecutor.class, "httpClientFactory");
        if (factory == null) {
            factory = new HttpClientFactory();
        }

        CloseableHttpClient httpClient = (DefaultHttpClient) factory.getHttpClient();

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setStaleConnectionCheckEnabled(true)
                .build();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setSoTimeout(params, soTimeout);
        //httpClient.
        //httpClient.setParams(params);

        //setStaticValue(HttpCommandExecutor.class, "httpClientFactory", factory);
    }

    private static <T> T getStaticValue(Class<?> aClass, String fieldName) {
        Field field = null;
        Boolean isAccessible = null;
        try {
            field = aClass.getDeclaredField(fieldName);
            isAccessible = field.isAccessible();
            field.setAccessible(true);

            return (T) field.get(null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (field != null && isAccessible != null) {
                field.setAccessible(isAccessible);
            }
        }
    }

    private static void setStaticValue(Class<HttpCommandExecutor> aClass, String fieldName, Object value) {
        Field field = null;
        Boolean isAccessible = null;
        try {
            field = aClass.getDeclaredField(fieldName);
            isAccessible = field.isAccessible();
            field.setAccessible(true);

            field.set(null, value);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (field != null && isAccessible != null) {
                field.setAccessible(isAccessible);
            }
        }
    }
}