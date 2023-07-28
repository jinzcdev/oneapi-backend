package top.charjin.oneapi.clientsdk;


import org.junit.jupiter.api.Test;
import top.charjin.oneapi.clientsdk.client.OneApiClient;
import top.charjin.oneapi.clientsdk.exception.OneAPISDKException;
import top.charjin.oneapi.clientsdk.models.GenerateCartoonAvatarUrlRequest;
import top.charjin.oneapi.clientsdk.models.GenerateCartoonAvatarUrlResponse;
import top.charjin.oneapi.clientsdk.models.GenerateUserAvatarUrlRequest;
import top.charjin.oneapi.clientsdk.models.GenerateUserAvatarUrlResponse;
import top.charjin.oneapi.clientsdk.profile.ClientProfile;
import top.charjin.oneapi.clientsdk.profile.HttpProfile;

public class ClientTest {

    @Test
    void test() {
        try {
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("localhost:8090");
            httpProfile.setProtocol(HttpProfile.REQ_HTTP);
            httpProfile.setReqMethod(HttpProfile.REQ_GET);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);


            OneApiClient client = new OneApiClient(new Credential("a33266a46ab3281a0679f6a8aaa6d70da", "b59946636dd96ac26d9b5751cbed8262e"), clientProfile);


            GenerateUserAvatarUrlRequest request = new GenerateUserAvatarUrlRequest();
            request.setGender(GenerateUserAvatarUrlRequest.PARAM_FEMALE);
            GenerateUserAvatarUrlResponse resp = client.generateUserAvatarUrl(request);
            System.out.println(resp);
        } catch (OneAPISDKException e) {
            System.out.println(e.getMessage());
        }
    }
}
