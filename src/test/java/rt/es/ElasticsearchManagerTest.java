package rt.es;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.testfield.rt.config.ApplicationPropertiesProvider;
import ru.testfield.rt.config.Properties;
import ru.testfield.rt.es.ElasticsearchManager;
import ru.testfield.rt.es.ElasticsearchManagerImpl;
import ru.testfield.rt.model.Post;

import java.io.IOException;
import java.util.UUID;

public class ElasticsearchManagerTest {

    ElasticsearchManager esManager;

    @Before
    public void init() {
        Properties properties = ApplicationPropertiesProvider.getInstance("properties.yml");
        esManager = new ElasticsearchManagerImpl(properties);
    }

    @After
    public void close() {
        esManager.close();
    }

    @Test
    public void createItem() throws IOException {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        System.out.println(esManager.index("postboxx", post));
        System.out.println(esManager.index("postboxx", post));
        System.out.println(esManager.index("postboxx", post));
    }

}