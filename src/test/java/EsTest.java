
import com.es.pojo.Item;
import com.es.repository.ItemRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.*;
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
    private List<StringTerms.Bucket> buckets;

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

    @Test
    public void TestFindBy() {
        //获取QueryBuilder
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "iphone");
        Iterable<Item> iterable = itemRepository.search(matchQueryBuilder); //    Iterable<T> search(QueryBuilder var1);

        NativeSearchQueryBuilder queryBuilder1 = new NativeSearchQueryBuilder();
        Page<Item> pageResult = itemRepository.search(queryBuilder1.build());

        SearchQuery query = new NativeSearchQueryBuilder().build();
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "xiaomi");
        itemRepository.search(queryBuilder);
    }

    @Test
    public void TestQuery() {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //结果过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "title", "price"}, null));
        //添加查询条件
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title", "iphone"));

        // 排序
        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));

        // 分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(1, 5));
        Page<Item> result = itemRepository.search(nativeSearchQueryBuilder.build());

        int totalPages = result.getTotalPages();
        long total = result.getTotalElements();
        //获取当前页
        List<Item> list = result.getContent();
        for (Item item : list) {
            System.out.println(item);
        }
    }

    @Test
    public void TestAgg() {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        String aggName = "popularBrand";
        //聚合
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(aggName).field("brand"));
        // 查询并返回带聚合的结果
        AggregatedPage<Item> items = template.queryForPage(nativeSearchQueryBuilder.build(), Item.class);

        //解析聚合
        Aggregations aggs = items.getAggregations();
        //获取指定名称的聚合,得到其中一个聚合
        StringTerms agg = aggs.get(aggName);
        //得到buckets
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            System.out.println("key = " + bucket.getKeyAsString());
            System.out.println("docCount = " + bucket.getDocCount());
        }
    }
}
