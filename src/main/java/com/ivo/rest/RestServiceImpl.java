package com.ivo.rest;

import com.ivo.rest.dpsAryCell.DpsAryCellMapper;
import com.ivo.rest.dpsLcm.DpsLcmMapper;
import com.ivo.rest.eif.EifMapper;
import com.ivo.rest.fcst.FcstMapper;
import com.ivo.rest.oracle.OracleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class RestServiceImpl implements RestService {

    private EifMapper eifMapper;

    private FcstMapper fcstMapper;

    private OracleMapper oracleMapper;

    private DpsLcmMapper dpsLcmMapper;

    private DpsAryCellMapper dpsAryCellMapper;

    @Autowired
    public RestServiceImpl(EifMapper eifMapper, FcstMapper fcstMapper, OracleMapper oracleMapper,
                           DpsLcmMapper dpsLcmMapper, DpsAryCellMapper dpsAryCellMapper) {
        this.eifMapper = eifMapper;
        this.fcstMapper = fcstMapper;
        this.oracleMapper = oracleMapper;
        this.dpsLcmMapper = dpsLcmMapper;
        this.dpsAryCellMapper = dpsAryCellMapper;
    }

    @Override
    public List<Map> getMaterialGroup() {
        log.info("从81数据库的表MM_O_MaterialGroup同步数据");
        return eifMapper.getMaterialGroup();
    }

    @Override
    public List<Map> getMaterial() {
        log.info("从81数据库的表MM_O_Material同步数据");
        return eifMapper.getMaterial();
    }

    @Override
    public List<Map> getProject() {
        log.info("从81数据库的表BG_O_Project同步数据");
        return eifMapper.getProject();
    }

    @Override
    public List<Map> getBomCell() {
        log.info("从MPS数据库同步CELL的BOM成品料号");
        return fcstMapper.getBomCell();
    }

    @Override
    public List<Map> getBomLcm1() {
        log.info("从81数据库MM_V_BOM_M1获取LCM1的BOM");
        return eifMapper.getBomLcm1();
    }

    @Override
    public List<Map> getBomLcm2() {
        log.info("从81数据库MM_V_BOM_M2获取LCM2的BOM");
        return eifMapper.getBomLcm2();
    }

    @Override
    public List<Map> getBomAry() {
        log.info("从81数据库MM_O_ARYBOM获取ARY的BOM");
        return eifMapper.getBomAry();
    }

    @Override
    public List<Map> getCellMtrl(String cellMtrl) {
        log.info("从Oracle数据库V_BOM_CELL_C获取成品料号的材料");
        return oracleMapper.getCellMtrl(cellMtrl);
    }

    @Override
    public List<Map> getMaterialSubstituteLcm1() {
        log.info("从81数据库mm_v_bom_m1replace获取LCM1的替代料关系");
        return eifMapper.getMaterialSubstituteLcm1();
    }

    @Override
    public List<Map> getMaterialSubstituteLcm2() {
        log.info("从81数据库mm_v_bom_m2replace获取LCM2的替代料关系");
        return eifMapper.getMaterialSubstituteLcm2();
    }

    @Override
    public List<Map> getMaterialSubstituteCell() {
        log.info("从81数据库MM_O_CELLBOM_REPLACE获取CELL的替代料关系");
        return eifMapper.getMaterialSubstituteCell();
    }

    @Override
    public List<Map> getDpsLcm(String ver) {
        log.info("从2.115 DPS数据库获取LCM DPS数据");
        return dpsLcmMapper.getDpsLcm(ver);
    }

    @Override
    public List<String> getDpsLcmVer() {
        log.info("从2.115 DPS数据库获取LCM DPS的版本");
        return dpsLcmMapper.getDpsLcmVer();
    }



    @Override
    public List<Map> getDpsCell(String ver) {
        log.info("从2.115 DPS数据库获取LCM DPS的版本");
        return dpsAryCellMapper.getDpsCell(ver);
    }

    @Override
    public List<Map> getDpsAry(String ver) {
        log.info("从2.75 DPS数据库获获取ARY的DPS数据 (Array Input)");
        return dpsAryCellMapper.getDpsAry(ver);
    }

    @Override
    public List<String> getDpsCellAryVer() {
        log.info("从2.75 DPS数据库获取CELL/ARY DPS PC上传的版本");
        return dpsAryCellMapper.getDpsCellAryVer();
    }

    @Override
    public List<Map> getDpsAryOc(String ver) {
        log.info("从2.75 DPS数据库获取ARY OC材料的DPS数据");
        return dpsAryCellMapper.getDpsAryOc(ver);
    }

    @Override
    public List<Map> getDpsPackage(String ver, List<String> productList) {
        log.info("从2.75 DPS数据库获取CELL包材的DPS数据");
        return dpsAryCellMapper.getDpsPackage(ver, productList);
    }



    // 良品仓位
    private static final String[] position_lcm1 = {"3010","3150", "4010"};
    private static final String[] position_lcm2 = {"2100","2101", "5610", "5611", "1400", "1401"};
    private static final String[] position_cell = {"1100", "1111", "1200", "5300", "1400", "1401"};
    private static final String[] position_ary = {"1100", "1111", "1200", "5300", "1400", "1401"};
    // 呆滞品仓位
    private static final String[] positionDull_lcm1 = {"3060"};
    private static final String[] positionDull_lcm2 = {"1310","1311"};
    private static final String[] positionDull_cell = {"1310","1311"};
    private static final String[] positionDull_ary = {"1310","1311"};
    // 厂别
    private static final String plant_lcm1 = "3000";
    private static final String plant_lcm2 = "1000";
    private static final String plant_cell = "1000";
    private static final String plant_ary = "1000";
    @Override
    public double getGoodInventory(String plant, String material, Date fabDate) {
        log.info("从Oracle数据库获取良品仓库存");
        Double d = null;
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            d = oracleMapper.getInventory(material, fabDate.toString(), plant_lcm1, Arrays.asList(position_lcm1));
        } else
        if(StringUtils.equalsIgnoreCase(plant, "LCM2")) {
            d =  oracleMapper.getInventory(material, fabDate.toString(), plant_lcm2, Arrays.asList(position_lcm2));
        } else
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            d =  oracleMapper.getInventory(material, fabDate.toString(), plant_cell, Arrays.asList(position_cell));
        }
        if(StringUtils.equalsIgnoreCase(plant, "ARY")) {
            d =  oracleMapper.getInventory(material, fabDate.toString(), plant_ary, Arrays.asList(position_ary));
        }
        return d == null ? 0 : d;
    }

    @Override
    public double getDullInventory(String plant, String material, Date fabDate) {
        log.info("从Oracle数据库获取呆滞料库存");
        Double d = null;
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            d = oracleMapper.getInventory(material, fabDate.toString(), plant_lcm1, Arrays.asList(positionDull_lcm1));
        } else
        if(StringUtils.equalsIgnoreCase(plant, "LCM2")) {
            d =  oracleMapper.getInventory(material, fabDate.toString(), plant_lcm2, Arrays.asList(positionDull_lcm2));
        } else
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            d =  oracleMapper.getInventory(material, fabDate.toString(), plant_cell, Arrays.asList(positionDull_cell));
        }
        if(StringUtils.equalsIgnoreCase(plant, "ARY")) {
            d =  oracleMapper.getInventory(material, fabDate.toString(), plant_ary, Arrays.asList(positionDull_ary));
        }
        return d == null ? 0 : d;
    }

    @Override
    public List<Map> getGoodInventory(String plant, List<String> materialList, Date fabDate) {
        log.info("从Oracle数据库获取良品仓库存");
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            return oracleMapper.getInventoryBatch( materialList, fabDate.toString(), plant_lcm1, Arrays.asList(position_lcm1));
        } else
        if(StringUtils.equalsIgnoreCase(plant, "LCM2")) {
            return oracleMapper.getInventoryBatch(materialList, fabDate.toString(), plant_lcm2, Arrays.asList(position_lcm2));
        } else
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            return oracleMapper.getInventoryBatch(materialList, fabDate.toString(), plant_cell, Arrays.asList(position_cell));
        }
        if(StringUtils.equalsIgnoreCase(plant, "ARY")) {
            return oracleMapper.getInventoryBatch(materialList, fabDate.toString(), plant_ary, Arrays.asList(position_ary));
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map> getDullInventory(String plant, List<String> materialList, Date fabDate) {
        log.info("从Oracle数据库获取呆滞料库存");
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            return oracleMapper.getInventoryBatch( materialList, fabDate.toString(), plant_lcm1, Arrays.asList(positionDull_lcm1));
        } else
        if(StringUtils.equalsIgnoreCase(plant, "LCM2")) {
            return oracleMapper.getInventoryBatch(materialList, fabDate.toString(), plant_lcm2, Arrays.asList(positionDull_lcm2));
        } else
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            return oracleMapper.getInventoryBatch(materialList, fabDate.toString(), plant_cell, Arrays.asList(positionDull_cell));
        }
        if(StringUtils.equalsIgnoreCase(plant, "ARY")) {
            return oracleMapper.getInventoryBatch(materialList, fabDate.toString(), plant_ary, Arrays.asList(positionDull_ary));
        }
        return new ArrayList<>();
    }
}
