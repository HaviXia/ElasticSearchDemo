package com.es.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * @author ：hkxia
 * @description：TODO
 * @date ：2020/11/21 23:55
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "es", type = "item", shards = 1, replicas = 1)
public class Item {

    @Field(type = FieldType.Long)
    @Id
    Long id;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    String title;
    @Field(type = FieldType.Keyword)
    String category;
    @Field(type = FieldType.Keyword)
    String brand;
    @Field(type = FieldType.Double)
    Double price;
    @Field(type = FieldType.Keyword, index = false)

    String images;
}
