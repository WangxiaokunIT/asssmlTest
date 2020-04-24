package com.xinshang.modular.im.controller;

import com.xinshang.constant.Constants;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.vo.ResultVO;
import com.xinshang.modular.im.dto.*;
import com.xinshang.modular.im.model.ImUser;
import com.xinshang.modular.im.service.*;
import com.xinshang.modular.system.model.File;
import com.xinshang.modular.system.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjiajia
 * @since 19-7-5
 */
@RestController
@RequestMapping("/im")
public class ImController {

    @Autowired
    private IImUserService iImUserService;

    @Autowired
    private IGroupService iGroupService;

    @Autowired
    private IGroupUserService iGroupUserService;

    @Autowired
    private IFriendGroupUserService iFriendGroupUserService;

    @Autowired
    private ImServerService imServerService;

    @Autowired
    private IFileService fileService;

    @GetMapping("/init")
    public Object init(){
        Integer userId = ShiroKit.getUser().getId();
        //查询个人信息
        ImUser imUser = iImUserService.selectById(userId);

        InitDTO initDTO = new InitDTO();
        InitUser iu = new InitUser();
        iu.setAvatar(imUser.getAvatar());
        iu.setId(imUser.getId());
        iu.setSign(imUser.getSign());
        iu.setStatus(Constants.ONLINE);
        iu.setUsername(imUser.getUsername());
        initDTO.setMine(iu);

        //查询好友列表
        List<InitFriendGroup> initFriendGroups = iFriendGroupUserService.initFriendGroupByUserId(userId);
        initFriendGroups.forEach(f->{
            f.getList().forEach(u->{
                u.setStatus(imServerService.isUserOnline(u.getId())?Constants.ONLINE:Constants.OFFLINE);
            });
        });
        initDTO.setFriend(initFriendGroups);

        //查询群信息
        List<InitGroup> initGroups = iGroupService.initGroupByUserId(userId);
        initDTO.setGroup(initGroups);

        return ResultVO.of(0,"初始化聊天成功",initDTO);
    }

    /**
     * 更新用户签名
     * @param userId
     * @param sign
     * @return
     */
    @PutMapping("/sign")
    public Object updateSign(Integer userId,String sign) {

        ImUser imUser = iImUserService.selectById(userId);
        imUser.setSign(sign);
        iImUserService.updateById(imUser);
        //通知好友更新签名
        imServerService.getOnlineFriend(userId).forEach(uid->{
            imServerService.sendMessage(uid,ResultVO.of().put("type","updateSign").put("userId",userId).put("sign",sign).toString());
        });
        return null;
    }

    /**
     * 获取群成员
     * @param groupId
     * @return
     */
    @GetMapping("/members")
    public Object getMembers(@RequestParam("id") Integer groupId){
        List<InitUser> list = iGroupUserService.getMembersByGroupId(groupId);
        return ResultVO.of(0,"获取群成员成功",new GroupMembersDTO(list));
    }

    /**
     * 聊天图片上传
     * @param file
     * @param categoryId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/image/{categoryId}")
    public Object uploadImage(@RequestParam(value = "file") MultipartFile file,
                         @PathVariable String categoryId) throws Exception{
        File uploadFile = fileService.upload(file,categoryId);
        Map<String,String> map = new HashMap<>();
        map.put("src",uploadFile.getSavePath());
        return ResultVO.of(0,"图片上传成功",map);
    }

    /**
     * 聊天文件上传
     * @param file
     * @param categoryId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/file/{categoryId}")
    public Object uploadFile(@RequestParam(value = "file") MultipartFile file,
                              @PathVariable String categoryId) throws Exception{
        File uploadFile = fileService.upload(file,categoryId);
        Map<String,String> map = new HashMap();
        map.put("src",uploadFile.getSavePath());
        map.put("name",uploadFile.getOriginalName());
        return ResultVO.of(0,"图片上传成功",map);
    }


}
