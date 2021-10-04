package com.revature.get_tags;

import com.revature.documents.Tag;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked"})
public class GetHandlerTestSuite {

    static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();

    GetHandler sut;
    Context mockContext;
    TagRepo mockTagRepo;
    Tag stubbedTagResponse;


    @BeforeEach
    public void caseSetUp() {
        mockTagRepo = mock(TagRepo.class);
        sut = new GetHandler(mockTagRepo);

        mockContext = mock(Context.class);

        stubbedTagResponse = new Tag();
        stubbedTagResponse.setTagName("java");
        stubbedTagResponse.setTagColor("blue");
    }

    @AfterEach
    public void caseTearDown() {
        sut = null;
        reset(mockContext);
    }

    @Test
    public void given_validRequest_handlerGetsAllBooks() {

        // Arrange
        APIGatewayProxyRequestEvent mockRequestEvent = new APIGatewayProxyRequestEvent();
        mockRequestEvent.withPath("/tags");
        mockRequestEvent.withHttpMethod("GET");
        mockRequestEvent.withBody(null);
        mockRequestEvent.withQueryStringParameters(null);

        List<Tag> mockTags = new ArrayList<>();
        mockTags.add(stubbedTagResponse);
        when(mockTagRepo.getAll()).thenReturn(mockTags);

        APIGatewayProxyResponseEvent expectedResponse = new APIGatewayProxyResponseEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization");
        headers.put("Access-Control-Allow-Origin", "*");
        expectedResponse.setHeaders(headers);
        expectedResponse.setStatusCode(200);
        expectedResponse.setBody(mapper.toJson(mockTags));

        // Act
        APIGatewayProxyResponseEvent actualResponse = sut.handleRequest(mockRequestEvent, mockContext);

        // Assert
        verify(mockTagRepo, times(1)).getAll();
        System.out.println(expectedResponse);
        System.out.println(actualResponse);
        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    public void tagRepoException_returns500StatusCode() {

        // Arrange
        APIGatewayProxyRequestEvent mockRequestEvent = new APIGatewayProxyRequestEvent();
        mockRequestEvent.withPath("/tags");
        mockRequestEvent.withHttpMethod("GET");
        mockRequestEvent.withBody(null);
        mockRequestEvent.withQueryStringParameters(null);

        List<Tag> mockTags = new ArrayList<>();
        mockTags.add(stubbedTagResponse);
        when(mockTagRepo.getAll()).thenThrow(new NullPointerException());

        APIGatewayProxyResponseEvent expectedResponse = new APIGatewayProxyResponseEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization");
        headers.put("Access-Control-Allow-Origin", "*");
        expectedResponse.setHeaders(headers);
        expectedResponse.setStatusCode(500);

        // Act
        APIGatewayProxyResponseEvent actualResponse = sut.handleRequest(mockRequestEvent, mockContext);

        // Assert
        verify(mockTagRepo, times(1)).getAll();
        System.out.println(expectedResponse);
        System.out.println(actualResponse);
        assertEquals(expectedResponse, actualResponse);

    }

}
