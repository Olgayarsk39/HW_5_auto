package org.max.home.accu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetWeatherTenDayTest extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(GetLocationTest.class);

    @Test
    void get_shouldReturn200() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 200 запущен");
        //given
        logger.debug("Формирование мока для GET /forecasts/v1/daily/10day/290396");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/10day/290396"))
                .withQueryParam("apiKey", containing("82c9229354f849e78efe010d94150807"))
                .willReturn(aResponse()
                        .withStatus(200).withBody("Жара")));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        //when
        HttpGet request = new HttpGet(getBaseUrl()+"/forecasts/v1/daily/10day/290396");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", "82c9229354f849e78efe010d94150807")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        HttpResponse response = httpClient.execute(request);

        //then
        verify(getRequestedFor(urlPathEqualTo("/forecasts/v1/daily/10day/290396")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Жара", convertResponseToString(response));
    }

    @Test
    void get_shouldReturn401() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 401 запущен");
        //given
        logger.debug("Формирование мока для GET /forecasts/v1/daily/10day/290396");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/10day/290396"))
                .withQueryParam("apiKey",  notMatching("82c9229354f849e78efe010d94150807"))
                .willReturn(aResponse()
                        .withStatus(401).withBody("Error")));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        //when
        HttpGet request = new HttpGet(getBaseUrl()+"/forecasts/v1/daily/10day/290396");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", "Not_82c9229354f849e78efe010d94150807")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        HttpResponse response = httpClient.execute(request);

        //then
        verify(getRequestedFor(urlPathEqualTo("/forecasts/v1/daily/10day/290396")));
        assertEquals(401, response.getStatusLine().getStatusCode());
        assertEquals("Error", convertResponseToString(response));

    }


}
