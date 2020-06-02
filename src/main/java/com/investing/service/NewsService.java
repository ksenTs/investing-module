package com.investing.service;

import com.investing.model.IssData;
import com.investing.model.IssResponseDto;
import com.investing.model.IssResultDto;
import com.investing.model.NewsDetails;
import com.investing.model.SiteNews;
import com.investing.rest.IssHttpRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.investing.rest.Deserializer.deserializeData;

@Service
public class NewsService {

    private IssHttpRestClient httpRestClient;

    @Autowired
    public void setHttpRestClient(IssHttpRestClient httpRestClient) {
        this.httpRestClient = httpRestClient;
    }

    public List<SiteNews> getNews() {
        try {
            IssResponseDto responseDto = httpRestClient.get("/sitenews.json");
            IssResultDto<IssData> data = deserializeData(responseDto);
            return data.getSitenews().getData().stream()
                    .map(row -> new SiteNews(
                            row.get(0),
                            row.get(3),
                            row.get(2)
                    ))
                    .collect(Collectors.toList());

        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public NewsDetails getNewsDetails(String code) {
        return new NewsDetails();
    }
}
