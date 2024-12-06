package cn.odboy.config.model;

import cn.odboy.config.constant.TransferMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallMessage implements Serializable {
    /**
     * 消息类型：cn.odboy.config.constant.TransferMessageType
     */
    private TransferMessageType type;
    private Response resp;

    @Data
    public static class Response implements Serializable {
        private Boolean success = true;
        private String errorCode = "0";
        private String errorMessage = "success";
        private Object data;

        public static Response bad(String errorMessage) {
            Response response = new Response();
            response.setSuccess(false);
            response.setErrorCode("400");
            response.setErrorMessage(errorMessage);
            response.setData(null);
            return response;
        }

        public static Response ok(Object data, String errorMessage) {
            Response response = new Response();
            response.setSuccess(true);
            response.setErrorCode("0");
            response.setErrorMessage(errorMessage);
            response.setData(data);
            return response;
        }

        public static Response ok(Object data) {
            Response response = new Response();
            response.setSuccess(true);
            response.setErrorCode("0");
            response.setErrorMessage("success");
            response.setData(data);
            return response;
        }
    }
}
