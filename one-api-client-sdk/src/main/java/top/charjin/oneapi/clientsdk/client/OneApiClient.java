package top.charjin.oneapi.clientsdk.client;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import top.charjin.oneapi.clientsdk.AbstractClient;
import top.charjin.oneapi.clientsdk.Credential;
import top.charjin.oneapi.clientsdk.JsonResponseModel;
import top.charjin.oneapi.clientsdk.exception.OneAPISDKException;
import top.charjin.oneapi.clientsdk.models.GenerateCartoonAvatarUrlRequest;
import top.charjin.oneapi.clientsdk.models.GenerateCartoonAvatarUrlResponse;
import top.charjin.oneapi.clientsdk.models.GenerateUserAvatarUrlRequest;
import top.charjin.oneapi.clientsdk.models.GenerateUserAvatarUrlResponse;
import top.charjin.oneapi.clientsdk.profile.ClientProfile;

import java.lang.reflect.Type;


public class OneApiClient extends AbstractClient {

    private static final String endpoint = "localhost:8090";


    public OneApiClient(Credential credential) {
        this(credential, new ClientProfile());
    }

    public OneApiClient(Credential credential, ClientProfile profile) {
        super(OneApiClient.endpoint, credential, profile);
    }

    public GenerateCartoonAvatarUrlResponse generateCartoonAvatarUrl(GenerateCartoonAvatarUrlRequest req) throws OneAPISDKException {
        JsonResponseModel<GenerateCartoonAvatarUrlResponse> rsp = null;
        String rspStr = "";
        try {
            Type type = new TypeToken<JsonResponseModel<GenerateCartoonAvatarUrlResponse>>() {
            }.getType();
            rspStr = this.internalRequest(req, "/api/avatar/cartoon");
            rsp = gson.fromJson(rspStr, type);
        } catch (JsonSyntaxException e) {
            throw new OneAPISDKException("response message: " + rspStr + ".\n Error message: " + e.getMessage());
        }
        return rsp.getData();
    }

    public GenerateUserAvatarUrlResponse generateUserAvatarUrl(GenerateUserAvatarUrlRequest req) throws OneAPISDKException {
        JsonResponseModel<GenerateUserAvatarUrlResponse> rsp = null;
        String rspStr = "";
        try {
            Type type = new TypeToken<JsonResponseModel<GenerateUserAvatarUrlResponse>>() {
            }.getType();
            rspStr = this.internalRequest(req, "/api/avatar/user");
            rsp = gson.fromJson(rspStr, type);
        } catch (JsonSyntaxException e) {
            throw new OneAPISDKException("response message: " + rspStr + ".\n Error message: " + e.getMessage());
        }
        return rsp.getData();
    }


}
