package top.charjin.oneapi.openinterface.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.charjin.oneapi.common.model.BaseResponse;
import top.charjin.oneapi.common.model.ErrorCode;
import top.charjin.oneapi.common.util.ResultUtils;
import top.charjin.oneapi.openinterface.exception.BusinessException;
import top.charjin.oneapi.openinterface.model.vo.CartoonAvatarUrlVO;
import top.charjin.oneapi.openinterface.model.vo.UserAvatarUrlVO;
import top.charjin.oneapi.openinterface.service.AvatarService;

import java.io.IOException;

/**
 * 头像接口
 */
@RestController
@RequestMapping("/avatar")
@Slf4j
public class AvatarController {

    @Autowired
    AvatarService avatarService;

    @GetMapping("/cartoon")
    public BaseResponse<CartoonAvatarUrlVO> getCartoonAvatarUrl() {
        String avatarUrl = "";
        try {
            avatarUrl = avatarService.getCartoonAvatarUrl();
            System.out.println(avatarUrl);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口地址失效");
        }
        return ResultUtils.success(new CartoonAvatarUrlVO(avatarUrl));
    }

    /**
     * @param gender male or female
     * @return AvatarUrl
     */
    @GetMapping("/user")
    public BaseResponse<UserAvatarUrlVO> getUserAvatarUrl(@RequestParam String gender) {
        if (!gender.equals("male") && !gender.equals("female")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "gender 只能为 male 或 female");
        }
        String avatarUrl = "";
        try {
            avatarUrl = avatarService.getUserAvatarUrl(gender);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResultUtils.success(new UserAvatarUrlVO(avatarUrl));
    }

    @ExceptionHandler(Exception.class)
    private String exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return e.getMessage();
    }
}
