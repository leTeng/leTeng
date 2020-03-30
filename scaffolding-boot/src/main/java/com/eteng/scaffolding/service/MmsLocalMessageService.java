package com.eteng.scaffolding.service;

import com.eteng.scaffolding.service.dto.MmsLocalMessageDTO;
import com.eteng.scaffolding.service.dto.MmsLocalMessageQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author eTeng
* @date 2020-03-17
*/
public interface MmsLocalMessageService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Page<MmsLocalMessageDTO>
    */
    Page<MmsLocalMessageDTO> queryAll(MmsLocalMessageQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<MmsLocalMessageDTO>
    */
    List<MmsLocalMessageDTO> queryAll(MmsLocalMessageQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return MmsLocalMessageDTO
     */
    MmsLocalMessageDTO findById(String id);

    MmsLocalMessageDTO create(MmsLocalMessageDTO resources);

    void update(MmsLocalMessageDTO resources);

    void delete(String id);

    void download(List<MmsLocalMessageDTO> all, HttpServletResponse response) throws IOException;

    /**
     * 发送消息
     */
    void sendMessage();

}