package wapper.common.utils;

/*
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
*/

/**
 * 安全服务工具类
 * 
 * @author ruoyi
 */
public class SecurityUtils
{
    /**
     * 用户ID
     **/
    public static Long getUserId()
    {
        try
        {
            //return getLoginUser().getUserId();
        }
        catch (Exception e)
        {
            //throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    /**
     * 获取部门ID
     **/
    public static Long getDeptId()
    {
        try
        {
            //return getLoginUser().getUserId();
        }
        catch (Exception e)
        {
            //throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
    
    /**
     * 获取用户账户
     **/
    public static String getUsername()
    {
        try
        {
            //return getLoginUser().getUserId();
        }
        catch (Exception e)
        {
            //throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    /**
     * 获取用户
     **/
    public static void getLoginUser()
    {
        try
        {
            //return getLoginUser().getUserId();
        }
        catch (Exception e)
        {
            //throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }

    }


    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */


    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */


    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }
}
