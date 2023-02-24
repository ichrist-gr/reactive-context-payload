package org.acme.retrieveusername.interceptor.processor;

import com.jayway.jsonpath.JsonPath;
import io.vertx.core.json.Json;
import org.acme.retrieveusername.interceptor.annotation.UsernameInterceptor;

import javax.enterprise.context.Dependent;
import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Dependent
public class SearchFieldsRetriever {
    public List<String> retrieveSearchFields(UsernameInterceptor annotation, InvocationContext invocationContext) {
        List<String> ids = new ArrayList<>();

        List<String> payloadSearchFields = extractPayloadSearchFields(invocationContext, annotation);
        if (payloadSearchFields != null) {
            ids.addAll(payloadSearchFields);
        }

        return ids;
    }

    private List<String> extractPayloadSearchFields(InvocationContext invocationContext, UsernameInterceptor annotation) {
        String[] payloadFields = annotation.payloadFields();
        Class<?> classType = annotation.classType();

        if (payloadFields == null || classType == null) {
            return null;
        }

        String stringPayload = getPayload(invocationContext.getParameters(), classType);
        return getPayloadSearchFields(stringPayload, Arrays.asList(payloadFields));
    }

    private String getPayload(Object[] parameters, Class<?> aclass) {
        for (Object param : parameters) {
            if (aclass.isInstance(param)) {
                return Json.encode(param);
            }
        }

        return null;
    }

    private List<String> getPayloadSearchFields(String payload, List<String> payloadIds) {
        List<String> ids = new ArrayList<>(payloadIds.size());

        for (String payloadId : payloadIds) {
            List<String> stringValues = readJsonValue(payload, payloadId);
            if (stringValues != null) {
                ids.addAll(stringValues);
            }
        }

        return ids;
    }

    private List<String> readJsonValue(String payload, String payloadId) {
        try {
            return JsonPath.read(payload, payloadId);
        } catch (RuntimeException runtimeException) {
            return null;
        }
    }
}
