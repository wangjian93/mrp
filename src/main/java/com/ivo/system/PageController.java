package com.ivo.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

}
