package io.smallrye.openapi.runtime.scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalLong;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.jandex.IndexWriter;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.smallrye.openapi.api.OpenApiConfig;
import test.io.smallrye.openapi.runtime.scanner.Widget;
import test.io.smallrye.openapi.runtime.scanner.jakarta.MultipleContentTypesWithFormParamsTestResource;

/**
 * @author Michael Edgar {@literal <michael@xlate.io>}
 */
class ParameterScanTests extends IndexScannerTestBase {

    private static void test(String expectedResource, Class<?>... classes) throws IOException, JSONException {
        Index index = indexOf(classes);
        OpenApiAnnotationScanner scanner = new OpenApiAnnotationScanner(emptyConfig(), index);
        OpenAPI result = scanner.scan();
        printToConsole(result);
        assertJsonEquals(expectedResource, result);
        verifyMethodAndParamRefsPresent(result);
    }

    private static void test(OpenApiConfig config, String expectedResource, Class<?>... classes)
            throws IOException, JSONException {
        Index index = indexOf(classes);
        OpenApiAnnotationScanner scanner = new OpenApiAnnotationScanner(config, index);
        OpenAPI result = scanner.scan();
        printToConsole(result);
        assertJsonEquals(expectedResource, result);
        verifyMethodAndParamRefsPresent(result);
    }

    @Test
    void testJavaxIgnoredMpOpenApiHeaders() throws IOException, JSONException {
        test("params.ignored-mp-openapi-headers.json",
                test.io.smallrye.openapi.runtime.scanner.javax.IgnoredMpOpenApiHeaderArgsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaIgnoredMpOpenApiHeaders() throws IOException, JSONException {
        test("params.ignored-mp-openapi-headers.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.IgnoredMpOpenApiHeaderArgsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxParameterOnMethod() throws IOException, JSONException {
        test("params.parameter-on-method.json",
                test.io.smallrye.openapi.runtime.scanner.javax.ParameterOnMethodTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaParameterOnMethod() throws IOException, JSONException {
        test("params.parameter-on-method.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParameterOnMethodTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxParameterOnField() throws IOException, JSONException {
        test("params.parameter-on-field.json",
                test.io.smallrye.openapi.runtime.scanner.javax.ResourcePathParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaParameterOnField() throws IOException, JSONException {
        test("params.parameter-on-field.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.ResourcePathParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxParameterInBeanFromField() throws IOException, JSONException {
        test("params.parameter-in-bean-from-field.json",
                test.io.smallrye.openapi.runtime.scanner.javax.ParameterInBeanFromFieldTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.ParameterInBeanFromFieldTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaParameterInBeanFromField() throws IOException, JSONException {
        test("params.parameter-in-bean-from-field.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParameterInBeanFromFieldTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParameterInBeanFromFieldTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxParameterInBeanFromSetter() throws IOException, JSONException {
        test("params.parameter-in-bean-from-setter.json",
                test.io.smallrye.openapi.runtime.scanner.javax.ParameterInBeanFromSetterTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.ParameterInBeanFromSetterTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaParameterInBeanFromSetter() throws IOException, JSONException {
        test("params.parameter-in-bean-from-setter.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParameterInBeanFromSetterTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParameterInBeanFromSetterTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxPathParamWithFormParams() throws IOException, JSONException {
        test("params.path-param-with-form-params.json",
                test.io.smallrye.openapi.runtime.scanner.javax.PathParamWithFormParamsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaPathParamWithFormParams() throws IOException, JSONException {
        test("params.path-param-with-form-params.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.PathParamWithFormParamsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxMultipleContentTypesWithFormParams() throws IOException, JSONException {
        test("params.multiple-content-types-with-form-params.json",
                test.io.smallrye.openapi.runtime.scanner.javax.MultipleContentTypesWithFormParamsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaMultipleContentTypesWithFormParams() throws IOException, JSONException {
        test("params.multiple-content-types-with-form-params.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.MultipleContentTypesWithFormParamsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testFailOnDuplicateOperationIds() {
        final OpenApiConfig config = failOnDuplicateOperationIdsConfig();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> test(config, "params.multiple-content-types-with-form-params.json",
                        MultipleContentTypesWithFormParamsTestResource.class, Widget.class));
        assertStartsWith(exception.getMessage(), "SROAP07950: Duplicate operationId:", "Exception message");
    }

    private static void assertStartsWith(String actual, String expectedStart, String description) {
        final boolean condition = actual != null && actual.startsWith(expectedStart);
        if (!condition) {
            Assertions
                    .fail(String.format("%s is expected to start with: <%s> but was <%s>", description, expectedStart, actual));
        }
    }

    @Test
    void testJavaxParametersInConstructor() throws IOException, JSONException {
        test("params.parameters-in-constructor.json",
                test.io.smallrye.openapi.runtime.scanner.javax.ParametersInConstructorTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.ParametersInConstructorTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaParametersInConstructor() throws IOException, JSONException {
        test("params.parameters-in-constructor.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParametersInConstructorTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParametersInConstructorTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxMatrixParamsOnResourceMethodArgs() throws IOException, JSONException {
        test("params.matrix-params-on-resource-method-args.json",
                test.io.smallrye.openapi.runtime.scanner.javax.MatrixParamsOnResourceMethodArgsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaMatrixParamsOnResourceMethodArgs() throws IOException, JSONException {
        test("params.matrix-params-on-resource-method-args.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.MatrixParamsOnResourceMethodArgsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxMatrixParamsOnResourceMethodCustomName() throws IOException, JSONException {
        test("params.matrix-params-on-resource-method-custom-name.json",
                test.io.smallrye.openapi.runtime.scanner.javax.MatrixParamsOnResourceMethodCustomNameTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaMatrixParamsOnResourceMethodCustomName() throws IOException, JSONException {
        test("params.matrix-params-on-resource-method-custom-name.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.MatrixParamsOnResourceMethodCustomNameTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxMatrixParamsOnMethodAndFieldArgs() throws IOException, JSONException {
        test("params.matrix-params-on-method-and-field-args.json",
                test.io.smallrye.openapi.runtime.scanner.javax.MatrixParamsOnMethodAndFieldArgsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaMatrixParamsOnMethodAndFieldArgs() throws IOException, JSONException {
        test("params.matrix-params-on-method-and-field-args.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.MatrixParamsOnMethodAndFieldArgsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxAllTheParams() throws IOException, JSONException {
        test("params.all-the-params.json", test.io.smallrye.openapi.runtime.scanner.javax.AllTheParamsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.AllTheParamsTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
        test("params.all-the-params.json",
                test.io.smallrye.openapi.runtime.scanner.javax.RestEasyReactiveAllTheParamsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.RestEasyReactiveAllTheParamsTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaAllTheParams() throws IOException, JSONException {
        test("params.all-the-params.json", test.io.smallrye.openapi.runtime.scanner.jakarta.AllTheParamsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.AllTheParamsTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
        test("params.all-the-params.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.RestEasyReactiveAllTheParamsTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.RestEasyReactiveAllTheParamsTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxMultipartForm() throws IOException, JSONException {
        test("params.multipart-form.json", test.io.smallrye.openapi.runtime.scanner.javax.MultipartFormTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.MultipartFormTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class, InputStream.class);
    }

    @Test
    void testJakartaMultipartForm() throws IOException, JSONException {
        test("params.multipart-form.json", test.io.smallrye.openapi.runtime.scanner.jakarta.MultipartFormTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.MultipartFormTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class, InputStream.class);
    }

    @Test
    void testJavaxEnumQueryParam() throws IOException, JSONException {
        test("params.enum-form-param.json", test.io.smallrye.openapi.runtime.scanner.javax.EnumQueryParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.EnumQueryParamTestResource.TestEnum.class,
                test.io.smallrye.openapi.runtime.scanner.javax.EnumQueryParamTestResource.TestEnumWithSchema.class);
    }

    @Test
    void testJakartaEnumQueryParam() throws IOException, JSONException {
        test("params.enum-form-param.json", test.io.smallrye.openapi.runtime.scanner.jakarta.EnumQueryParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.EnumQueryParamTestResource.TestEnum.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.EnumQueryParamTestResource.TestEnumWithSchema.class);
    }

    @Test
    void testJavaxUUIDQueryParam() throws IOException, JSONException {
        test("params.uuid-params-responses.json",
                test.io.smallrye.openapi.runtime.scanner.javax.UUIDQueryParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.UUIDQueryParamTestResource.WrappedUUID.class);
    }

    @Test
    void testJakartaUUIDQueryParam() throws IOException, JSONException {
        test("params.uuid-params-responses.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.UUIDQueryParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.UUIDQueryParamTestResource.WrappedUUID.class);
    }

    @Test
    void testJavaxRestEasyFieldsAndSetters() throws IOException, JSONException {
        test("params.resteasy-fields-and-setters.json",
                test.io.smallrye.openapi.runtime.scanner.javax.RestEasyFieldsAndSettersTestResource.class);
    }

    @Test
    void testJakartaRestEasyFieldsAndSetters() throws IOException, JSONException {
        test("params.resteasy-fields-and-setters.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.RestEasyFieldsAndSettersTestResource.class);
    }

    @Test
    void testJavaxCharSequenceArrayParam() throws IOException, JSONException {
        test("params.char-sequence-arrays.json",
                test.io.smallrye.openapi.runtime.scanner.javax.CharSequenceArrayParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.CharSequenceArrayParamTestResource.EchoResult.class);
    }

    @Test
    void testJakartaCharSequenceArrayParam() throws IOException, JSONException {
        test("params.char-sequence-arrays.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.CharSequenceArrayParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.CharSequenceArrayParamTestResource.EchoResult.class);
    }

    @Test
    void testJavaxOptionalParam() throws IOException, JSONException {
        test("params.optional-types.json", test.io.smallrye.openapi.runtime.scanner.javax.OptionalParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.OptionalParamTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.javax.OptionalParamTestResource.NestedBean.class,
                test.io.smallrye.openapi.runtime.scanner.javax.OptionalParamTestResource.OptionalWrapper.class, Optional.class,
                OptionalDouble.class, OptionalLong.class);
    }

    @Test
    void testJakartaOptionalParam() throws IOException, JSONException {
        test("params.optional-types.json", test.io.smallrye.openapi.runtime.scanner.jakarta.OptionalParamTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.OptionalParamTestResource.Bean.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.OptionalParamTestResource.NestedBean.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.OptionalParamTestResource.OptionalWrapper.class,
                Optional.class, OptionalDouble.class, OptionalLong.class);
    }

    @Test
    void testJavaxPathParamTemplateRegex() throws IOException, JSONException {
        test("params.path-param-templates.json",
                test.io.smallrye.openapi.runtime.scanner.javax.PathParamTemplateRegexTestResource.class);
    }

    @Test
    void testJakartaPathParamTemplateRegex() throws IOException, JSONException {
        test("params.path-param-templates.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.PathParamTemplateRegexTestResource.class);
    }

    @Test
    void testJavaxPathSegmentMatrix() throws IOException, JSONException {
        test("params.path-segment-param.json",
                test.io.smallrye.openapi.runtime.scanner.javax.PathSegmentMatrixTestResource.class);
    }

    @Test
    void testJakartaPathSegmentMatrix() throws IOException, JSONException {
        test("params.path-segment-param.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.PathSegmentMatrixTestResource.class);
    }

    @Test
    void testJavaxParamNameOverride() throws IOException, JSONException {
        test("params.param-name-override.json",
                test.io.smallrye.openapi.runtime.scanner.javax.ParamNameOverrideTestResource.class);
    }

    @Test
    void testJakartaParamNameOverride() throws IOException, JSONException {
        test("params.param-name-override.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParamNameOverrideTestResource.class);
    }

    @Test
    void testJavaxCommonTargetMethodParameter() throws IOException, JSONException {
        test("params.common-annotation-target-method.json",
                test.io.smallrye.openapi.runtime.scanner.javax.CommonTargetMethodParameterResource.class);
    }

    @Test
    void testJakartaCommonTargetMethodParameter() throws IOException, JSONException {
        test("params.common-annotation-target-method.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.CommonTargetMethodParameterResource.class);
    }

    @Test
    void testJavaxRestEasyReactivePathParamOmitted() throws IOException, JSONException {
        test("params.resteasy-reactive-missing-restpath.json",
                test.io.smallrye.openapi.runtime.scanner.javax.RestEasyReactivePathParamOmittedTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.RestEasyReactivePathParamOmittedTestResource2.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJakartaRestEasyReactivePathParamOmitted() throws IOException, JSONException {
        test("params.resteasy-reactive-missing-restpath.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.RestEasyReactivePathParamOmittedTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.RestEasyReactivePathParamOmittedTestResource2.class,
                test.io.smallrye.openapi.runtime.scanner.Widget.class);
    }

    @Test
    void testJavaxSerializedIndexParameterAnnotations() throws IOException, JSONException {
        Index i1 = indexOf(test.io.smallrye.openapi.runtime.scanner.javax.GreetResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.GreetResource.GreetingMessage.class);
        testSerializedIndexParameterAnnotations(i1);
    }

    @Test
    void testJakartaSerializedIndexParameterAnnotations() throws IOException, JSONException {
        Index i1 = indexOf(test.io.smallrye.openapi.runtime.scanner.jakarta.GreetResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.GreetResource.GreetingMessage.class);
        testSerializedIndexParameterAnnotations(i1);
    }

    void testSerializedIndexParameterAnnotations(Index i) throws IOException, JSONException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IndexWriter writer = new IndexWriter(out);
        writer.write(i);

        Index index = new IndexReader(new ByteArrayInputStream(out.toByteArray())).read();
        OpenApiAnnotationScanner scanner = new OpenApiAnnotationScanner(emptyConfig(), index);
        OpenAPI result = scanner.scan();
        printToConsole(result);
        assertJsonEquals("params.serialized-annotation-index.json", result);
    }

    @Test
    void testJavaxParameterRefOnly() throws IOException, JSONException {
        test("params.parameter-ref-property.json",
                test.io.smallrye.openapi.runtime.scanner.javax.ParameterRefTestApplication.class,
                test.io.smallrye.openapi.runtime.scanner.javax.ParameterRefTestResource.class);
    }

    @Test
    void testJakartaParameterRefOnly() throws IOException, JSONException {
        test("params.parameter-ref-property.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParameterRefTestApplication.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParameterRefTestResource.class);
    }

    @Test
    void testJavaxDefaultEnumValue() throws IOException, JSONException {
        test("params.local-schema-attributes.json",
                test.io.smallrye.openapi.runtime.scanner.javax.DefaultEnumTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.DefaultEnumTestResource.MyEnum.class);
    }

    @Test
    void testJakartaDefaultEnumValue() throws IOException, JSONException {
        test("params.local-schema-attributes.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.DefaultEnumTestResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.DefaultEnumTestResource.MyEnum.class);
    }

    @Test
    void testJavaxGenericTypeVariableResource() throws IOException, JSONException {
        test("params.generic-type-variables.json", test.io.smallrye.openapi.runtime.scanner.javax.BaseGenericResource.class,
                test.io.smallrye.openapi.runtime.scanner.javax.BaseGenericResource.GenericBean.class,
                test.io.smallrye.openapi.runtime.scanner.javax.IntegerStringUUIDResource.class);
    }

    @Test
    void testJakartaGenericTypeVariableResource() throws IOException, JSONException {
        test("params.generic-type-variables.json", test.io.smallrye.openapi.runtime.scanner.jakarta.BaseGenericResource.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.BaseGenericResource.GenericBean.class,
                test.io.smallrye.openapi.runtime.scanner.jakarta.IntegerStringUUIDResource.class);
    }

    @Test
    void testPreferredParameterOrderWithAnnotation() throws IOException, JSONException {
        test("params.annotation-preferred-order.json",
                test.io.smallrye.openapi.runtime.scanner.jakarta.ParameterOrderResource.CLASSES);
    }

    static class Issue1256 {
        static final Class<?>[] CLASSES = {
                BeanParamBean.class,
                Filter.class,
                FilterBean.class,
                JsonBase.class,
                DataJson.class,
                GenericBaseInterface.class,
                RestInterface.class,
                RestImpl.class
        };

        static class BeanParamBean {
            @jakarta.ws.rs.QueryParam("param")
            @Parameter(description = "A parameter")
            private String param;
        }

        interface Filter {
        }

        static class FilterBean implements Filter {
        }

        static class JsonBase {
        }

        static class DataJson extends JsonBase {
        }

        interface GenericBaseInterface<T extends JsonBase, F extends Filter> {

            @Operation(summary = "list")
            @APIResponse(responseCode = "200", description = "OK")
            @APIResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(type = SchemaType.OBJECT)))
            List<T> list(BeanParamBean params, F filter);
        }

        @jakarta.ws.rs.Path("reproducer/reproducers")
        @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
        @jakarta.ws.rs.Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
        @Tag(name = "reproducers", description = "This resource is there for reproducing bug 1256.")
        interface RestInterface extends GenericBaseInterface<DataJson, FilterBean> {

            @jakarta.ws.rs.POST
            @Operation(summary = "create")
            @APIResponse(responseCode = "200", description = "OK")
            @APIResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(type = SchemaType.OBJECT)))
            DataJson create(DataJson json);

            @jakarta.ws.rs.GET
            @Override
            List<DataJson> list(@jakarta.ws.rs.BeanParam BeanParamBean params, @jakarta.ws.rs.BeanParam FilterBean filter);
        }

        static class RestImpl implements RestInterface {
            @Override
            public DataJson create(DataJson json) {
                return null;
            }

            @Override
            public List<DataJson> list(BeanParamBean params, FilterBean filter) {
                return null;
            }
        }
    }

    @Test
    void testParamsNotDuplicated() throws IOException, JSONException {
        test("params.synthetic-methods-not-included.json", Issue1256.CLASSES);
    }
}
