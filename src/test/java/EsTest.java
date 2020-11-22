import com.es.EsApplication;
import com.es.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ：hkxia
 * @description：TODO
 * @date ：2020/11/22 00:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {
    @Autowired
    ElasticsearchTemplate template;

    @Test
    public void testCreate() {
    // 创建索引库
        template.createIndex(Item.class); //需要实体类的字节码
    }
}
