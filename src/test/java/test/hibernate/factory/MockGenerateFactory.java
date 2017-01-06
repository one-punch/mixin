package test.hibernate.factory;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dozer.DozerBeanMapper;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.shiro.PasswordHelper;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.generate.MockGenerate;


public class MockGenerateFactory {

    public final MockGenerate<UserPo> user;

    public final MockGenerate<RolePo> role;

    public MockGenerateFactory(IBaseDao baseDao, DozerBeanMapper mapper) {

        user = new MockGenerate<UserPo>(baseDao) {

            private PasswordHelper helper;

            @Override
            protected void setList(List<UserPo> list) {
                helper = new PasswordHelper();
                List<UserVo> voList = new ArrayList<>();
                
                voList.add(new UserVo("user", MD5Utils.md5("000001"), "18320376671", "9321410271@qq.com"));
                voList.add(new UserVo("supermanager", MD5Utils.md5("000002"), "18320376672", "932141027@qq.com"));
                
                for (UserVo vo : voList) {
                    encryptUser(vo);
                    list.add(mapper.map(vo, UserPo.class));
                }
            }

            private void encryptUser(UserVo userVo) {
                String uuid;
                uuid = UUID.randomUUID().toString().replaceAll("-", "");
                userVo.setPrincipal(uuid);
                helper.encryptPassword(userVo);
            }

        };

        role = new MockGenerate<RolePo>(baseDao) {
            @Override
            protected void setList(List<RolePo> list) {
                for (RoleType role : RoleType.values()) {
                    list.add(new RolePo(role));
                }
            }
        };

    }
    
    public static void main(String[] args) {
        System.out.println(MD5Utils.md5("123456"));// e10adc3949ba59abbe56e057f20f883e
        System.out.println(MD5Utils.md5("000001"));// 4fc711301f3c784d66955d98d399afb
        System.out.println(MD5Utils.md5("000002"));// 768c1c687efe184ae6dd2420710b8799
    }

}
