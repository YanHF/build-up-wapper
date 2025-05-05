package com.huaifang.yan.mapper;

import com.huaifang.yan.dal.TbPgsPccRuleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TbPgsPccRuleMapper {

    TbPgsPccRuleDO  selectTbPgsPccRuleById(long id);
    /**
     * 新增
     * @author BEJSON.com
     * @date 2025/04/24
     **/
    int insert(TbPgsPccRuleDO tbPgsPccRule);

    /**
     * 刪除
     * @author BEJSON.com
     * @date 2025/04/24
     **/
    int delete(int id);

    /**
     * 更新
     * @author BEJSON.com
     * @date 2025/04/24
     **/
    int update(TbPgsPccRuleDO tbPgsPccRule);

    /**
     * 查询 根据主键 id 查询
     * @author BEJSON.com
     * @date 2025/04/24
     **/
    TbPgsPccRuleDO load(int id);

    /**
     * 查询 分页查询
     * @author BEJSON.com
     * @date 2025/04/24
     **/
    List<TbPgsPccRuleDO> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author BEJSON.com
     * @date 2025/04/24
     **/
    int pageListCount(int offset,int pagesize);

}
