package com.revature.get_tags;

import com.revature.documents.Tag;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TagRepo {
    private final DynamoDbTable<Tag> tagTable;

    public TagRepo(){
        DynamoDbClient db = DynamoDbClient.builder().httpClient(ApacheHttpClient.create()).build();
        DynamoDbEnhancedClient dbClient = DynamoDbEnhancedClient.builder().dynamoDbClient(db).build();
        tagTable = dbClient.table("Tags", TableSchema.fromBean(Tag.class));
    }

    public List<Tag> getAll() {
        return tagTable.scan().items().stream().collect(Collectors.toList());
    }

}