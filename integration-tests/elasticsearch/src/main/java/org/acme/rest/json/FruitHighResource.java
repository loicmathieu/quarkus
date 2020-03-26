package org.acme.rest.json;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import io.vertx.core.json.JsonObject;

@Path("/fruits/high")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitHighResource {
    @Inject
    RestHighLevelClient restClient;

    @POST
    public Response index(Fruit fruit) throws IOException {
        if (fruit.id == null) {
            fruit.id = UUID.randomUUID().toString();
        }
        IndexRequest request = new IndexRequest("fruits");
        request.id(fruit.id);
        request.source(JsonObject.mapFrom(fruit).toString(), XContentType.JSON);

        restClient.index(request, RequestOptions.DEFAULT);

        return Response.created(URI.create("/fruits/" + fruit.id)).build();
    }

    @GET
    @Path("/{id}")
    public Fruit get(@PathParam("id") String id) throws IOException {
        GetRequest request = new GetRequest("fruits", id);
        GetResponse getResponse = restClient.get(request, RequestOptions.DEFAULT);
        String sourceAsString = getResponse.getSourceAsString();
        JsonObject json = new JsonObject(sourceAsString);
        return json.mapTo(Fruit.class);
    }

    @GET
    @Path("/search")
    public List<Fruit> search(@QueryParam("name") String name, @QueryParam("color") String color) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("fruits");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (name != null) {
            searchSourceBuilder.query(QueryBuilders.matchQuery("name", name));
        } else if (color != null) {
            searchSourceBuilder.query(QueryBuilders.matchQuery("color", color));
        } else {
            throw new BadRequestException("Should provides name of color query param");
        }
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
        List<Fruit> results = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {
            JsonObject json = new JsonObject(hit.getSourceAsString());
            results.add(json.mapTo(Fruit.class));
        }
        return results;
    }

}
