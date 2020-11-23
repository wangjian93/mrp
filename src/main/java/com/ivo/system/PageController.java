package com.ivo.system;

import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.MrpVer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 系统页面
 * @author wj
 * @version 1.0
 */
@Controller
public class PageController {

    private static final String lossRate_view = "page/mrp/lossRate";
    private static final String substitute_view = "page/mrp/substitute";
    private static final String bom_ary_view = "page/mrp/bom/bomAry";
    private static final String bom_cell_view = "page/mrp/bom/bomCell";
    private static final String bom_lcm1_view = "page/mrp/bom/bomLcm1";
    private static final String bom_lcm2_view = "page/mrp/bom/bomLcm2";
    private static final String bom_pol_view = "page/mrp/bom/bomPol";
    private static final String bom_package_view = "page/mrp/bom/bomPackage";

    private static final String dps_ver_lcm_view = "page/mrp/dps/dpsVerLcm";
    private static final String dps_ver_ary_view = "page/mrp/dps/dpsVerAry";
    private static final String dps_ver_cell_view = "page/mrp/dps/dpsVerCell";
    private static final String dps_ver_package_view = "page/mrp/dps/dpsVerPackage";
    private static final String dps_ver_pol_view = "page/mrp/dps/dpsVerPol";

    private static final String dps_lcm_view = "page/mrp/dps/dpsLcm";
    private static final String dps_ary_view = "page/mrp/dps/dpsLcm";
    private static final String dps_cell_view = "page/mrp/dps/dpsLcm";
    private static final String dps_package_view = "page/mrp/dps/dpsLcm";
    private static final String dps_pol_view = "page/mrp/dps/dpsLcm";

    private static final String mrp_ver_lcm_view = "page/mrp/mrp/mrpVerLcm";
    private static final String mrp_ver_ary_view = "page/mrp/mrp/mrpVerAry";
    private static final String mrp_ver_cell_view = "page/mrp/mrp/mrpVerCell";
    private static final String mrp_ver_package_view = "page/mrp/mrp/mrpVerPackage";
    private static final String mrp_ver_pol_view = "page/mrp/mrp/mrpVerPol";

    private static final String mrp_lcm_view = "page/mrp/mrp/mrpLcm";
    private static final String mrp_ary_view = "page/mrp/mrp/mrpLcm";
    private static final String mrp_cell_view = "page/mrp/mrp/mrpLcm";
    private static final String mrp_package_view = "page/mrp/mrp/mrpLcm";
    private static final String mrp_pol_view = "page/mrp/mrp/mrpLcm";

    private static final String arrivalPlan_lcm_view = "page/mrp/arrivalPlan/arrivalPlanLcm";
    private static final String arrivalPlan_ary_view = "page/mrp/arrivalPlan/arrivalPlanAry";
    private static final String arrivalPlan_cell_view = "page/mrp/arrivalPlan/arrivalPlanCell";

    private static final String supplier_view = "page/mrp/supplier";

    private static final String actualArrival_view = "page/mrp/actualArrival";

    private static final String position_view = "page/mrp/position";


    private static final String mps_ver_lcm_view = "page/mrp/mps/mpsVerLcm";
    private static final String mps_ver_ary_view = "page/mrp/mps/mpsVerAry";
    private static final String mps_ver_cell_view = "page/mrp/mps/mpsVerCell";


    /**
     * 损耗率
     * @return String
     */
    @GetMapping("/lossRate")
    public String lossRate() {
        return lossRate_view;
    }

    /**
     * 替代料
     * @return String
     */
    @GetMapping("/substitute")
    public String substitute() {
        return substitute_view;
    }

    /**
     * LCM1 BOM
     * @return String
     */
    @GetMapping("/bomLcm1")
    public String bomLcm1() {
        return bom_lcm1_view;
    }

    /**
     * LCM2 BOM
     * @return String
     */
    @GetMapping("/bomLcm2")
    public String bomLcm2() {
        return bom_lcm2_view;
    }

    /**
     * CELL BOM
     * @return String
     */
    @GetMapping("/bomCell")
    public String bomCell() {
        return bom_cell_view;
    }

    /**
     * ARY BOM
     * @return String
     */
    @GetMapping("/bomAry")
    public String bomAry() {
        return bom_ary_view;
    }

    /**
     * 包材 BOM
     * @return String
     */
    @GetMapping("/bomPackage")
    public String bomPackage() {
        return bom_package_view;
    }

    /**
     * POL BOM
     * @return String
     */
    @GetMapping("/bomPol")
    public String bomPol() {
        return bom_pol_view;
    }

    /**
     * DPS版本
     * @param type DPS类型
     * @return String
     */
    @GetMapping("/dpsVer/{type}")
    public String dpsVer(@PathVariable String type) {
        if(StringUtils.equalsIgnoreCase(type, DpsVer.Type_Ary)) {
            return dps_ver_ary_view;
        }
        if(StringUtils.equalsIgnoreCase(type, DpsVer.Type_Cell)) {
            return dps_ver_cell_view;
        }
        if(StringUtils.equalsIgnoreCase(type, DpsVer.Type_Lcm)) {
            return dps_ver_lcm_view;
        }
        if(StringUtils.equalsIgnoreCase(type, "Package")) {
            return dps_ver_package_view;
        }
        if(StringUtils.equalsIgnoreCase(type, DpsVer.Type_Pol)) {
            return dps_ver_pol_view;
        }
        return null;
    }

    /**
     * MRP版本
     * @param type MRP类型
     * @return String
     */
    @GetMapping("/mrpVer/{type}")
    public String mrpVer(@PathVariable String type) {
        if(StringUtils.equalsIgnoreCase(type, MrpVer.Type_Ary)) {
            return mrp_ver_ary_view;
        }
        if(StringUtils.equalsIgnoreCase(type, MrpVer.Type_Cell)) {
            return mrp_ver_cell_view;
        }
        if(StringUtils.equalsIgnoreCase(type, MrpVer.Type_Lcm)) {
            return mrp_ver_lcm_view;
        }
        if(StringUtils.equalsIgnoreCase(type, "Package")) {
            return mrp_ver_package_view;
        }
        if(StringUtils.equalsIgnoreCase(type, MrpVer.Type_Pol)) {
            return mrp_ver_pol_view;
        }
        return null;
    }

    /**
     * 供应商到货计划
     * @param fab 产别
     * @return String
     */
    @GetMapping("/arrivalPlan/{fab}")
    public String arrivalPlan(@PathVariable String fab) {
        if(StringUtils.equalsIgnoreCase(fab, MrpVer.Type_Ary)) {
            return arrivalPlan_ary_view;
        }
        if(StringUtils.equalsIgnoreCase(fab, MrpVer.Type_Cell)) {
            return arrivalPlan_cell_view;
        }
        if(StringUtils.equalsIgnoreCase(fab, "LCM")) {
            return arrivalPlan_lcm_view;
        }
        return null;
    }

    /**
     * 供应商信息页面
     * @return String
     */
    @GetMapping("/supplier")
    public String supplier() {
        return supplier_view;
    }

    /**
     * 实际到货数据页面
     * @return String
     */
    @GetMapping("/actualArrival")
    public String actualArrival() {
        return actualArrival_view;
    }

    /**
     * 仓位
     * @return String
     */
    @GetMapping("/position")
    public String position() {
        return position_view;
    }

    /**
     * MPS版本
     * @param type DPS类型
     * @return String
     */
    @GetMapping("/mpsVer/{type}")
    public String mpsVer(@PathVariable String type) {
        if(StringUtils.equalsIgnoreCase(type, DpsVer.Type_Ary)) {
            return mps_ver_ary_view;
        }
        if(StringUtils.equalsIgnoreCase(type, DpsVer.Type_Cell)) {
            return mps_ver_cell_view;
        }
        if(StringUtils.equalsIgnoreCase(type, DpsVer.Type_Lcm)) {
            return mps_ver_lcm_view;
        }
        return null;
    }

}
