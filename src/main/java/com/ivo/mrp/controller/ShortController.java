package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.direct.ary.MrpAry;
import com.ivo.mrp.entity.direct.ary.MrpAryMaterial;
import com.ivo.mrp.entity.direct.cell.MrpCell;
import com.ivo.mrp.entity.direct.cell.MrpCellMaterial;
import com.ivo.mrp.entity.direct.lcm.MrpLcm;
import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.service.MrpService;
import com.ivo.mrp.service.ProductCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Api(tags = "缺料报表接口")
@RestController
@RequestMapping("/mrp/short")
public class ShortController {

    private MrpService mrpService;

    private ProductCustomerService productCustomerService;

    @Autowired
    public ShortController(MrpService mrpService, ProductCustomerService productCustomerService) {
        this.mrpService = mrpService;
        this.productCustomerService = productCustomerService;
    }

    @ApiOperation("分页获取MRP缺料数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "searchProduct", value = "查询机种"),
            @ApiImplicitParam(name = "searchMaterialGroup", value = "查询物料组"),
            @ApiImplicitParam(name = "searchMaterial", value = "查询料号"),
            @ApiImplicitParam(name = "searchSupplier", value = "查询供应商")
    })
    @GetMapping("/getPageShort")
    public Result getPageShort(@RequestParam(required = false, defaultValue = "1") int page,
                                         @RequestParam(required = false, defaultValue = "50") int limit,
                                         String fab,
                                         @RequestParam(required = false, defaultValue = "") String searchProduct,
                                         @RequestParam(required = false, defaultValue = "") String searchMaterialGroup,
                                         @RequestParam(required = false, defaultValue = "") String searchMaterial,
                                         @RequestParam(required = false, defaultValue = "") String searchSupplier) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");


        if(StringUtils.containsIgnoreCase(fab, "LCM")) {
            String ver = mrpService.getLastMrpVer(fab, MrpVer.Type_Lcm);
            List<Map> mapList = new ArrayList<>();
            Long total = 0L;
            Page<MrpLcmMaterial> p = mrpService.getPageMrpLcmMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
            total = p.getTotalElements();
            List<MrpLcmMaterial> materialList = p.getContent();
            for(MrpLcmMaterial mrpLcmMaterial : materialList) {
                Map map = new HashMap();
                map.put("material", mrpLcmMaterial.getMaterial());
                map.put("fab", mrpLcmMaterial.getFab());
                map.put("materialGroup", mrpLcmMaterial.getMaterialGroup());
                map.put("materialName", mrpLcmMaterial.getMaterialName());
                map.put("products", mrpLcmMaterial.getProducts());
                List<MrpLcm> mrpLcmList = mrpService.getMrpLcm(ver, mrpLcmMaterial.getMaterial());
                for(MrpLcm mrpLcm : mrpLcmList) {
                    map.put(mrpLcm.getFabDate().toString(), mrpLcm.getShortQty());

                    String month = sdf.format(mrpLcm.getFabDate());
                    if(map.get(month) == null) {
                        map.put(month, 0d);
                    }
                    map.put(month, DoubleUtil.sum((double)map.get(month), mrpLcm.getShortQty()));
                }

                //客户
                if(StringUtils.isNotEmpty(mrpLcmMaterial.getProducts())) {
                   List<String> productList = new ArrayList<>(Arrays.asList(mrpLcmMaterial.getProducts().split(",")));
                    List<String> customerList = productCustomerService.getCustomer(productList);
                    String customers = "";
                    if(customerList != null && customerList.size()>0) {
                        for(int i=0; i<customerList.size(); i++) {
                            if(i != 0) {
                                customers += ",";
                            }
                            customers += customerList.get(i);
                        }
                    }
                    map.put("customers", customers);
                }
                mapList.add(map);
            }
            return ResultUtil.successPage(mapList, total);
        } else if(StringUtils.equalsIgnoreCase(fab, "ARY")) {
            String ver = mrpService.getLastMrpVer(fab, MrpVer.Type_Ary);
            List<Map> mapList = new ArrayList<>();
            Long total = 0L;
            Page<MrpAryMaterial> p = mrpService.getPageMrpAryMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
            total = p.getTotalElements();
            List<MrpAryMaterial> materialList = p.getContent();
            for(MrpAryMaterial mrpAryMaterial : materialList) {
                Map map = new HashMap();
                map.put("material", mrpAryMaterial.getMaterial());
                map.put("fab", mrpAryMaterial.getFab());
                map.put("materialGroup", mrpAryMaterial.getMaterialGroup());
                map.put("materialName", mrpAryMaterial.getMaterialName());
                map.put("products", mrpAryMaterial.getProducts());
                List<MrpAry> mrpAryList = mrpService.getMrpAry(ver, mrpAryMaterial.getMaterial());
                for(MrpAry mrpAry : mrpAryList) {
                    map.put(mrpAry.getFabDate().toString(), mrpAry.getShortQty());

                    String month = sdf.format(mrpAry.getFabDate());
                    if(map.get(month) == null) {
                        map.put(month, 0d);
                    }
                    map.put(month, DoubleUtil.sum((double)map.get(month), mrpAry.getShortQty()));
                }

                mapList.add(map);
            }
            return ResultUtil.successPage(mapList, total);

        } else if(StringUtils.equalsIgnoreCase(fab, "CELL")) {
            String ver = mrpService.getLastMrpVer(fab, MrpVer.Type_Cell);
            List<Map> mapList = new ArrayList<>();
            Long total = 0L;
            Page<MrpCellMaterial> p = mrpService.getPageMrpCellMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
            total = p.getTotalElements();
            List<MrpCellMaterial> materialList = p.getContent();
            for(MrpCellMaterial mrpCellMaterial : materialList) {
                Map map = new HashMap();
                map.put("material", mrpCellMaterial.getMaterial());
                map.put("fab", mrpCellMaterial.getFab());
                map.put("materialGroup", mrpCellMaterial.getMaterialGroup());
                map.put("materialName", mrpCellMaterial.getMaterialName());
                map.put("products", mrpCellMaterial.getProducts());
                List<MrpCell> mrpCellList = mrpService.getMrpCell(ver, mrpCellMaterial.getMaterial());
                for(MrpCell mrpCell : mrpCellList) {
                    map.put(mrpCell.getFabDate().toString(), mrpCell.getShortQty());

                    String month = sdf.format(mrpCell.getFabDate());
                    if(map.get(month) == null) {
                        map.put(month, 0D);
                    }
                    map.put(month, DoubleUtil.sum((double)map.get(month), mrpCell.getShortQty()));
                }

                mapList.add(map);
            }
            return ResultUtil.successPage(mapList, total);
        }
        return ResultUtil.successPage(new ArrayList(), 0);
    }


    @RequestMapping("/getLastMrpVer")
    public Result getLastMrpVer(String fab) {
        String ver = "";
        if(StringUtils.containsIgnoreCase(fab, "LCM")) {
            ver = mrpService.getLastMrpVer(fab, MrpVer.Type_Lcm);
        } else if(StringUtils.equalsIgnoreCase(fab, "ARY")) {
            ver = mrpService.getLastMrpVer(fab, MrpVer.Type_Ary);
        } else if(StringUtils.equalsIgnoreCase(fab, "CELL")) {
            ver = mrpService.getLastMrpVer(fab, MrpVer.Type_Cell);
        }
        return ResultUtil.success("", ver);
    }
}
