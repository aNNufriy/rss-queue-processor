package rt.testfield.es;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.testfield.rt.config.ApplicationPropertiesProvider;
import ru.testfield.rt.config.Properties;
import ru.testfield.rt.es.ElasticsearchManager;
import ru.testfield.rt.es.ElasticsearchManagerImpl;
import ru.testfield.rt.model.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ElasticsearchManagerTest {

    static ElasticsearchManager esManager;

    static List<String> indexedPosts = new ArrayList<>();

    @BeforeAll
    static public void init() {
        Properties properties = ApplicationPropertiesProvider.getInstance("properties.yml");
        esManager = new ElasticsearchManagerImpl(properties);
    }

    @AfterAll
    public void close() {
        esManager.close();
    }

    @Test
    @Order(3)
    public void createItem() throws IOException {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        indexedPosts.add(esManager.index("postboxx", post));
        indexedPosts.add(esManager.index("postboxx", post));
        boolean postboxx = indexedPosts.add(esManager.index("postboxx", post));
        System.out.println(postboxx);
    }

//    @Test
//    @Order(2)
//    public void queryItems() throws IOException {
//        Collection<Post> postbox = esManager.queryForIds(Post.class, "postbox", "67284e2d-6162-4cc9-b4fe-967476033b6f");
//        int size = new ArrayList<>(postbox).size();
//        System.out.println(size);
//    }

}