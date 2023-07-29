package top.charjin.oneapi.backend.service.impl;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.charjin.oneapi.backend.exception.BusinessException;
import top.charjin.oneapi.backend.exception.OneAPIParseException;
import top.charjin.oneapi.backend.mapper.InterfaceInfoMapper;
import top.charjin.oneapi.backend.service.InterfaceInvokerService;
import top.charjin.oneapi.clientsdk.AbstractModel;
import top.charjin.oneapi.clientsdk.Credential;
import top.charjin.oneapi.clientsdk.client.OneApiClient;
import top.charjin.oneapi.clientsdk.exception.OneAPISDKException;
import top.charjin.oneapi.clientsdk.profile.ClientProfile;
import top.charjin.oneapi.clientsdk.profile.HttpProfile;
import top.charjin.oneapi.common.model.ErrorCode;
import top.charjin.oneapi.common.model.entity.InterfaceInfo;

import javax.el.MethodNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 接口调用服务. 通过反射动态的创建请求对象, 并调用对应的接口
 *
 * @author Zhichao
 */
@Service
public class InterfaceInvokerServiceImpl implements InterfaceInvokerService {

    static final String prefix = "top.charjin.oneapi.clientsdk.models";

    @Autowired
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public String invoke(Long id, String params, Credential credential) throws OneAPIParseException, OneAPISDKException {
        InterfaceInfo info = interfaceInfoMapper.selectById(id);
        String requestMethod = info.getMethod();
        String actionName = info.getAction();

        if (requestMethod == null || actionName == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "内部接口信息不完整");
        }
        if (!requestMethod.equals(HttpProfile.REQ_GET) && !requestMethod.equals(HttpProfile.REQ_POST)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不支持 " + requestMethod + " 请求方式");
        }

        Gson gson = new Gson();
        HashMap<String, String> paramMap;
        if (requestMethod.equals(HttpProfile.REQ_GET)) {
            paramMap = parseGetParams(params);
        } else {
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            paramMap = gson.fromJson(params, type);
        }

        HttpProfile httpProfile = new HttpProfile();
        // todo 接口调用的地址需要根据部署环境修改，如开发环境和生产环境或根据数据库进行配置
        httpProfile.setEndpoint("localhost:8090");
        httpProfile.setProtocol(HttpProfile.REQ_HTTP);
        httpProfile.setReqMethod(requestMethod);

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        OneApiClient client = new OneApiClient(credential, clientProfile);


        String respStr = "";

        try {
            Class<AbstractModel> requestModelClass = (Class<AbstractModel>) getModelClass(actionName);
            AbstractModel req = gson.fromJson(gson.toJson(paramMap), requestModelClass);

            Method method = client.getClass().getMethod(
                    actionName.substring(0, 1).toLowerCase() + actionName.substring(1),
                    req.getClass());
            AbstractModel resp = (AbstractModel) method.invoke(client, req);
            respStr = AbstractModel.toJsonString(resp);
        } catch (ClassNotFoundException | MethodNotFoundException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new OneAPIParseException("SDK版本未更新，内部接口解析失败");
        } catch (InvocationTargetException e) {
            // 反射调用的方法抛出异常为 InvocationTargetException
            // 通过 getTargetException() 获取原始异常 OneAPISDKException
            Throwable targetException = e.getTargetException();
            if (targetException instanceof OneAPISDKException) {
                throw new OneAPISDKException(targetException.getMessage());
            }
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }

        return respStr;
    }

    private Class<?> getModelClass(String action) throws ClassNotFoundException {
        String requestClassName = prefix + "." + action + "Request";
        return Class.forName(requestClassName);
    }


    /**
     * 解析get请求参数
     * 将由 & 拼接的参数格式转为 HashMap
     *
     * @param params
     * @return
     */
    private HashMap<String, String> parseGetParams(String params) {
        HashMap<String, String> result = new HashMap<>();
        if (params == null) {
            return result;
        }

        String[] pairs = params.trim().split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                result.put(kv[0], kv[1]);
            }
        }

        return result;
    }
}
