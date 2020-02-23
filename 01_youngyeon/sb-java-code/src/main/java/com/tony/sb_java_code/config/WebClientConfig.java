package com.tony.sb_java_code.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${restClient.readTime:0}")
    private int READ_TIMEOUT;

    @Value("${restClient.connTimeout:0}")
    private int CONN_TIMEOUT;

    @Value("${restClient.connectionMaxTotal:20}")
    private int CONN_MAX_TOTAL;

    @Value("${restClient.defaultMexPerRoute:20}")
    private int CONN_DEFAULT_MAX_PER_ROUTE;

    @Value("${restClient.maxPerRoute:50}")
    private int CONN_MAX_PER_ROUTE;

    @Value("${restClient.url:localhost}")
    private String url = "localhost";

    /**
     * WebClient Connection pool 설정
     * @return
     */
    @Bean
    public WebClient webClient(
    ) {
        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Connection Timeout
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)) // Read Timeout
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))); // Write Timeout

        ClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.from(tcpClient));

        return WebClient.builder()
           .baseUrl("http://localhost:8081")
           .clientConnector(connector).build();
    }

    /**
     * RestTemplate Connection pool 설정
     * @return
     */
    private HttpComponentsClientHttpRequestFactory getHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(CONN_MAX_TOTAL); // 최대 커넥션 수: 서버자원현황, 요청수를 계산해서 설정해야 함.
        cm.setDefaultMaxPerRoute(CONN_DEFAULT_MAX_PER_ROUTE); // 예를 들어 http://localhost:8080/testA, http://localhost:8080/testB 두개의 경로에 대해서 별도로 설정이 없다면 최대 20개의 연결이 생성된다는 뜻이다.
        HttpHost localhost = new HttpHost(url);
        cm.setMaxPerRoute(new HttpRoute(localhost), CONN_MAX_PER_ROUTE); // 최대 연결 개수

        // 타임아웃 설정
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONN_TIMEOUT)
                .setConnectionRequestTimeout(CONN_TIMEOUT)
                .setSocketTimeout(READ_TIMEOUT).build();

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(config).build();
        factory.setHttpClient(client);

        return factory;
    }
}
