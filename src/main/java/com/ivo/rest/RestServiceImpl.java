package com.ivo.rest;

import com.ivo.mrp.entity.Inventory;
import com.ivo.mrp.service.InventoryService;
import com.ivo.mrp.service.PositionService;
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

    private PositionService positionService;

    private InventoryService inventoryService;

    @Autowired
    public RestServiceImpl(EifMapper eifMapper, FcstMapper fcstMapper, OracleMapper oracleMapper,
                           DpsLcmMapper dpsLcmMapper, DpsAryCellMapper dpsAryCellMapper,
                           PositionService positionService,InventoryService inventoryService) {
        this.eifMapper = eifMapper;
        this.fcstMapper = fcstMapper;
        this.oracleMapper = oracleMapper;
        this.dpsLcmMapper = dpsLcmMapper;
        this.dpsAryCellMapper = dpsAryCellMapper;
        this.positionService = positionService;
        this.inventoryService = inventoryService;
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
        log.info("从MPS数据库同步Ary的BOM的15料号");
        return fcstMapper.getBomAry();
    }

    @Override
    public List<Map> getMaterialByAryMtrl(String aryMtr) {
        log.info("从81数据库ARY的15料号查询51料号");
        return eifMapper.getMaterialByAryMtrl(aryMtr);
    }

    @Override
    public List<Map> getCellMtrl(String cellMtrl) {
        log.info("从Oracle数据库V_BOM_CELL_C获取CELL成品料号的材料");
        return oracleMapper.getCellMtrl(cellMtrl);
    }

    public List<Map> getCellMtrl() {
        log.info("从Oracle数据库V_BOM_CELL_C获取CELL成品料号的材料");
        return oracleMapper.getCellMaterial();
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

    @Override
    public List<Map> getDpsPol(String ver) {
        log.info("从2.75 DPS数据库获取CELL POL的DPS数据");
        return dpsAryCellMapper.getDpsPol(ver);
    }

    @Override
    public double getGoodInventory(String plant, String material, Date fabDate) {
        log.info("从Oracle数据库获取良品仓库存");
        Double d;
        // LCM1 IVE
        // LCM2/ARY/CELL IVO
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            String fab = "3000";
            List<String> positionList = positionService.getPositionIveGood();
            d = oracleMapper.getInventory(material, fabDate.toString(), fab, positionList);
        } else {
            String fab = "1000";
            List<String> positionList = positionService.getPositionIvoGood();
            d =  oracleMapper.getInventory(material, fabDate.toString(), fab, positionList);
        }
        return d == null ? 0 : d;
    }

    @Override
    public double getDullInventory(String plant, String material, Date fabDate) {
        log.info("从Oracle数据库获取良品仓库存");
        Double d;
        // LCM1 IVE
        // LCM2/ARY/CELL IVO
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            String fab = "3000";
            List<String> positionList = positionService.getPositionIveDull();
            d = oracleMapper.getInventory(material, fabDate.toString(), fab, positionList);
        } else {
            String fab = "1000";
            List<String> positionList = positionService.getPositionIvoDull();
            d =  oracleMapper.getInventory(material, fabDate.toString(), fab, positionList);
        }
        return d == null ? 0 : d;
    }

    @Override
    public List<Map> getGoodInventory(String plant, List<String> materialList, Date fabDate) {
        return inventoryService.getGoodInventory(plant, materialList, fabDate);
    }

    @Override
    public List<Map> getDullInventory(String plant, List<String> materialList, Date fabDate) {
        return inventoryService.getDullInventory(plant, materialList, fabDate);
    }

    @Override
    public List<Map> getSupplierMaterial() {
        log.info("从81数据库获取料号供应商数据");
        return eifMapper.getSupplierMaterial();
    }

    @Override
    public List<Map> getActualArrivalQty(Date fabDate) {
        log.info("从81数据库获取供应商的实际到货量");
        return eifMapper.getActualArrivalQty(fabDate);
    }

    @Override
    public List<Map> getAryMps(String ver) {
        return dpsAryCellMapper.getAryMps(ver);
    }

    @Override
    public List<String> getAryMpsVer() {
        return dpsAryCellMapper.getAryMpsVer();
    }

    @Override
    public List<Map> getCellMps(String ver) {
        return dpsAryCellMapper.getCellMps(ver);
    }

    @Override
    public List<String> getCellMpsVer() {
        return dpsAryCellMapper.getCellMpsVer();
    }


    @Override
    public List<String> getMpsDateOfInsertForVersion() {
        return fcstMapper.getMpsDateOfInsertForVersion();
    }

    @Override
    public List<Map> getAryMpsData(String dateOfInsert) {
        return fcstMapper.getAryMpsDate(dateOfInsert);
    }

    @Override
    public List<Map> getCellMpsData(String dateOfInsert) {
        return fcstMapper.getCellMpsDate(dateOfInsert);
    }


    @Override
    public List<Map> getBomPackageLcm1() {
        return eifMapper.getBomPackageLcm1();
    }

    @Override
    public List<Map> getBomPackageLcm2() {
        return eifMapper.getBomPackageLcm2();
    }

    @Override
    public List<Map> getMpsLcm(String version) {
        return fcstMapper.getMpsLcm(version);
    }


    @Override
    public List<Map> getCellMpsMode() {
        log.info("从MPS数据库获取CELL的MPS机种命名");
        return fcstMapper.getCellMpsMode();
    }

    @Override
    public List<String> getMpsLcmVersion() {
        return fcstMapper.getMpsLcmVersion();
    }

    @Override
    public List<Map> getAryMpsMode() {
        return fcstMapper.getAryMpsMode();
    }

    @Override
    public List<String> getTftMatrialByCellMtrlFroAry(List<String> cellMtrlList) {
        return oracleMapper.getTftMatrialByCellMtrlFroAry(cellMtrlList);
    }

    @Override
    public List<Map> getAryMatrialByTftMtrl(List<String> tftMtrlList) {
        return eifMapper.getAryMatrialByTftMtrl(tftMtrlList);
    }
}
