package com.wteam.mixin.shiro;


import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.UserVo;


/**
 * 安全密码加密.
 *
 * @author benko
 */
public class PasswordHelper {

    /** 随机数生成器 */
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    /** 指定散列算法为md5 */
    public static final String algorithmName = "md5";

    /** 散列迭代次数 */
    public static final int hashIterations = 2;

    /**
     * Description: 加密账号<br>
     *
     * @param principal 身份
     * @param password 未加密密码
     * @return {newPassword, salt}
     * @see
     */
    public String[] encryptPassword(String principal, String password) {

        String[] newPassword = new String[2];//{newPassword, salt}
        // 生成随机数
        newPassword[1] = randomNumberGenerator.nextBytes().toHex();
        newPassword[0] = new SimpleHash(
            algorithmName,
            password,
            // 用户的盐: 身份 + 随机数
            ByteSource.Util.bytes(principal + newPassword[1]),
            hashIterations).toHex();

        return newPassword;
    }

    /**
     * 功能：加密从前台传来的用户密码， 添加用户或修改密码时使用。
     *
     * @param user
     */
    public void encryptPassword(UserVo user) {
        user.setPassSalt(randomNumberGenerator.nextBytes().toHex());

        if (user.getPrincipal() == null || "".equals(user.getPrincipal())) {
            throw new NullPointerException("user.principal 没有赋值！");
        }
        // 最终的密码摘要
        String newPassword = new SimpleHash(algorithmName, user.getPassword(),
            ByteSource.Util.bytes(user.createCredentialsSalt()), hashIterations).toHex();

        user.setPassword(newPassword);
    }

    /**
     * 比较 要匹配的密码是否和数据库的密码一样.
     *
     * @param matchPassword
     * @param userPo
     * @return
     */
    public boolean matchPassword(String matchPassword, UserPo userPo) {

        if (userPo.getPrincipal() == null || "".equals(userPo.getPrincipal())) {
            throw new NullPointerException("user.principal 没有赋值！");
        }
        if (userPo.getPassSalt() == null || "".equals(userPo.getPassSalt())) {
            throw new NullPointerException("user.passSalt 没有赋值！");
        }
        // 最终的密码摘要
        String newPassword = new SimpleHash(algorithmName, matchPassword,
            ByteSource.Util.bytes(userPo.getPrincipal() + userPo.getPassSalt()),
            hashIterations).toHex();

        return newPassword.equals(userPo.getPassword());
    }
}
