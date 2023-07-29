package top.charjin.oneapi.backend.service;

import top.charjin.oneapi.backend.exception.BusinessException;
import top.charjin.oneapi.backend.exception.OneAPIParseException;
import top.charjin.oneapi.clientsdk.Credential;
import top.charjin.oneapi.clientsdk.exception.OneAPISDKException;

public interface InterfaceInvokerService {

    String invoke(Long id, String params, Credential credential) throws BusinessException, OneAPIParseException, OneAPISDKException;

}
