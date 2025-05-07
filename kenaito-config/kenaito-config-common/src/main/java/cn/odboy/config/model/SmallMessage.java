package cn.odboy.config.model;

import cn.odboy.config.constant.TransferMessageType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallMessage implements Serializable {
  /** 消息类型：cn.odboy.config.constant.TransferMessageType */
  private TransferMessageType type;

  private Response resp;

  @Data
  public static class Response implements Serializable {
    private Boolean success = true;
    private String errorCode = "0";
    private String errorMessage = "success";
    private Object data;

    /**
     * 创建一个表示错误响应的对象 该方法用于当请求出现问题时返回信息给客户端
     *
     * @param errorMessage 错误信息，用于向客户端描述错误情况
     * @return 返回一个包含错误信息的Response对象
     */
    public static Response bad(String errorMessage) {
      Response response = new Response();
      response.setSuccess(false);
      response.setErrorCode("400");
      response.setErrorMessage(errorMessage);
      response.setData(null);
      return response;
    }

    /**
     * 创建一个表示成功响应的对象，包含数据和错误信息 该方法用于当请求成功时返回信息和数据给客户端
     *
     * @param data 成功响应的数据
     * @param errorMessage 即使在成功的情况下，也可能需要提供一些错误信息
     * @return 返回一个包含成功状态、数据和错误信息的Response对象
     */
    public static Response ok(Object data, String errorMessage) {
      Response response = new Response();
      response.setSuccess(true);
      response.setErrorCode("0");
      response.setErrorMessage(errorMessage);
      response.setData(data);
      return response;
    }

    /**
     * 创建一个表示成功响应的对象，仅包含数据 该方法用于当请求成功且不需要提供错误信息时使用
     *
     * @param data 成功响应的数据
     * @return 返回一个包含成功状态和数据的Response对象
     */
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
