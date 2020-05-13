package com.ivo.mrp2.controlller;

import com.ivo.common.result.PageResult;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.Bom;
import com.ivo.mrp2.service.BomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BOM清单请求处理
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/bom")
public class BomController {

    private BomService bomService;

    @Autowired
    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    /**
     * 分页查询BOM清单
     * @param page 页码
     * @param limit 分页大小
     * @param product 产品
     * @param material 料号
     * @return PageResult
     */
    @RequestMapping("/list")
    public PageResult list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit,
                           @RequestParam(defaultValue = "") String product, @RequestParam(defaultValue = "") String material,
                           @RequestParam(defaultValue = "") String plant) {
        Page<Bom> p = bomService.pageQueryBom(page-1, limit, product, material, plant);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }
}
