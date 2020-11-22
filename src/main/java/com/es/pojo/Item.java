package com.es.pojo;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * @author ：hkxia
 * @description：TODO
 * @date ：2020/11/21 23:55
 */

@Data
@Document(indexName = "es", type = "item", shards = 1, replicas = 1)
public class Item {

    @Field(type = FieldType.Long)
    @Id
    Long id;

    @Field(type = FieldType.Text,analyzer = "ik_smart")
    String title;
    @Field(type = FieldType.Keyword)

    String category;
    String brand;
    Double price;
    String images;
}
