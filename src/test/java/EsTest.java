import com.es.EsApplication;
import com.es.pojo.Item;
import com.es.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testCreate() {
        //创建索引库
        template.createIndex(Item.class); //需要实体类的字节码
        //映射关系
        template.putMapping(Item.class);

        //删除索引
        template.delete(Item.class, "price");
        //删除索引库

    }

    //    itemRepository.findById();//查找索引
    //    itemRepository.save();//增加索引和修改索引，id相同即为修改
    //    itemRepository.delete();//删除索引
    @Test
    public void insertIndex() {

        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "Note20", "手机", "三星", 8799.00, "www.samsung.com"));
        list.add(new Item(2L, "iphone", "手机", "苹果", 6799.00, "www.apple.com"));
        list.add(new Item(3L, "huawei mate40pro", "手机", "华为", 7799.00, "www.huawei.com"));
        list.add(new Item(4L, "xiaomi10", "手机", "小米", 5799.00, "www.xiaomi.com"));
        list.add(new Item(5L, "iqoo2", "手机", "oppo", 7799.00, "www.oppo.com"));
        //接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    @Test
    public void find() {
        Iterable<Item> all = itemRepository.findAll();
        for (Item item : all) {
            System.out.println("item = " + item);
        }
    }

    //复杂查询
    @Test
    public void testFindBy() {
        List<Item> list = itemRepository.findByPriceBetween(2000d, 4000d);
        for (Item item : list) {
            System.out.println("item = " + item);
        }

    }
}
