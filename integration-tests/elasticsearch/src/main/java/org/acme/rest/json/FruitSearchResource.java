package org.acme.rest.json;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import io.vertx.core.json.JsonObject;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.backend.elasticsearch.search.query.dsl.ElasticsearchSearchQueryWhereStep;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.javabean.mapping.SearchMapping;
import org.hibernate.search.mapper.javabean.session.SearchSession;

@Path("/fruits/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitSearchResource {
    @Inject
    SearchMapping mapping;

    @Inject
    RestClient restClient;

    private final Gson gson;

    public FruitSearchResource() {
        gson = new Gson();
        // Workaround for https://github.com/google/gson/issues/764:
        // Concurrent lazy initialization of type adapters can fail... so we initialize them eagerly.
        gson.getAdapter( Fruit.class );
    }

    @PostConstruct
    void initIndex() throws IOException {
        // we setup the index and the aliases if not already done
        // automatic index creation didn't works for the javabean mapper.
        Request listIndices = new Request("GET", "/_all");
        org.elasticsearch.client.Response response = restClient.performRequest(listIndices);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody);
        if(! json.containsKey("fruit")) {
            Request createIndex = new Request("PUT", "/fruit");
            JsonObject body = new JsonObject();
            body.put("aliases", new JsonObject().put("fruit-read", new JsonObject())
                    .put("fruit-write", new JsonObject()));
            createIndex.setJsonEntity(body.toString());
            restClient.performRequest(createIndex);
        }
    }

    @POST
    public Response index(Fruit fruit) {
        if (fruit.id == null) {
            fruit.id = UUID.randomUUID().toString();
        }

        try (SearchSession session = mapping.createSession() ) {
            session.indexingPlan().add(fruit);
        }

        return Response.created(URI.create("/fruits/" + fruit.id)).build();
    }

    @GET
    @Path("/{id}")
    public Fruit get(@PathParam("id") String id) {
        try (SearchSession session = mapping.createSession() ) {
            return initQuery(session)
                    .where( f -> f.id().matching( id) )
                    .fetchSingleHit()
                    .orElseThrow(NotFoundException::new);
        }
    }

    @GET
    @Path("/search")
    public List<Fruit> search(@QueryParam("name") String name, @QueryParam("color") String color) {
        try (SearchSession session = mapping.createSession() ) {
            SearchResult<Fruit> results;
            if (name != null) {
                results = initQuery(session)
                                .where(f -> f.match().field( "name" ).matching( name) )
                                .fetchAll();
            } else if (color != null) {
                results = initQuery(session)
                                .where(f -> f.match().field( "color" ).matching( color) )
                                .fetchAll();
            } else {
                throw new BadRequestException("Should provides name or color query param");
            }
            return results.getHits();
        }
    }

    private ElasticsearchSearchQueryWhereStep<Fruit, ?> initQuery(SearchSession session){
        // This init a query where we load the entity thanks to GSON
        return session.search(Fruit.class)
                .extension( ElasticsearchExtension.get() )
                .select( f -> f.composite(
                        source -> gson.fromJson( source, Fruit.class ),
                        f.source()
                ) );
    }

}
