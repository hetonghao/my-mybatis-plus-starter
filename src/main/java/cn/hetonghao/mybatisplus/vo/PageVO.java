package cn.hetonghao.mybatisplus.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通用分页参数
 *
 * @author HeTongHao
 * @since 2019-07-03
 */
@Data
@Accessors(chain = true)
public class PageVO {
    @ApiModelProperty("页码")
    private Integer pageNumber;
    @ApiModelProperty("每页数据条数")
    private Integer pageSize;

    /**
     * 生成分页组件
     *
     * @return Mybatis-Plus分页插件
     */
    public IPage generatePage() {
        return pageNumber == null || pageSize == null ? new Page(1L, -1L) : new Page(pageNumber, pageSize);
    }
}
