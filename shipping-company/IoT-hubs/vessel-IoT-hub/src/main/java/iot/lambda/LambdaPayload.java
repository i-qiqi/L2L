package iot.lambda;

import lombok.Data;

@Data
public class LambdaPayload {
    private Event event;
    private Object data;
}
