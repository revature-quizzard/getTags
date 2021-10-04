package com.revature.get_tags;

import com.revature.documents.Tag;
import org.junit.jupiter.api.*;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings({"rawtypes"})
public class TagRepoTestSuite {


    TagRepo sut;
    DynamoDbTable mockTagTable;
    Tag stubbedTagResponse;


    @BeforeEach
    public void caseSetUp() {
        mockTagTable = mock(DynamoDbTable.class);
        sut = new TagRepo(mockTagTable);

        stubbedTagResponse = new Tag();
        stubbedTagResponse.setTagName("java");
        stubbedTagResponse.setTagColor("blue");
    }

    @AfterEach
    public void caseTearDown() {
        sut = null;
        reset(mockTagTable);
    }


    @Test
    public void test_template() {
        //Arrange
        Iterator stubbedIterator = new Iterator() {

            private int callCount = 0;

            @Override
            public boolean hasNext() {
                return (callCount == 0);
            }

            @Override
            public Object next() {
                callCount++;
                List<Tag> tags = new ArrayList<>();
                tags.add(stubbedTagResponse);
                Page<Tag> page = Page.create(tags);
                return page;
            }
        };


        PageIterable<Tag> mockTags = () -> stubbedIterator;
        when(mockTagTable.scan()).thenReturn(mockTags);

        // Act
        List<Tag> actualResult = sut.getAll();
        assertEquals(1, actualResult.size());

    }
}
